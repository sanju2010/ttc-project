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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.models.WrapperConfiguration;

public class Wrapper extends JCasAnnotator_ImplBase {

	private WrapperConfiguration configuration;
	
	private void setConfiguration(WrapperConfiguration configuration) {
		this.configuration = configuration;
	}

	private WrapperConfiguration getConfiguration() {
		return this.configuration;
	}

	private void doLaunch(String commandLine) throws IOException, InterruptedException {
		this.getContext().getLogger().log(Level.INFO,"Processing '" + commandLine + "'");
		Process proc = Runtime.getRuntime().exec(commandLine);
		if (proc.waitFor() != 0) {
			throw new IOException("Failure while processing '" + commandLine + "'") ;
		}
		proc.destroy();
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			WrapperConfiguration configuration = (WrapperConfiguration) context.getResourceObject("WrapperConfiguration");
			this.setConfiguration(configuration);
			String fileName = (String) context.getConfigParameterValue("WrapperConfigurationFile");
			if (fileName != null) {
				this.getConfiguration().doLoad(fileName);				
			}
			if (this.getConfiguration().getPreProcess() != null) {
				this.doLaunch(this.getConfiguration().getPreProcess());
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		} 
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			File inputFile = File.createTempFile("uima-input-",".txt");
			OutputStream outputStream = new FileOutputStream(inputFile);
			this.getConfiguration().getHandler().doEncode(cas,outputStream);
			outputStream.close();
			File outputFile = File.createTempFile("uima-output-",".txt");
			
			this.doLaunch(this.getConfiguration().getProcess(cas.getDocumentLanguage(),inputFile.getAbsolutePath(),outputFile.getAbsolutePath()));

			InputStream inputStream = new FileInputStream(outputFile);
			this.getConfiguration().getHandler().doDecode(cas,inputStream);
			inputStream.close();
		} catch (Exception e) { 
			throw new AnalysisEngineProcessException(e);
		}
	}

	@Override
	public void collectionProcessComplete() { 
		try {
			if (this.getConfiguration().getPostProcess() != null) {
				this.doLaunch(this.getConfiguration().getPostProcess());
			}
		} catch (Exception e) {
			UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		} 
	}
	
}

