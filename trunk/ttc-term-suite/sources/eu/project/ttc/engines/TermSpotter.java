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

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.matcher.engines.Matcher;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.WordAnnotation;

public class TermSpotter extends Matcher {
			
	@Override
	protected void annotate(JCas cas, String id, int begin, int end,Annotation[] annotations) {
		String language = cas.getDocumentLanguage();
		if (0 <= begin && begin < end && end <= cas.getDocumentText().length()) {
			if (annotations.length == 1) {
				SingleWordTermAnnotation term = new SingleWordTermAnnotation(cas, begin, end);
				WordAnnotation word = (WordAnnotation) annotations[0];
				term.setCategory(word.getCategory());
				if (word.getLemma() == null) {
					word.setLemma(word.getCoveredText().toLowerCase());
				} else if (word.getLemma().equals("@card@")) {
					word.setLemma(word.getCoveredText().toLowerCase());
				}
				term.setLemma(word.getLemma());
				term.addToIndexes();
			} else {
				MultiWordTermAnnotation term = new MultiWordTermAnnotation(cas, begin, end);
				// FIXME 
				if (language.equals("lv")) {
					FSArray array = new FSArray(cas, annotations.length);
					term.setComponents(array);
				}
				StringBuilder lemma = new StringBuilder();
				for (int index = 0; index < annotations.length; index++) {
					WordAnnotation word = (WordAnnotation) annotations[index];
					if (word.getBegin() < word.getEnd()) {
						
					} else {
						continue;
					}
					if (word.getLemma() == null) {
						word.setLemma(word.getCoveredText().toLowerCase());
					} else if (word.getLemma().equals("@card@")) {
						word.setLemma(word.getCoveredText().toLowerCase());
					}
					lemma.append(word.getLemma());
					if (index < annotations.length - 1) {
						lemma.append(' ');
					}
					// FIXME
					if (language.equals("lv")) {
						term.setComponents(index, word);						
					}
				}
				term.setLemma(lemma.toString());
				term.setCategory(id);
				term.addToIndexes();
			}
		}
	}
		
}
