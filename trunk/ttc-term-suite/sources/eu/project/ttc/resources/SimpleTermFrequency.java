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

import java.lang.Character.UnicodeBlock;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import eu.project.ttc.models.Context;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;

public class SimpleTermFrequency implements SharedResourceObject {

	public SimpleTermFrequency() {
		this.setFrequencies();
		this.setCategories();
		this.setContexts();
		this.setForms();
	}

	private Map<String, Integer> frequencies;

	private void setFrequencies() {
		this.frequencies = new HashMap<String, Integer>();
	}

	public Map<String, Integer> getFrequencies() {
		return this.frequencies;
	}

	private Map<String, String> categories;

	private void setCategories() {
		this.categories = new HashMap<String, String>();
	}

	public Map<String, String> getCategories() {
		return this.categories;
	}

	private Map<String, Map<String, Integer>> forms;

	private void setForms() {
		this.forms = new HashMap<String, Map<String, Integer>>();
	}

	public Map<String, Map<String, Integer>> getForms() {
		return this.forms;
	}

	public Map<String, Integer> getFormFreqs(String term) {
		return forms.get(term);
	}

	protected String add(TermAnnotation annotation) {
		String term = annotation.getLemma().toLowerCase()
				.replaceAll("\\s+", " ").trim();
		String form = annotation.getCoveredText().trim();
		if (term == null) {
			return null;
		} else if (term.length() <= 2) {
			return null;
		} else if (term.length() > 50) {
			return null;
		} else if (term.startsWith("http:") || term.startsWith("www")) {
			return null;
		} else if (this.allow(term)) {

			// Increase term frequency
			Integer frequency = frequencies.get(term);
			frequencies.put(term, frequency == null ? 1
					: frequency.intValue() + 1);
			categories.put(term, annotation.getCategory());

			// Handle forms
			Map<String, Integer> termForms = forms.get(term);
			if (termForms == null) {
				termForms = new TreeMap<String, Integer>();
				forms.put(term, termForms);
			}

			// Increse term forms frequencies
			Integer formFreq = termForms.get(form);
			termForms.put(form, formFreq == null ? 1 : formFreq.intValue() + 1);
			return term;
		} else {
			return null;
		}
	}

	private boolean allow(String term) {
		char ch = term.charAt(0);
		int type = Character.getType(ch);
		UnicodeBlock unicode = Character.UnicodeBlock.of(ch);
		if (type == Character.LOWERCASE_LETTER) {
			return true;
		} else if (unicode == Character.UnicodeBlock.CYRILLIC) {
			return true;
		} else if (unicode == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
			return true;
		} else {
			return false;
		}
	}

	public void addEntry(SingleWordTermAnnotation annotation) {
		this.add(annotation);
	}

	private Map<String, Context> contexts;

	private void setContexts() {
		this.contexts = new HashMap<String, Context>();
	}

	public Map<String, Context> getContexts() {
		return this.contexts;
	}

	public void setCoOccurrences(String term, String context,
			Double coOccurrences, int mode) {
		Context termContext = this.getContexts().get(term);
		if (termContext == null) {
			termContext = new Context();
			this.getContexts().put(term, termContext);
		}
		termContext.setCoOccurrences(context, coOccurrences, mode);
	}

	public void addCoOccurrences(String term, String context) {
		Context termContext = this.getContexts().get(term);
		if (termContext == null) {
			termContext = new Context();
			this.getContexts().put(term, termContext);
		}
		termContext.setCoOccurrences(context, new Double(1), Context.ADD_MODE);
	}

	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
	}

}
