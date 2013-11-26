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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.resources.Dictionary;
import eu.project.ttc.types.TranslationCandidateAnnotation;

public class Builder extends JCasAnnotator_ImplBase {

	private Dictionary resource;
	
	private void setResource(Dictionary resource) {
		this.resource = resource;
	}
	
	private Dictionary getResource() {
		return this.resource;
	}
	
	private File file;
	
	private void setFile(String path) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) {
				this.file = file;	
			} else {
				throw new IOException("Not a file: " + path);
			}
		} else {
			this.file = file;
		}
	}
	
	private File getFile() {
		return this.file;
	}
	
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			Dictionary tabResource = (Dictionary) context.getResourceObject("Dictionary");
			this.setResource(tabResource);
			String path = (String) context.getConfigParameterValue("File");
			this.setFile(path);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TranslationCandidateAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			Map<Integer, TranslationCandidateAnnotation> annotations = new HashMap<Integer, TranslationCandidateAnnotation>(); 
			while (iterator.hasNext()) {
				TranslationCandidateAnnotation annotation = (TranslationCandidateAnnotation) iterator.next();
				annotations.put(annotation.getRank(), annotation);
			}
			for (Integer rank : annotations.keySet()) {
				TranslationCandidateAnnotation annotation = annotations.get(rank);
				String term = annotation.getCoveredText();
				String translation = annotation.getTranslation() + "\t(" + annotation.getScore() + ")";
				this.getResource().add(term, translation);
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			OutputStream stream = new FileOutputStream(this.getFile());
			try {
				Map<String, Set<String>> map = this.getResource().get();
				for (String source : map.keySet()) {
					for (String target : map.get(source)) {
						String line = source + "\t" + target + "\n";
						stream.write(line.getBytes());
					}
				}
			} finally {
				stream.close();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}