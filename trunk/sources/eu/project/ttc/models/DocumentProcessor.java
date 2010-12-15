package eu.project.ttc.models;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.TokenAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import fr.univnantes.lina.uima.engines.Annotator;

/**
 */
public class DocumentProcessor extends Annotator {

	private List<List<String>> elements;
	
	private void setElements() {
		this.elements = new ArrayList<List<String>>();	
	}
	
	private List<List<String>> getElements() {
		return this.elements;
	}
	
	private String commandLine;

	private void setCommandLine(String commandLine) throws ResourceInitializationException {
		this.commandLine = commandLine;
	}

	private String getCommandLine(String file) {
		return this.commandLine + " " + file;
	}

	/**  
	 */
	@Override
	public void doInitialize() throws ResourceInitializationException {
		this.setElements();
		this.setCommandLine((String) this.getParameter("CommandLine"));
	}

	/** 
	 */
	@Override
	public void doProcess(JCas cas) throws AnalysisEngineProcessException {
		try {
			String input = this.doWrite(cas.getDocumentText());
			String output = this.doLaunch(this.getCommandLine(input));
			String text = this.doRead(output);
			this.doBuild(text);
			this.doValidate(cas);
		} catch (Exception e) { 
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private String doRead(String file) throws Exception {
		String text = "";
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		String line = null;
		while ((line = buffer.readLine()) != null) {
			text += line + "\n";
		}
		buffer.close();
		return text;
	}
	
	private String doWrite(String text) throws Exception {
		File file = File.createTempFile("document-",".txt");
		FileOutputStream stream = new FileOutputStream(file);
		Writer writer = new OutputStreamWriter(stream);
		writer.write(text);
		writer.close();
		return file.getAbsolutePath();
	}
	
	private String doLaunch(String command) throws Exception {
		File file = File.createTempFile("document-result-",".txt");
		String cmd = command + " " + file;
		Process proc = Runtime.getRuntime().exec(cmd);
		if (proc.waitFor() != 0) {
			throw new Exception("Failure while processing '" + cmd + "'") ;
		}
		proc.destroy();
		return file.getAbsolutePath();
	}

	@Override
	public void doFinalize() { }


	private void doBuild(String text) {
		this.getElements().clear();
		String[] tokens = text.split("\n");
		for (String token : tokens) {
			String[] elements = token.split("\t");
			this.addElement(elements);
		}
	}
	
	private void doValidate(JCas cas) throws Exception {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TokenAnnotation.type);
		FSIterator<Annotation> iter = index.iterator();
		int i = 0;
		while(iter.hasNext()) {
			TokenAnnotation annotation = (TokenAnnotation) iter.next();
			int begin = annotation.getBegin();
			int end = annotation.getEnd();
			if (this.doCheck(annotation,i)) {
				this.doAnnotate(cas,i,begin,end);	
			}
			i++;
		}
	}
	
	private void addElement(String[] elements) {
		List<String> element = new ArrayList<String>();
		for (String e : elements) {
			element.add(e);
		}
		this.getElements().add(element);
	}
	
	private String getToken(int index) {
		return this.getElements().get(index).get(0);
	}
	
	private String getTag(int index) {
		return this.getElements().get(index).get(1);
	}
	
	private String getLemma(int index) {
		return this.getElements().get(index).get(2);
	}
	
	private String getMultext(int index) {
		return this.getElements().get(index).get(3);
	}
	
	private void doAnnotate(JCas cas,int index,int begin,int end) {
		
	}
	
	private boolean doCheck(Annotation annotation,int index) throws Exception {
		String first = annotation.getCoveredText();
		String second = this.getToken(index);
		if (first.equals(second)) {
			return true;
		} else {
			String msg =  "Alignment failure between ";
			msg += first + " and " + second;
			this.getLogger().log(Level.SEVERE,msg);
			throw new Exception(msg);
		}
	}

	
}
