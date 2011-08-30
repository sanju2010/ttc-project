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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.flow.FinalStep;
import org.apache.uima.flow.Flow;
import org.apache.uima.flow.FlowControllerContext;
import org.apache.uima.flow.JCasFlowController_ImplBase;
import org.apache.uima.flow.JCasFlow_ImplBase;
import org.apache.uima.flow.ParallelStep;
import org.apache.uima.flow.Step;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.util.Level;

public class AnnotationTypeFlowController extends JCasFlowController_ImplBase {

	private Map<String,String> annotationTypes;
	
	private void setAnnotationTypes() {
		this.annotationTypes = new HashMap<String, String>();
	}
	
	private Map<String,String> getAnnotationTypes() {
		return this.annotationTypes;
	}
	
	private void addAnnotationType(String type,String id) {
		String annotationType = null;
		String[] elements = type.split(":");
		if (elements.length == 2) {
			annotationType = elements[0];
		} else {
			annotationType = type;
		}
		String previous = this.getAnnotationTypes().put(annotationType,id);
		if (previous != null) {
			String msg = "Tagger Annotation mapping '" + type + "' previously defined with " + previous;
			msg += " is redefined with " + id;
			UIMAFramework.getLogger().log(Level.INFO,msg);
		}
	}

	@Override
	public void initialize(FlowControllerContext context) throws ResourceInitializationException {
		this.setAnnotationTypes();
		Map<String,AnalysisEngineMetaData> map = context.getAnalysisEngineMetaDataMap();
	    Iterator<String> iter = map.keySet().iterator();
	    while (iter.hasNext()) {
	    	String id = (String) iter.next();
	    	AnalysisEngineMetaData md = map.get(id);
	    	ConfigurationParameterSettings config = md.getConfigurationParameterSettings();
	    	String annotation = (String) config.getParameterValue("AnnotationType");
	    	this.addAnnotationType(annotation,id);
	    }
	}
	
	@Override
	public Flow computeFlow(JCas cas) throws AnalysisEngineProcessException {
		return new AnnotationTypeFlow();
	}
	
	private class AnnotationTypeFlow extends JCasFlow_ImplBase {
		
		boolean done = false;
		
		@Override
		public Step next() throws AnalysisEngineProcessException {
			if (this.done) { 
				return new FinalStep(); 
			} else {
				this.done = true;
				JCas cas = this.getJCas();
				Set<String> steps = new HashSet<String>();
				for (String annotation : getAnnotationTypes().keySet()) {
					Type type = cas.getTypeSystem().getType(annotation);
					AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
					if (index.size() > 0) {
						String id = getAnnotationTypes().get(annotation);
						steps.add(id);
					}
				}
				return new ParallelStep(steps);
			}
		}
	
	}

}
