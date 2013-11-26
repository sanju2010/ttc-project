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

import eu.project.ttc.types.TermAnnotation;

public class Contextualizer extends uima.sandbox.contextualizer.engines.Contextualizer {

	@Override
	protected void annotate(JCas cas, Annotation annotation, Annotation[] annotations) {
		// System.out.println(annotation.getCoveredText());
		if (annotation instanceof TermAnnotation) {
			TermAnnotation term = (TermAnnotation) annotation;
			int length = annotations.length;
			FSArray array = new FSArray(cas,length);
			term.setContext(array);
			for (int index = 0; index < length; index ++) {
				TermAnnotation coTerm = (TermAnnotation) annotations[index];
				term.setContext(index, coTerm);
			}
		}
	}

}
