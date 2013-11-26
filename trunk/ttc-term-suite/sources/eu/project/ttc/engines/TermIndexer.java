/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eu.project.ttc.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import eu.project.ttc.metrics.AssociationRate;
import eu.project.ttc.models.Component;
import eu.project.ttc.models.Context;
import eu.project.ttc.models.CrossTable;
import eu.project.ttc.resources.ComplexTermFrequency;
import eu.project.ttc.resources.SimpleTermFrequency;
import eu.project.ttc.types.FormAnnotation;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import uima.sandbox.indexer.engines.Indexer;

public class TermIndexer extends Indexer {

	private SimpleTermFrequency singleWordTermFrequency;

	private void setSingleWordTermFrequency(SimpleTermFrequency termFrequency) {
		this.singleWordTermFrequency = termFrequency;
	}

	private SimpleTermFrequency getSingleWordTermFrequency() {
		return this.singleWordTermFrequency;
	}

	private ComplexTermFrequency multiWordTermFrequency;

	private void setMultiWordTermFrequency(ComplexTermFrequency termFrequency) {
		this.multiWordTermFrequency = termFrequency;
	}

	private ComplexTermFrequency getMultiWordTermFrequency() {
		return this.multiWordTermFrequency;
	}

	private String language;

	private void setLanguage(String language) {
		this.language = language;
	}

	private String getLanguage() {
		return this.language;
	}

	private AssociationRate associationRate;

	private void setAssociationRate(String name) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		if (this.associationRate == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof AssociationRate) {
				this.associationRate = (AssociationRate) obj;
				UIMAFramework.getLogger().log(
						Level.INFO,
						"Setting Association Rate: "
								+ this.associationRate.getClass()
										.getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name
						+ "' doesn't comply "
						+ AssociationRate.class.getCanonicalName());
			}
		}
	}

	private AssociationRate getAssociationRate() {
		return this.associationRate;
	}

	@Override
	public void initialize() throws Exception {
		if (this.getSingleWordTermFrequency() == null) {
			SimpleTermFrequency singleWordTermFrequency = (SimpleTermFrequency) this
					.getContext().getResourceObject("SimpleTermFrequency");
			this.setSingleWordTermFrequency(singleWordTermFrequency);
		}
		if (this.getMultiWordTermFrequency() == null) {
			ComplexTermFrequency multiWordTermFrequency = (ComplexTermFrequency) this
					.getContext().getResourceObject("ComplexTermFrequency");
			this.setMultiWordTermFrequency(multiWordTermFrequency);
		}
		if (this.getLanguage() == null) {
			String language = (String) this.getContext()
					.getConfigParameterValue("Language");
			this.setLanguage(language);
		}
		if (this.getAssociationRate() == null) {
			String className = (String) this.getContext()
					.getConfigParameterValue("AssociationRateClassName");
			this.setAssociationRate(className);
		}
	}

	private void process(JCas cas, boolean mode) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation sdi = (SourceDocumentInformation) iterator
					.next();
			if (mode) {
				this.getContext().getLogger()
						.log(Level.INFO, "Indexing " + sdi.getUri());
			} else {
				this.getContext().getLogger()
						.log(Level.INFO, "Releasing " + sdi.getUri());
			}
		}
	}

	@Override
	public void update(JCas cas) throws Exception {
		this.process(cas, true);
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (annotation instanceof SingleWordTermAnnotation) {
				this.getSingleWordTermFrequency().addEntry(
						(SingleWordTermAnnotation) annotation);
				this.update(annotation);
			} else if (annotation instanceof MultiWordTermAnnotation) {
				this.getMultiWordTermFrequency().addEntry(
						(MultiWordTermAnnotation) annotation);
			}
		}
	}

	private void update(TermAnnotation annotation) throws Exception {
		FSArray array = annotation.getContext();
		if (array == null) {
			return;
		} else {
			String term = annotation.getLemma().toLowerCase();
			Integer freq = this.getSingleWordTermFrequency().getFrequencies()
					.get(term);
			if (freq == null || freq.intValue() < 2) {
				return;
			} else {
				for (int index = 0; index < array.size(); index++) {
					TermAnnotation entry = annotation.getContext(index);
					String context = entry.getLemma().toLowerCase();
					this.getSingleWordTermFrequency().addCoOccurrences(term,
							context);
				}
			}
		}
	}

	@Override
	public void release(JCas cas) throws Exception {
		this.process(cas, false);
		cas.setDocumentLanguage(this.getLanguage());
		StringBuilder builder = new StringBuilder();
		this.release(cas, builder, this.getSingleWordTermFrequency());
		this.release(cas, builder, this.getMultiWordTermFrequency());
		cas.setDocumentText(builder.toString());
		SourceDocumentInformation sdi = new SourceDocumentInformation(cas);
		sdi.setUri("http://" + this.getLanguage() + "-terminology.xmi");
		sdi.setBegin(0);
		sdi.setEnd(builder.length());
		sdi.setOffsetInSource(0);
		sdi.addToIndexes();
		this.normalize(this.getAssociationRate());
		this.annotate(cas);
	}

	private void annotate(JCas cas) throws Exception {
		Map<String, Context> contexts = new TreeMap<String, Context>();
		contexts.putAll(this.getSingleWordTermFrequency().getContexts());
		for (String item : contexts.keySet()) {
			Integer freq = this.getSingleWordTermFrequency().getFrequencies()
					.get(item);
			JCas jcas = cas.createView(item);
			jcas.setDocumentLanguage(this.getLanguage());
			StringBuilder builder = new StringBuilder();
			Context context = contexts.get(item);
			builder.append(context.toString());
			jcas.setDocumentText(builder.toString());
			TermAnnotation annotation = new TermAnnotation(jcas, 0, jcas
					.getDocumentText().length());
			annotation.setOccurrences(freq.intValue());
			annotation.addToIndexes();
		}
	}

	private void normalize(AssociationRate rate) throws Exception {
		CrossTable crossTable = new CrossTable();
		Map<String, Context> contexts = this.getSingleWordTermFrequency()
				.getContexts();
		crossTable.setContexts(contexts);
		for (String term : contexts.keySet()) {
			Context context = contexts.get(term);
			crossTable.setTerm(term);
			for (String coTerm : context.getCoOccurrences().keySet()) {
				crossTable.setCoTerm(coTerm);
				crossTable.compute();
				int a = crossTable.getA();
				int b = crossTable.getB();
				int c = crossTable.getC();
				int d = crossTable.getD();
				double norm = rate.getValue(a, b, c, d);
				this.getSingleWordTermFrequency().setCoOccurrences(term,
						coTerm, new Double(norm), Context.DEL_MODE);
			}
		}
	}

	private void release(JCas cas, StringBuilder builder,
			SimpleTermFrequency frequency) {
		for (String entry : frequency.getFrequencies().keySet()) {
			int freq = frequency.getFrequencies().get(entry).intValue();
			String category = frequency.getCategories().get(entry);
			Set<String> forms = frequency.getForms().get(entry).keySet();
			int begin = builder.length();
			builder.append(entry);
			int end = builder.length();
			builder.append('\n');
			TermAnnotation annotation = new SingleWordTermAnnotation(cas,
					begin, end);
			annotation.setOccurrences(freq);
			annotation.setFrequency(freq);
			annotation.setCategory(category);
			annotation.setLemma(entry);
			annotation.addToIndexes();
			handleForms(entry, begin, forms, frequency, cas, annotation);
		}
	}

	private void release(JCas cas, StringBuilder builder,
			ComplexTermFrequency frequency) {
		for (String entry : frequency.getFrequencies().keySet()) {
			int freq = frequency.getFrequencies().get(entry).intValue();
			String category = frequency.getCategories().get(entry);
			List<Component> components = frequency.getComponents().get(entry);
			Set<String> forms = frequency.getForms().get(entry).keySet();
			int begin = builder.length();
			builder.append(entry);
			int end = builder.length();
			builder.append('\n');
			MultiWordTermAnnotation annotation = new MultiWordTermAnnotation(
					cas, begin, end);
			annotation.setOccurrences(freq);
			annotation.setFrequency(freq);
			annotation.setCategory(category);
			annotation.addToIndexes();
			if (components != null) {
				FSArray array = new FSArray(cas, components.size());
				annotation.setComponents(array);
				int i = 0;
				int start = begin;
				for (Component component : components) {
					int stop = start + component.length();
					TermComponentAnnotation c = new TermComponentAnnotation(
							cas, start, stop);
					component.release(c);
					c.addToIndexes();
					annotation.setComponents(i, c);
					i++;
					start = stop + 1;
				}
			}
			handleForms(entry, begin, forms, frequency, cas, annotation);
		}
	}

	private void handleForms(String entry, int begin, Set<String> forms,
			SimpleTermFrequency frequency, JCas cas, TermAnnotation annotation) {
		if (forms != null && !forms.isEmpty()) {
			// forms.add(entry);
			Map<String, Integer> newForms = gatherForms(entry, forms, frequency);
			newForms = sortByValue(newForms);
			FSArray array = new FSArray(cas, newForms.size());
			annotation.setForms(array);
			int i = 0;
			// System.out.println("Forms of \\" + entry + "\\");
			FormAnnotation formAnnot;
			for (String form : newForms.keySet()) {
				formAnnot = new FormAnnotation(cas, begin, begin
						+ form.length());
				formAnnot.setForm(form);
				formAnnot.setOccurrences(newForms.get(form));
				formAnnot.addToIndexes();
				annotation.setForms(i, formAnnot);
				i++;
				// System.out.println("\t" + form + " = " + newForms.get(form));
			}
			// System.out.println();
		}
	}

	private Map<String, Integer> gatherForms(String entry, Set<String> forms,
			SimpleTermFrequency frequency) {
		HashMap<String, Integer> newForms = new HashMap<String, Integer>();
		Integer mainFreq;
		Integer freq;
		int curr;
		for (String k : forms) {
			String term = k.toLowerCase().replaceAll("\\s+", " ").trim();
			mainFreq = frequency.getFrequencies().get(k);
			freq = frequency.getFormFreqs(entry).get(k);
			curr = freq == null ? (mainFreq == null ? 0 : mainFreq.intValue())
					: freq.intValue();
			if (newForms.containsKey(term))
				newForms.put(term, newForms.get(term) + curr);
			else
				newForms.put(term, curr);
		}
		return newForms;
	}

	public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
		ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		list.addAll(map.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				int comp = o2.getValue().compareTo(o1.getValue());
				return comp == 0 ? o1.getKey().compareTo(o2.getKey()) : comp;
			}
		});

		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
