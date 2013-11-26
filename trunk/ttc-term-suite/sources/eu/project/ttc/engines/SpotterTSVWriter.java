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
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.WordAnnotation;

/**
 * Writes spotter cases as TSV.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class SpotterTSVWriter extends Writer {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"yyyyMMdd-HHmm");

	/** Parameter that must be set to enable this analysis engine, if not set, the engine will not output TSV files */
    private static final String P_IS_ENABLED = "isEnabled";

	private boolean isEnabled;
	
	@Override
	public void initialize(UimaContext context)
	        throws ResourceInitializationException {
	    
	    isEnabled = Boolean.TRUE.equals(context.getConfigParameterValue(P_IS_ENABLED));

	    if (isEnabled)
	        super.initialize(context);
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	    
	    if (!isEnabled)
	        return;
	    
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
						.getAnnotationIndex(WordAnnotation.type);
				WordAnnotation word;
				for (Annotation annot : index) {
					word = (WordAnnotation) annot;
					out.append(word.getCoveredText()).append('\t');
					out.append(word.getCategory()).append('\t');
					out.append(word.getLemma()).append('\n');
				}
			} finally {
				out.close();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	@Override
	protected void setDirectory(String path) throws IOException {
		String dir = "tsv_" + DATE_FORMATTER.format(new Date());
		if (path.contains("xmi")) {
			path = path.replace("xmi", dir);
		} else {
			path = path + File.pathSeparator + dir;
		}
		super.setDirectory(path);
	}

	@Override
	protected String retrieve(JCas cas) {
		String name = super.retrieve(cas);
		return name == null ? null : name.replace(".xmi", ".tsv");
	}
}
