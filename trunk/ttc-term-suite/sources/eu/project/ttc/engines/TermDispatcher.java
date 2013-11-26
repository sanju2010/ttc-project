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

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasMultiplier_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.TranslationAnnotation;

public class TermDispatcher extends JCasMultiplier_ImplBase {
	
	private String sourceLanguage;
	
	private void setSourceLanguage(String language) {
		this.sourceLanguage = language;
	}
	
	private String getSourceLanguage() {
		return this.sourceLanguage;
	}
	
	private String targetLanguage;
	
	private void setTargetLanguage(String language) {
		this.targetLanguage = language;
	}
	
	private String getTargetLanguage() {
		return this.targetLanguage;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getSourceLanguage() == null) {
				String language = (String) context.getConfigParameterValue("SourceLanguage");
				this.setSourceLanguage(language);
			}
			if (this.getTargetLanguage() == null) {
				String language = (String) context.getConfigParameterValue("TargetLanguage");
				this.setTargetLanguage(language);
			}
			if (this.getCandidates() == null) {
				this.setCandidates();
			}
			this.last = false;
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private String term;
		
	private void setTerm(String term) {
		this.term = term == null ? null : term.trim();
	}
	
	private String getTerm() {
		return this.term;
	}
	
	private Set<String> candidates;
	
	private void setCandidates() {
		this.candidates = new HashSet<String>();
	}
	
	private Set<String> getCandidates() {
		return this.candidates;
	}
	
	private boolean last;
	
	private void setLast(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation sdi = (SourceDocumentInformation) iterator.next();
			this.last = sdi.getLastSegment();
		}
	}
	
	private boolean getLast() {
		return this.last;
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			this.setTerm(null);
			this.getCandidates().clear();
			this.setLast(cas);
			String text = cas.getDocumentText();
			String[] items = text.split("\t");
			this.setTerm(items[0]);
			for (int index = 1; index < items.length; index++) {
				this.getCandidates().add(items[index].trim());
			}
			this.enableHasNext(true);
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private boolean hasNext;

	private void enableHasNext(boolean enabled) {
		this.hasNext = enabled;
	}
	
	@Override
	public boolean hasNext() {
		return this.hasNext;
	}
	
	@Override
	public AbstractCas next() throws AnalysisEngineProcessException {
		this.enableHasNext(false);
		JCas cas = this.getEmptyJCas();
		try {
			cas.setDocumentLanguage(this.getSourceLanguage());
			cas.setDocumentText(this.getTerm());
			int begin = 0;
			int end = cas.getDocumentText().length();

			for(int i = this.getCandidates().size(); i > 0; i--) {
				TranslationAnnotation annotation = new TranslationAnnotation(cas, begin, end);
				annotation.setLanguage(this.getTargetLanguage());
				annotation.addToIndexes();
			}
			
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas, begin, end);
			sdi.setDocumentSize(end);
			sdi.setLastSegment(!this.getLast());
			sdi.setOffsetInSource(0);
			sdi.setUri("http://" + cas.getDocumentText() + ".term");
			sdi.addToIndexes();
			this.getContext().getLogger().log(Level.INFO,"Processing " + cas.getDocumentText());
			return cas;
		} catch (Exception e) {
			cas.release();
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
