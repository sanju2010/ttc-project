package eu.project.ttc.engines;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.flow.FinalStep;
import org.apache.uima.flow.Flow;
import org.apache.uima.flow.FlowControllerContext;
import org.apache.uima.flow.JCasFlowController_ImplBase;
import org.apache.uima.flow.JCasFlow_ImplBase;
import org.apache.uima.flow.SimpleStep;
import org.apache.uima.flow.Step;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.util.Level;

public class LanguageFlowController extends JCasFlowController_ImplBase {
	
	public LanguageFlowController() {
		this.setLanguageTable();
	}
	
	private Map<String,String> languageTable;
	
	private void setLanguageTable() {
		this.languageTable = new HashMap<String,String>();
	}
	
	public Map<String,String> getLanguageTable() {
		return this.languageTable;
	}
	
	private void addLanguage(String language,String id) {
		this.getLanguageTable().put(language,id);
		String msg = "Language mapping '" + language + " is associated with " + id;
		UIMAFramework.getLogger().log(Level.INFO,msg);
	}
	
	private void setLanguageTable(FlowControllerContext context) {
		Map<String,AnalysisEngineMetaData> map = context.getAnalysisEngineMetaDataMap();
	    Iterator<String> iter = map.keySet().iterator();
	    while (iter.hasNext()) {
	    	String id = (String) iter.next();
	    	AnalysisEngineMetaData md = map.get(id);
	    	Capability[] capabilities = md.getCapabilities();
	    	for (Capability capability : capabilities) {
	    		for (String language : capability.getLanguagesSupported()) {
	    			if (this.getAllowedLanguages().contains(language)) {
	    				this.addLanguage(language,id);
	    			} else {
	    				Locale locale = new Locale(language);
	    				UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + id + " for processing language " + locale.getDisplayLanguage(Locale.ENGLISH) + "(" + language + ")");
	    			}
	    		}
	    	}
	    }
	}
	
	public Map<String,String> getLanguages() { 
		return this.getLanguageTable(); 
	}
	
	private List<String> allowedLanguages;
	
	private void setAllowedLanguages(FlowControllerContext context) {
		this.allowedLanguages = new ArrayList<String>();
		Capability[] capabilities = context.getAggregateMetadata().getCapabilities();
		for (Capability capability : capabilities) {
			String[] languages = capability.getLanguagesSupported();
			for (String language : languages) {
				this.getAllowedLanguages().add(language);
			}
		}
	}
	
	private List<String> getAllowedLanguages() {
		return this.allowedLanguages;
	}
		
	@Override
	public void initialize(FlowControllerContext context) throws ResourceInitializationException {
	    super.initialize(context);
		this.setAllowedLanguages(context);
		this.setLanguageTable(context);
	}
	
	@Override
	public Flow computeFlow(JCas cas) throws AnalysisEngineProcessException {
		return new LanguageFlow();
	}

	private class LanguageFlow extends JCasFlow_ImplBase {
		
		boolean done = false;
		
		@Override
		public Step next() throws AnalysisEngineProcessException {
			if (this.done) {
				return new FinalStep();
			} else {
				this.done = true;
				JCas cas = this.getJCas();
				String lang = cas.getDocumentLanguage();
				String key = getLanguages().get(lang);
				if (key == null || key.isEmpty()) {
					String msg = "No engine found for processing language code " + lang + ".";
					UIMAFramework.getLogger().log(Level.WARNING,msg);
					return new FinalStep();
				} else {
					String msg = "Engine " + key + " for processing language code " + lang + ".";
					UIMAFramework.getLogger().log(Level.INFO,msg);
					return new SimpleStep(key);
				}
			}
		}
	
	}

}
