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
package eu.project.ttc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.JCasIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.JCasPool;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;

import eu.project.ttc.tools.commons.InputSource.InputSourceTypes;
import eu.project.ttc.tools.commons.InvalidTermSuiteConfiguration;
import eu.project.ttc.tools.commons.ToolController;


public class TermSuiteRunner extends SwingWorker<Void, Void> {

    private final ToolController tool;
    private boolean inError;
    private Throwable lastError;

    public void error(Throwable e, int code) {
        UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
        e.printStackTrace();
        //System.exit(code);
        setError(true, e);

        firePropertyChange("fatalerror", null, e);
	}

    private void setError(boolean b, Throwable e) {
        inError = b;
        lastError = e;
    }

    private boolean isInError() {
        return inError;
    }
	
	/**
	 * This attribute corresponds to the CAS pool build from the XMI file list
	 * of the remote resources. 
	 */
	private JCasPool pool;

	private AnalysisEngine analysisEngine;

	private AnalysisEngineDescription description;
	
	private ArrayList<File> data = new ArrayList<File>();
	
	private InputSourceTypes inputSourceTypes;

	private String language;

	private String encoding;
	
	private TermSuite termSuite;

	/**
	 * Creates a new runner for the specified <code>tool</code>
	 * @param tool
	 * @throws InvalidTermSuiteConfiguration
	 * @throws FileNotFoundException
	 */
    public TermSuiteRunner(ToolController tool) throws InvalidTermSuiteConfiguration, FileNotFoundException {
        this.tool = tool;
        this.inputSourceTypes = tool.getInputSource().getType();
        this.language = tool.getLanguage();
        this.encoding = tool.getEncoding();
        try {
            String engineDescriptor = tool.getAEDescriptor();
            UIMAFramework.getLogger().log(Level.INFO, "Ready to run: " + engineDescriptor);
            setDescription(description(engineDescriptor), tool.getAESettings());
        } catch (ResourceConfigurationException e) {
            throw new InvalidTermSuiteConfiguration(
                    "Unable to validate the configuration then unable to launch the processing.", e);
        }
        this.data = new ArrayList<File>( tool.getInputSource().getInputFiles() );
    }
    
	private void initAnalysisEngine() throws ResourceInitializationException {
		if (analysisEngine != null)
		    analysisEngine.destroy();
	    int threads = Runtime.getRuntime().availableProcessors();
		this.analysisEngine = UIMAFramework.produceAnalysisEngine(this.description, threads, 0);
		this.pool = new JCasPool(threads, this.analysisEngine);
	}

	private void setDescription(AnalysisEngineDescription description, ConfigurationParameterSettings settings) {
		description.getAnalysisEngineMetaData().setConfigurationParameterSettings(settings);
		for (NameValuePair pair : settings.getParameterSettings()) {
			String name = pair.getName();
			Object value = pair.getValue();
			description.getAnalysisEngineMetaData().getConfigurationParameterSettings().setParameterValue(name, value);
		}
		this.description = description;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// System.out.println("START");
		try {
			this.initAnalysisEngine();
            // System.out.println("INITIALIZED");
            int max = this.data.size();
            this.setProgress(0);
            for (int index = 0; index < this.data.size(); index++) {
                if (this.isCancelled()) {
                    break;
                }
                File file = this.data.get(index);
                UIMAFramework.getLogger().log(Level.INFO, "Processing file '" + file + "'.");
                boolean last = index == this.data.size() - 1;
                // this.publish(file);
                try {
                    this.process(file, this.encoding, this.language, this.inputSourceTypes, last);
                } catch (Throwable e) {
                    UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
                    e.printStackTrace();
                    // FIXME use CPE capabilities to handle errors and report them
                    // System.exit(3);
                }
                int progress = (index * 100) / max;
                this.setProgress(progress);
            }
			this.analysisEngine.collectionProcessComplete();
            this.setProgress(100);
            return null;
		} catch (Throwable e) {
		    UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            termSuite.displayException("An error occurred while running the analysis.", e);
			//System.exit(2);
            return null;
		}
	}

	@Override
	public void done() {
		this.reset(); // FIXME shouldn't we reset error and cancel flags ?
        if (isInError()) {
            firePropertyChange("inerror", null, lastError);
            return;
        } else if (isCancelled()) {
            firePropertyChange("cancelled", false, true);
			return;
		}
		String message = TermSuiteCLIRunner.display(this.analysisEngine.getAnalysisEngineMetaData(), this.analysisEngine.getManagementInterface(), 0);
        UIMAFramework.getLogger().log(Level.INFO, message);
        firePropertyChange("done", false, true);
	}

    private void process(File file, String encoding, String language, InputSourceTypes mode, boolean last) throws Exception {

        // FIXME quick hack to dispose language
        language = (String) this.analysisEngine.getAnalysisEngineMetaData()
                .getConfigurationParameterSettings().getParameterValue("Language");

		JCas cas = this.pool.getJCas();
		try {
			String uri = file.toURI().toString();
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas);
			sdi.setUri(uri);
			
			switch (mode) {
			case TXT:
				String text = FileUtils.readFileToString(file, encoding);
				cas.setDocumentLanguage(language);
				cas.setDocumentText(text);
				sdi.setBegin(0);
				sdi.setEnd(text.length());
				sdi.setOffsetInSource(0);
				break;
			case XMI:
				InputStream inputStream = new FileInputStream(file);
				try {
					XmiCasDeserializer.deserialize(inputStream, cas.getCas(),
							true);
				} finally {
					inputStream.close();
				}
				break;
			case URI:
				String mime = file.toURI().toURL().openConnection()
						.getContentType();
				cas.setSofaDataURI(uri, mime);
				break;
			}

			sdi.setLastSegment(last);
			sdi.addToIndexes();
			if (this.analysisEngine.getAnalysisEngineMetaData().getOperationalProperties().getOutputsNewCASes()) {
				JCasIterator iterator = this.analysisEngine.processAndOutputNewCASes(cas);
				while (iterator.hasNext()) {
					JCas c = iterator.next();
                    if (this.tool != null )
                        this.tool.processingCallback(c.getCas());
				}
				iterator.release();
			} else {
				this.analysisEngine.process(cas);
                if (this.tool != null )
                    this.tool.processingCallback(cas.getCas());
			}
		} finally {
			this.pool.releaseJCas(cas);
		}
	}

	private void reset() {
		if (this.termSuite == null) {
			return;
		} else {
			this.termSuite.getToolBar().getRun().setEnabled(true);
			this.termSuite.getToolBar().getStop().setEnabled(false);
		}
	}

    /**
     * @param resource  descriptor resource
     * @return  an analysis engine description without any specified configuration
     * @throws Exception
     */
    private static AnalysisEngineDescription description(String resource) throws InvalidTermSuiteConfiguration {
        try {
            URL url = TermSuiteRunner.class.getClassLoader().getResource(resource.replaceAll("\\.", "/") + ".xml");
            UIMAFramework.getLogger().log(Level.INFO, "resource specifier :" + url.toString());
            XMLInputSource in = new XMLInputSource(url.toURI().toString());
            ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
            if (specifier instanceof AnalysisEngineDescription) {
                return (AnalysisEngineDescription) specifier;
            } else {
                throw new RuntimeException("Wrong XML Analysis Engine Descriptor: " + url.toExternalForm());
            }
        } catch (URISyntaxException e) {
            throw new InvalidTermSuiteConfiguration("Unable to access the tool descriptor.", e);
        } catch (IOException e) {
            throw new InvalidTermSuiteConfiguration("Unable to access the tool descriptor.", e);
        } catch (InvalidXMLException e) {
            throw new InvalidTermSuiteConfiguration("Unable to parse the tool descriptor.", e);
        }
    }

    public Throwable getLastError() {
        return lastError;
    }
}
