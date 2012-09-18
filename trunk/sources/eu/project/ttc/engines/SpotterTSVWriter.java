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
import java.io.OutputStreamWriter;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import eu.project.ttc.types.TermAnnotation;

/**
 * Writes spotter cases as TSV.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class SpotterTSVWriter extends Writer {

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		String name = retrieve(cas);
		if (name == null) {
			this.getContext().getLogger()
					.log(Level.WARNING, "Skiping CAS Serialization");
			return;
		}
		try {
			File file = new File(getDirectory(), name);
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(file), "utf-8");
			try {
				this.getContext().getLogger()
						.log(Level.INFO, "Writing " + file.getAbsolutePath());
				AnnotationIndex<Annotation> index = cas
						.getAnnotationIndex(TermAnnotation.type);
				TermAnnotation term;
				for (Annotation annot : index) {
					term = (TermAnnotation) annot;
					out.append(term.getCoveredText()).append('\t');
					out.append(term.getCategory()).append('\t');
					out.append(term.getLemma()).append('\n');
				}
			} finally {
				out.close();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	@Override
	protected String retrieve(JCas cas) {
		String name = super.retrieve(cas);
		return name == null ? null : name.replace(".xmi", ".tsv");
	}
}
