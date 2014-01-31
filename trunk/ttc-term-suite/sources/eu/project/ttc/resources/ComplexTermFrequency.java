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
package eu.project.ttc.resources;

import eu.project.ttc.models.Component;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import eu.project.ttc.types.WordAnnotation;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplexTermFrequency extends AbstractMemBasedTermFrequency<MultiWordTermAnnotation> {

    private Map<String, List<Component>> components = new HashMap<String, List<Component>>();

	public Map<String, List<Component>> getComponents() {
		return this.components;
	}

	public void addEntry(MultiWordTermAnnotation annotation) {
		String term = this.add(annotation);
		if (term == null) {
			return;
		} else {
			if (annotation.getComponents() == null) {
				this.addSubComponents(annotation, term);
			} else {
				this.addComponents(annotation, term);
			}
		}
	}

	private void addForm(MultiWordTermAnnotation annotation,
			Map<String, MutableInt> forms) {
		String coveredText = annotation.getCoveredText().toLowerCase()
				.replaceAll("\\s+", " ");
		int size = annotation.getComponents().size();
		StringBuilder form = new StringBuilder();
		for (int index = 0; index < size; index++) {
			TermComponentAnnotation component = annotation.getComponents(index);
			if (component.getBegin() == annotation.getBegin()
					&& component.getEnd() == annotation.getEnd()) {
				continue;
			} else {
				form.append(component.getCoveredText());
				if (index < size - 1) {
					form.append(' ');
				}
			}
		}

		String formText = (coveredText.length() < form.length()) ? coveredText
				: form.toString();
        if (!forms.containsKey(formText))
            forms.put(formText, new MutableInt(0));
        MutableInt formFreq = forms.get(formText);
		formFreq.increment();
	}

	private void add(MultiWordTermAnnotation annotation, String term) {
        if (!frequencies.containsKey(term))
            frequencies.put(term, new MutableInt(0));
        MutableInt frequency = frequencies.get(term);
		frequency.increment();
		categories.put(term, annotation.getCategory());
		Map<String, MutableInt> forms = this.forms.get(term);
		if (forms == null) {
			forms = new HashMap<String, MutableInt>();
			this.forms.put(term, forms);
		}
		this.addForm(annotation, forms);
	}

	private String add(MultiWordTermAnnotation annotation) {
		if (annotation.getComponents() == null) {
			return addGeneric(annotation);
		} else {
			int size = annotation.getComponents().size();
			StringBuilder builder = new StringBuilder();
			for (int index = 0; index < size; index++) {
				TermComponentAnnotation component = annotation
						.getComponents(index);
				builder.append(component.getLemma());
				if (index < size - 1) {
					builder.append(' ');
				}
			}
			String term = builder.toString();
			this.add(annotation, term);
			return term;
		}
	}

	private void addComponents(MultiWordTermAnnotation annotation, String term) {
		int size = annotation.getComponents().size();
		List<Component> components = new ArrayList<Component>();
		for (int index = 0; index < size; index++) {
			TermComponentAnnotation component = annotation.getComponents(index);
			if (component.getBegin() == annotation.getBegin()
					&& component.getEnd() == annotation.getEnd()) {
				continue;
			} else {
				Component c = new Component();
				c.update(component);
				components.add(c);
			}
		}
		this.getComponents().put(term, components);
	}

	private void addSubComponents(MultiWordTermAnnotation annotation,
			String term) {
		try {
			JCas cas = annotation.getCAS().getJCas();
			AnnotationIndex<Annotation> index = cas
					.getAnnotationIndex(WordAnnotation.type);
			FSIterator<Annotation> iterator = index.subiterator(annotation);
			List<Component> components = new ArrayList<Component>();
			while (iterator.hasNext()) {
				WordAnnotation component = (WordAnnotation) iterator.next();
				if (component.getBegin() == annotation.getBegin()
						&& component.getEnd() == annotation.getEnd()) {
					continue;
				} else {
					Component c = new Component();
					c.update(component);
					components.add(c);
				}
			}
			this.getComponents().put(term, components);
		} catch (NullPointerException e) {
			UIMAFramework.getLogger().log(Level.SEVERE, term);
			UIMAFramework.getLogger().log(Level.SEVERE, annotation.toString());
			// ignore throw e;
		} catch (CASException e) {
			UIMAFramework.getLogger().log(Level.WARNING, e.getMessage());
		}
	}

    @Override
    public void close() {
        super.close();
        for(List<Component> compo : components.values())
            compo.clear();
        components.clear();
    }
}
