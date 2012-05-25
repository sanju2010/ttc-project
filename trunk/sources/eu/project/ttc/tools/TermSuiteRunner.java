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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;

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
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.util.JCasPool;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;

public class TermSuiteRunner extends SwingWorker<Void, Void> {

	public static void error(Throwable e, int code) {
        UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
        e.printStackTrace();
        System.exit(code);
	}

	public static void warning(String message) {
		UIMAFramework.getLogger().log(Level.WARNING, message);
	}

	public static void info(String message) {
		UIMAFramework.getLogger().log(Level.INFO, message);
	}
	
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
		// System.out.println("Initializing '" + this.description.getAnalysisEngineMetaData().getName() + "'");
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
			throw new FileNotFoundException("unable to find : "+path);
		}
		Collections.sort(this.data, this.comparator);
		// System.out.println("Number of documents to process: " + this.data.size());
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
		// System.out.println("START");
		try {
			this.setAnalysisEngine();
		} catch (Throwable e) {
		        e.printStackTrace();
			System.exit(1);
		}
		// System.out.println("INITIALIZED");
		int max = this.data.size();
		this.setProgress(0);
		for (int index = 0; index < this.data.size(); index++) {
			if (this.isCancelled()) {
				break;
			}
			File file = this.data.get(index);
			// System.out.println("PROCESS " + file);
			info("Process : " + file);
			boolean last = index == this.data.size() - 1;
			// this.publish(file);
			try {
				this.process(file, this.encoding, this.language, this.input, last);
			} catch (Throwable e) {
				TermSuiteRunner.warning(e.getMessage());
				e.printStackTrace();
				// System.exit(3);
			}
			int progress = (index * 100) / max;
			this.setProgress(progress);
		}
		try {
			this.analysisEngine.collectionProcessComplete();
		} catch (Throwable e) {
			System.exit(2);
		}
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
		info(message);//UIMAFramework.getLogger().log(Level.INFO, message);
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
					if (this.engine != null) { 
						this.engine.callBack(c.getCas());
					}
				}
				iterator.release();
			} else {
				this.analysisEngine.process(cas);
				if (this.engine != null) {
					this.engine.callBack(cas.getCas());
				}
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

	public TermSuiteRunner(AnalysisEngineDescription description, String directory,	int input, String language, String encoding) throws Exception {
		this.input = input;
		this.language = language;
		this.encoding = encoding;
		this.setDescription(description, description.getAnalysisEngineMetaData().getConfigurationParameterSettings());
		this.setData(directory, this.input);
	}

	private static String usage;
    
    static {
            usage = new String();
            usage += "You should use it as follows:\n";
            usage += "\t" + TermSuiteRunner.class.getCanonicalName();
            usage += " [-txt|-uri|-xmi]?";
            usage += " -encoding <encoding>";
            usage += " -language <code>";
            usage += " -directory <path>"; 
            usage += " -analysis-engine <name>";
            usage += " [--<name> <value>]*";
    }
	
	public static void main(String[] arguments) {
		try {
			Integer input = null;
			String encoding = null;
			String language = null;
			String analysisEngine = null;
			String directory = null;
			Map<String, String> parameters = new HashMap<String, String>();
			for (int index = 0; index < arguments.length - 1; index++) {
				if (arguments[index].equals("-txt")) {
					input = TermSuiteRunner.parse(TermSuiteRunner.TXT, input == null);
				} else if (arguments[index].equals("-uri")) {
					input = TermSuiteRunner.parse(TermSuiteRunner.URI, input == null);
				} else if (arguments[index].equals("-xmi")) {
					input = TermSuiteRunner.parse(TermSuiteRunner.XMI, input == null);
				} else if (arguments[index].equals("-analysis-engine")) {
					analysisEngine = TermSuiteRunner.parse(arguments, index, analysisEngine == null, "-analysis-engine");
					index++;                                                
				} else if (arguments[index].equals("-encoding")) {
					encoding = TermSuiteRunner.parse(arguments, index, encoding == null, "-encoding");
					index++;                                                
				} else if (arguments[index].equals("-language")) {
					language = TermSuiteRunner.parse(arguments, index, language == null, "-language");
					index++;                                                
				} else if (arguments[index].equals("-directory")) {
					directory = TermSuiteRunner.parse(arguments, index, directory == null, "-directory");
					index++;                                                
				} else if (arguments[index].startsWith("--") && arguments[index].length() > 2) {
					TermSuiteRunner.parse(arguments, index, parameters);
					index++;
				} else {
					throw new Exception("I don't know what to do with this option: '" + arguments[index] + "'\n" + usage);
				}
			}
			TermSuiteRunner.process(analysisEngine, parameters, input, directory, language, encoding);
		} catch (Exception e) {
			TermSuiteRunner.error(e, 1);
		}
	}

	private static Integer parse(int value, boolean allowed) throws Exception {
		if (allowed) {
			return new Integer(value);                                              
		} else {
			throw new Exception("I've got too much options -txt or -uri or -xmi\n" + usage);
		}
	}

	private static void process(String engine, Map<String, String> parameters, Integer input, String directory, String language, String encoding) 
			throws Exception {
		encoding = encoding == null ? UTF8 : encoding;
		if (input == null) {
			throw new Exception("No option -text | -uri | -xmi\n" + usage);
		} else if (engine == null) {
			throw new Exception("No option -analysis-engine\n" + usage);
		} else if (input.intValue() == TermSuiteRunner.TXT && language == null) {
			throw new Exception("No option -language\n" + usage);
		} else if (directory == null) {
			throw new Exception("No option -directory\n" + usage);
		} else {
			AnalysisEngineDescription description = TermSuiteRunner.description(engine, parameters);
			TermSuiteRunner runner = new TermSuiteRunner(description, directory, input, language, encoding);
			runner.execute();
			if (!SwingUtilities.isEventDispatchThread())
			    runner.get();
		}
	}

	private static AnalysisEngineDescription description(String path, Map<String, String> options) throws Exception {
		URL url = TermSuiteRunner.class.getClassLoader().getResource(path.replaceAll("\\.", "/") + ".xml");
		XMLInputSource in = new XMLInputSource(url.toURI().toString());
		ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
		if (specifier instanceof AnalysisEngineDescription) {
			AnalysisEngineDescription description = (AnalysisEngineDescription) specifier;
			TermSuiteRunner.configure(description.getAnalysisEngineMetaData(), options);
			return description;
		} else {
			throw new Exception("Wrong XML Analysis Engine Descriptor: " + url.toExternalForm());
		}
	}

	private static void configure(AnalysisEngineMetaData metadata, Map<String, String> options) {
		ConfigurationParameterDeclarations declarations = metadata.getConfigurationParameterDeclarations();
		ConfigurationParameterSettings settings = metadata.getConfigurationParameterSettings();
		for (String option : options.keySet()) {
			String value = options.get(option);
			ConfigurationParameter declaration = declarations.getConfigurationParameter(null, option);
			if (declaration != null) { 
				String type = declaration.getType();
				// TODO boolean multiValued = declaration.isMultiValued();
				if (type.equals(ConfigurationParameter.TYPE_STRING)) {
					settings.setParameterValue(option, value);
				} else if (type.equals(ConfigurationParameter.TYPE_INTEGER)) {
					settings.setParameterValue(option, Integer.valueOf(value));
				} else if (type.equals(ConfigurationParameter.TYPE_FLOAT)) {
					settings.setParameterValue(option, Float.valueOf(value));
				} else if (type.equals(ConfigurationParameter.TYPE_BOOLEAN)) {
					settings.setParameterValue(option, Boolean.valueOf(value));
				} 
			}
		}
	}

	private static String parse(String[] arguments, int index, boolean allowed, String name) throws Exception {
		if (allowed) {
			return arguments[index + 1];
		} else {
			throw new Exception("Option " + name + " already set\n" + usage);
		}
	}

	private static void parse(String[] arguments, int index, Map<String, String> options) {
		String key = arguments[index].substring(2);
		String value = arguments[index + 1];
		if (value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(1, value.length() - 1);
		} else if (value.startsWith("'") && value.endsWith("'")) {
			value = value.substring(1, value.length() - 1);
		}
		options.put(key, value);
	}

}
