package eu.project.ttc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import eu.project.ttc.tools.commons.TermSuiteEngine;
import eu.project.ttc.tools.commons.ToolController;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
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


import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.OptionBuilder;
import eu.project.ttc.tools.utils.FileComparator;
import eu.project.ttc.tools.utils.InputSourceFilter;
import eu.project.ttc.tools.commons.InputSource.InputSourceTypes;


public class TermSuiteRunner extends SwingWorker<Void, Void> {

    private ToolController tool; // FIXME final ?

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
	
	/**
	 * This attribute corresponds to the CAS pool build from the XMI file list
	 * of the remote resources. 
	 */
	private JCasPool pool;

	private AnalysisEngine analysisEngine;

	private AnalysisEngineDescription description;
	
	private ArrayList<File> data = new ArrayList<File>();
	
	private InputSourceTypes input;

	private String language;

	private String encoding;
	
	private void setAnalysisEngine() throws Exception {
		// System.out.println("Initializing '" + this.description.getAnalysisEngineMetaData().getName() + "'");
		Runtime runtime = Runtime.getRuntime();
		int threads = runtime.availableProcessors();
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

//	private void setData(String path, InputSourceTypes mode) throws Exception {
//		InputSourceFilter filter = new InputSourceFilter(mode);
//		File file = new File(path);
//		if (file.isDirectory()) {
//			File[] files = file.listFiles(filter);
//			for (File f : files) {
//				this.data.add(f);
//			}
//		} else if (file.isFile()
//				&& filter.accept(file.getParentFile(), file.getName())) {
//			this.data.add(file);
//		} else {
//			throw new FileNotFoundException(path
//					+ " is not a directory, or it cannot be found.");
//		}
//		Collections.sort(this.data, new FileComparator());
//		// System.out.println("Number of documents to process: " + this.data.size());
//	}

	@Override
	protected Void doInBackground() throws Exception {
		// System.out.println("START");
		try {
			this.setAnalysisEngine();
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
			this.analysisEngine.collectionProcessComplete();
            this.setProgress(100);
            return null;
		} catch (Throwable e) {
            System.out.print(e.toString());
            e.printStackTrace();
            termSuite.displayException("An error occurred while running the analysis.", e);
			//System.exit(2);
            return null;
		}
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
//					if (this.engine != null) {
//						this.engine.callBack(c.getCas());
//					}
				}
				iterator.release();
			} else {
				this.analysisEngine.process(cas);
                if (this.tool != null )
                    this.tool.processingCallback(cas.getCas());
//				if (this.engine != null) {
//					this.engine.callBack(cas.getCas());
//				}
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

    @Deprecated
	public TermSuiteRunner(TermSuite termSuite, TermSuiteEngine engine) throws Exception {
		this.termSuite = termSuite;
		this.engine = engine;
//		this.input = this.engine.input();
//		this.language = this.engine.language();
//		this.encoding = this.engine.encoding();
//		this.setDescription(this.engine.getEngineDescriptor(), this.engine.getAESettings());
//		this.setData(this.engine.getInputSource(), this.input);
	}

    @Deprecated
	public TermSuiteRunner(AnalysisEngineDescription description, String directory,	InputSourceTypes input, String language, String encoding) throws Exception {
		this.input = input;
		this.language = language;
		this.encoding = encoding;
		this.setDescription(description, description.getAnalysisEngineMetaData().getConfigurationParameterSettings());
//		this.setData(directory, this.input);
	}

    public TermSuiteRunner(ToolController tool) throws Exception {
        this.tool = tool;
        this.input = tool.getInputSourceType();
        this.language = tool.getLanguage();
        this.encoding = tool.getEncoding();
        setDescription(description(tool.getAEDescriptor()), tool.getAESettings());
        this.data = new ArrayList<File>( tool.getInputSource().getInputFiles() );
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] arguments) {
		try {
			InputSourceTypes inputType = null;

			Properties myProperties = new Properties();
			
			// create the command line parser
			CommandLineParser parser = new PosixParser();
			
			// create the Options
			Options options = new Options();
			//Option op = new Option()
			OptionGroup optionGroup = new OptionGroup( ); 
			OptionBuilder.hasArg(false);
			optionGroup.addOption( OptionBuilder.create("txt") );
			optionGroup.addOption( OptionBuilder.create("xmi") );
			optionGroup.addOption( OptionBuilder.create("url") );
			optionGroup.isRequired();
			options.addOptionGroup( optionGroup );
			
			options.addOption( "directory", "", true, "input directory" );
			options.addOption( "analysisEngine", "analysis-engine", true, "analysis engine" );
			options.addOption( "language", "", true, "language" );
			options.addOption( "encoding", "", true, "encoding" );

			
			String engineName = null;
			int i = 0;
				// parse the command line arguments
			while ((i<arguments.length) && (!arguments[i].equalsIgnoreCase("-analysisEngine")) && (!arguments[i].equalsIgnoreCase("--analysis-engine"))) {
				System.out.println("argument[" + i + "]=" + arguments[i]);
				i++;
			}
			
			if (i<arguments.length) {		
				engineName= arguments[i+1];
				System.out.println(engineName);				
				if(engineName.contains("SpotterController")) {
					// SpotterController engine
					System.out.println("spotter : " + engineName);
					options.addOption( "", "Directory", true, "output directory" );
					options.addOption( "", "TreeTaggerHomeDirectory", true, "TreeTagger home directory" );
				} else if (engineName.contains("Indexer")) {
					// Indexer engine
					System.out.println("indexer : " + engineName);
					options.addOption( "", "Directory", true, "output directory" );
					options.addOption( "", "EnableTermGathering", true, "enable term gathering" );
					options.addOption( "", "EditDistanceClassName", true, "edit distance classname" );
					options.addOption( "", "EditDistanceThreshold", true, "edit distance threshold" );
					options.addOption( "", "EditDistanceNgrams", true, "edit distance ngrams" );
					options.addOption( "", "IgnoreDiacriticsInMultiwordTerms", true, "ignore diacritics in multiword terms" );
					options.addOption( "", "OccurrenceThreshold", true, "occurence threshold" );
					options.addOption( "", "AssociationRateClassName", true, "association rate class name" );
					options.addOption( "", "FilterRuleThreshold", true, "filter rule threshold" ); 
					options.addOption( "", "FilterRule", true, "filter rule" ); 
					options.addOption( "", "KeepVerbsAndOthers", true, "keep verbs and other" );								
				} else if (engineName.contains("Aligner")) {
					// Aligner engine
					System.out.println("aligner : " + engineName);
					options.addOption( "", "Directory", true, "output directory" );
					options.addOption( "", "SourceLanguage", true, "source language" );
					options.addOption( "", "TargetLanguage", true, "target language" );
					options.addOption( "", "SourceTerminologyFile", true, "source terminology file" );
					options.addOption( "", "TargetTerminologyFile", true, "target terminology file" );
					options.addOption( "", "DictionaryFile", true, "dictionnary file" );
					options.addOption( "", "AlignerOutputDirectory", true, "aligner output directory" );
					options.addOption( "", "CompositionalMethod", true, "compositional method" );
					options.addOption( "", "DistributionalMethod", true, "distributional method" ); 
					options.addOption( "", "SimilarityDistanceClassName", true, "similarity distance classname" ); 
					options.addOption( "", "MaxTranslationCandidates", true, "maximum number of translation candidates" );								
				}	

				String propertiesFileName = engineName.substring(engineName.lastIndexOf(".")+1).concat(".properties");

				try {
					//doSave properties to project root folder
		    		myProperties.load(new FileInputStream(propertiesFileName));
				} catch (IOException ex) {
		    		info("unable to find the properties file name" + propertiesFileName);			
				}

								
				try {
					CommandLine line = parser.parse( options, arguments, false);
					if (line.hasOption("txt")) inputType = InputSourceTypes.TXT;
					else if (line.hasOption("uri")) inputType = InputSourceTypes.URI;
					else if (line.hasOption("xmi")) inputType = InputSourceTypes.XMI;

					
					for( Option myOption : line.getOptions() ) {
						if (myOption.hasArg()) {
							System.out.println(myOption.getOpt() + " : " + myOption.getLongOpt() + " : " + myOption.getValue());
							if (!(myOption.getOpt().isEmpty())) {
								//parameters.put(myOption.getOpt(),myOption.getValue());
								myProperties.setProperty(myOption.getOpt(),myOption.getValue());
							}
							if (!(myOption.getLongOpt().isEmpty())) {
								//parameters.put(myOption.getLongOpt(),myOption.getValue());
								myProperties.setProperty(myOption.getLongOpt(),myOption.getValue());
							}
						}	
					}
										
					try {
						//doSave properties to project root folder
			    		myProperties.store(new FileOutputStream(propertiesFileName), null);
					} catch (IOException ex) {
			    		ex.printStackTrace();			
					}
					
					TermSuiteRunner.process(engineName, new HashMap<String, String>((Map) myProperties), inputType, 
							myProperties.getProperty("directory"), 
							myProperties.getProperty("language"), 
							myProperties.getProperty("encoding"));

				} catch (ParseException e) {
					// automatically generate the various statement
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp( "java -Xms1g  -Xmx2g -cp target/ttc-term-suite-1.3.jar eu.project.ttc.tools.TermSuiteRunner", options );					
				}
				
			} else {
				// automatically generate the various statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "TermSuiteRunner", options );
			}
			
		} catch (Exception e) {
			TermSuiteRunner.error(e, 1);
		}
	}

	private static void process(String engine, Map<String, String> parameters, InputSourceTypes input, String directory, String language, String encoding) 
			throws Exception {
		encoding = encoding == null ? TermSuiteEngine.DEFAULT_ENCODING : encoding;
		if (input == null) {
			throw new Exception("No option -txt | -uri | -xmi\n" + usage);
		} else if (engine == null) {
			throw new Exception("No option -analysis-engine\n" + usage);
		} else if (input == InputSourceTypes.TXT && language == null) {
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

    /**
     * @param resource  descriptor resource
     * @return  an analysis engine description without any specified configuration
     * @throws Exception
     */
    private static AnalysisEngineDescription description(String resource) throws Exception {
        URL url = TermSuiteRunner.class.getClassLoader().getResource(resource.replaceAll("\\.", "/") + ".xml");
        System.out.println("resource specifier :" + url.toString());
        XMLInputSource in = new XMLInputSource(url.toURI().toString());
        ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
        if (specifier instanceof AnalysisEngineDescription) {
            return (AnalysisEngineDescription) specifier;
        } else {
            throw new Exception("Wrong XML Analysis Engine Descriptor: " + url.toExternalForm());
        }
    }

	private static AnalysisEngineDescription description(String path, Map<String, String> options) throws Exception {
		URL url = TermSuiteRunner.class.getClassLoader().getResource(path.replaceAll("\\.", "/") + ".xml");
		System.out.println("resource specifier :" + url.toString());
		XMLInputSource in = new XMLInputSource(url.toURI().toString());
		ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
		if (specifier instanceof AnalysisEngineDescription) {
			AnalysisEngineDescription description = (AnalysisEngineDescription) specifier;
			//System.out.println("description : " + description);
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
				info(option + "\t" + type + "\t"+ value);
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
}
