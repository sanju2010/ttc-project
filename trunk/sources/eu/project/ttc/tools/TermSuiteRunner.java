package eu.project.ttc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingWorker;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineManagement;
import org.apache.uima.analysis_engine.JCasIterator;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.util.JCasPool;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;

public class TermSuiteRunner extends SwingWorker<Void, Void> {
	
	public static final int TXT = 0;
	
	public static final int URI = 1;
	
	public static final int XMI = -1;
	
	public static final String UTF8 = "utf-8";
	
	/**
	 * This attribute corresponds to the CAS pool build from the XMI file list
	 * of the remote resources. 
	 */
	private JCasPool pool;
	
	private AnalysisEngine analysisEngine;
	
	private void setAnalysisEngine() throws Exception {
		System.out.println("Initializing '" + this.description.getAnalysisEngineMetaData().getName() + "'");
		Runtime runtime = Runtime.getRuntime();
		int threads = runtime.availableProcessors();
	    this.analysisEngine = UIMAFramework.produceAnalysisEngine(this.description, threads, 0);
	    this.pool = new JCasPool(threads, this.analysisEngine);
	}
	
	private AnalysisEngineDescription description;
	
	private void setDescription(AnalysisEngineDescription description, ConfigurationParameterSettings settings) {
        description.getAnalysisEngineMetaData().setConfigurationParameterSettings(settings);
	    for (NameValuePair pair : settings.getParameterSettings()) {
	    	String name = pair.getName();
	    	Object value = pair.getValue();
	    	description.getAnalysisEngineMetaData().getConfigurationParameterSettings().setParameterValue(name, value);
	    }
	    this.description = description;
	}
	
	private Comparator<File> comparator;
	
	private FilenameFilter filter;
	
	private List<File> data;
	
	private void setData(int mode) {
		if (this.data == null) {
			this.data = new ArrayList<File>();
		}
		if (this.filter == null) {
			this.filter = new Filter(mode);
		}
		if (this.comparator == null) {
			this.comparator = new FileComparator();
		}
	}
	
	private void setData(String path, int mode) throws Exception {
		this.setData(mode);
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles(this.filter);
				for (File f : files) {
					this.data.add(f);
				}
			} else if (file.isFile() && this.filter.accept(file.getParentFile(), file.getName())) {
				this.data.add(file);
			}
		} else {
			throw new FileNotFoundException(path);
		}
		Collections.sort(this.data, this.comparator);
		System.out.println("Number of documents to process: " + this.data.size());
	}
	
	private class FileComparator implements Comparator<File> {

		@Override
		public int compare(File source, File target) {
			return source.getName().compareTo(target.getName());
		}
		
	}
	
	private class Filter implements FilenameFilter {
		
		private int input;
		
		public Filter(int input) {
			this.input = input;
		}
		
		@Override
		public boolean accept(File directory, String name) {
			File file = new File(directory, name);
			if (file.exists()) {
				if (file.isFile()) {
					try {
						URL url = file.toURI().toURL();
						if (this.input == TermSuiteRunner.TXT) {
							String type = url.openConnection().getContentType();
							return type.equals("text/plain");							
						} else if (this.input == TermSuiteRunner.XMI) {
							String type = url.openConnection().getContentType();
							return type.equals("application/xml") && name.endsWith(".xmi");							
						} else if (this.input == TermSuiteRunner.URI) {
							return true;							
						} else {
							return false;
						}
					} catch (Exception e) {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	private int input;
	
	private String language;
	
	private String encoding;
	
	@Override
	protected Void doInBackground() throws Exception {
		System.out.println("START");
		this.setAnalysisEngine();
		System.out.println("INITIALIZED");
		int max = this.data.size();
		this.setProgress(0);
        for (int index = 0; index < this.data.size(); index++) {
        	if (this.isCancelled()) {
				break;
			}
        	File file = this.data.get(index);
        	System.out.println("PROCESS " + file);
        	boolean last = index == this.data.size() - 1;
        	// this.publish(file);
        	this.process(file, this.encoding, this.language, this.input, last);
        	int progress = (index * 100) / max;
        	this.setProgress(progress);
        }
        this.analysisEngine.collectionProcessComplete();
    	this.setProgress(100);
        return null;
	}

	/*
	@Override
	public void process(List<File> files) {
		for (File file : files) {
			if (this.isCancelled()) {
				break;
			}
			UIMAFramework.getLogger().log(Level.INFO, "Processing " + file.getAbsolutePath());
		}
	}
	*/
	
	@Override
	public void done() {
		this.reset();
		if (this.isCancelled()) {
			return;
		}
		String message = this.display(this.analysisEngine.getAnalysisEngineMetaData(), this.analysisEngine.getManagementInterface(), 0);
		UIMAFramework.getLogger().log(Level.INFO, message);
	}
	
	private void process(File file, String encoding, String language, int mode, boolean last) throws Exception {
		JCas cas = this.pool.getJCas();
		try {
			String uri = file.toURI().toString();
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas);
			sdi.setUri(uri);
			if (mode == TermSuiteRunner.TXT) {
				String text = this.extract(file, encoding);
				cas.setDocumentLanguage(language);
				cas.setDocumentText(text);
				sdi.setBegin(0);
				sdi.setEnd(text.length());
				sdi.setOffsetInSource(0);
			} else if (mode == TermSuiteRunner.XMI) {
				InputStream inputStream = new FileInputStream(file);
				try {
					XmiCasDeserializer.deserialize(inputStream, cas.getCas(), true);
				} finally {
					inputStream.close();
				}
			} else {
				String mime = file.toURI().toURL().openConnection().getContentType();
				cas.setSofaDataURI(uri, mime);
			}
			sdi.setLastSegment(last);
			sdi.addToIndexes();
			if (this.analysisEngine.getAnalysisEngineMetaData().getOperationalProperties().getOutputsNewCASes()) {
				JCasIterator iterator = this.analysisEngine.processAndOutputNewCASes(cas);
				while (iterator.hasNext()) {
					JCas c = iterator.next();
					this.engine.callBack(c.getCas());
				}
				iterator.release();
			} else {
				this.analysisEngine.process(cas);
				this.engine.callBack(cas.getCas());
			}
		} finally {
			this.pool.releaseJCas(cas);
		}
	}
	
	private void reset() {
		this.termSuite.getToolBar().getRun().setEnabled(true);
		this.termSuite.getToolBar().getStop().setEnabled(false);
	}
	
	private String extract(File file, String encoding) throws Exception {
		StringBuilder builder = new StringBuilder();
		InputStream inputStream = new FileInputStream(file);
		Scanner scanner = new Scanner(inputStream, encoding);
		try {
			String delimiter = System.getProperty("line.separator");
			scanner.useDelimiter(delimiter);
			while (scanner.hasNext()) {
				String line = scanner.next();
				builder.append(line);
				builder.append(delimiter);
			}
			return builder.toString();
		} finally {
			scanner.close();
			inputStream.close();
		}
	}
	
	private void setDescription(String path, ConfigurationParameterSettings settings) throws Exception {
        URL url = this.getClass().getClassLoader().getResource(path);
        XMLInputSource in = new XMLInputSource(url.toURI().toString());
        ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
        if (specifier instanceof AnalysisEngineDescription) {
        	AnalysisEngineDescription description = (AnalysisEngineDescription) specifier;
        	this.setDescription(description, settings);
        } else {
        	throw new Exception("Wrong Analysis Engine Description: " + url.toExternalForm());
        }
	}
	
	private <md> String display(AnalysisEngineMetaData metadata, AnalysisEngineManagement managment, int level) {
		long time = managment.getAnalysisTime();
		String name = managment.getName();
		String perf = managment.getCASesPerSecond();
		long num = managment.getNumberOfCASesProcessed();
		StringBuilder display = new StringBuilder();
		if (level == 0) {
			display.append("\n\n");
		}
		for (int index = 0; index < level; index++) {
			display.append("    ");
		}
		display.append("Analysis Engine '" + name + "'\n");
		for (int index = 0; index < level; index++) {
			display.append("    ");
		}
		display.append("processes " + num + " documents in "  + time + " milli-seconds ("  + perf + " doc/s).\n\n");
		for (String key : managment.getComponents().keySet()) {
			AnalysisEngineManagement m = managment.getComponents().get(key);
			display.append(this.display(metadata, m, level + 1));
		} 
		return display.toString();
	}

	private TermSuite termSuite;
	
	private TermSuiteEngine engine;
	
	public TermSuiteRunner(TermSuite termSuite, TermSuiteEngine engine) throws Exception {
		this.termSuite = termSuite;
		this.engine = engine;
		this.input = this.engine.input();
		this.language = this.engine.language();
		this.encoding = this.engine.encoding();
        this.setDescription(this.engine.get(), this.engine.settings());
        this.setData(this.engine.data(), this.input);
	}
	
}
