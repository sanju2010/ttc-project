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
package eu.project.ttc.tools.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.aligner.AlignerEngine;
import eu.project.ttc.tools.indexer.IndexerEngine;
import eu.project.ttc.tools.spotter.SpotterEngine;

/**
 * This class consists of static methods used by the CLI.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public final class TermSuiteCLIUtils {

	/** Preferences directory for the current user */
	public static final String USER_HOME = System.getProperty("user.home")
			+ File.separator + ".term-suite" + File.separator
			+ TermSuite.TERMSUITE_VERSION + File.separator;

	/** Path to the aligner engine */
	private static final String ALIGNER_ENGINE = "eu/project/ttc/all/engines/aligner/Aligner.xml";

	/** Name of the parameter that must be set to the input directory */
	public static final String P_INPUT_DIR = "directory";
	
	/** Name of the parameter that must be set to the input files' encoding */
	public static final String P_ENCODING = "encoding";
	
	/** Name of the parameter that must be set to the input files' language */
	public static final String P_LANGUAGE = "language";
	
	/** Name of the parameter that must be set to the configuration file */
	public static final String P_PROPERTIES_FILE = "configFile";
	
	/**
	 * Instances should NOT be constructed in standard programming.
	 */
	private TermSuiteCLIUtils() {}

	/**
	 * Unconditionally close an <code>InputStream</code>. Equivalent to
	 * {@link InputStream#close()}, except any exceptions will be ignored.
	 * <p>
	 * Code from apache IOUtils.
	 * 
	 * @param input
	 *            A (possibly null) InputStream
	 */
	public static void closeQuietly(InputStream input) {
		if (input == null) {
			return;
		}

		try {
			input.close();
		} catch (IOException ioe) {
		}
	}

	/**
	 * Unconditionally close an <code>OutputStream</code>. Equivalent to
	 * {@link OutputStream#close()}, except any exceptions will be ignored.
	 * <p>
	 * Code from apache IOUtils.
	 * 
	 * @param output
	 *            A (possibly null) OutputStream
	 */
	public static void closeQuietly(OutputStream output) {
		if (output == null) {
			return;
		}

		try {
			output.close();
		} catch (IOException ioe) {
		}
	}

	/**
	 * Reads a list of {@link Properties} from the specified
	 * <code>fileName</code> in the user preferences folder.
	 * 
	 * @param fileName
	 *            The name of the file to read
	 * @return The properties read, or <code>null</code> if the file cannot be
	 *         read.
	 */
	public static Properties readFromUserHome(String fileName) {
		File preFile = new File(USER_HOME, fileName);
		FileInputStream input = null;
		try {
			input = new FileInputStream(preFile);
			Properties props = new Properties();
			props.load(input);
			return props;
		} catch (IOException e) {
			return null;
		} finally {
			closeQuietly(input);
		}
	}
	
	/**
	 * Reads a list of {@link Properties} from the specified
	 * <code>fileName</code> in the user preferences folder.
	 * 
	 * @param fileName
	 *            The name of the file to read
	 * @return The properties read, or <code>null</code> if the file cannot be
	 *         read.
	 */
	public static Properties readPropertiesFileName(String fileName) {
		File preFile = new File(fileName);
		FileInputStream input = null;
		try {
			input = new FileInputStream(preFile);
			Properties props = new Properties();
			props.load(input);
			return props;
		} catch (IOException e) {
			return null;
		} finally {
			closeQuietly(input);
		}
	}

	/**
	 * Saves the specified <code>properties</code> to the user preferences
	 * folder.
	 * 
	 * @param fileName
	 *            The output file name
	 * @param properties
	 *            The properties
	 * @throws IOException
	 */
	public static void saveToUserHome(String fileName, Properties properties)
			throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(USER_HOME, fileName));
			properties.store(out, null);
		} finally {
			closeQuietly(out);
		}
	}

	/**
	 * Creates a mandatory {@link Option} using the specified arguments.
	 * 
	 * @param opt
	 *            The name of the option
	 * @param longOpt
	 *            The long representation of the option
	 * @param hasArg
	 *            Specifies whether the Option takes an argument or not
	 * @param description
	 *            Describes the function of the option
	 * @param isMandatory
	 *            whether the Option is mandatory
	 * @return The option instance
	 */
	public static Option createOption(String opt, String longOpt,
			boolean hasArg, String description, boolean isMandatory) {
		Option option = new Option(opt, longOpt, hasArg, description);
		option.setRequired(isMandatory);
		return option;
	}

	/**
	 * Returns the key of the given option
	 * 
	 * @param opt
	 *            The option
	 * @return {@link Option#getOpt()} if the value returned is not empty, or
	 *         {@link Option#getLongOpt()} otherwise.
	 */
	public static String getOptionKey(Option opt) {
		String key = opt.getOpt();
		if (key == null || key.isEmpty())
			key = opt.getLongOpt();
		return key;
	}

	/**
	 * Sets the <code>value</code> of the specified <code>property</code> if no
	 * value exists for it in the given <code>properties</code>.
	 * 
	 * @param properties
	 *            The property list
	 * @param property
	 *            The property name
	 * @param value
	 *            The value to set
	 */
	public static void setToValueIfNotExists(Properties properties,
			String property, String value) {
		if (properties.getProperty(property) == null)
			properties.setProperty(property, value);
	}

	/**
	 * Determines whether the specified <code>property</code> value is
	 * <code>null</code>.
	 * 
	 * @param properties
	 *            The property list
	 * @param property
	 *            The property name
	 * @return <code>true</code> if the value of <code>property</code> is
	 *         <code>null</code>, or <code>false</code> otherwise.
	 */
	public static boolean isNull(Properties properties, String property) {
		return properties.getProperty(property) == null;
	}
	
	/**
	 * Returns the description of an {@link IndexerEngine} for the specified
	 * <code>lang</code>
	 * 
	 * @param lang
	 *            The iso language code
	 * @return The engine descriptor
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InvalidXMLException
	 */
	public static AnalysisEngineDescription getIndexerAEDescription(String lang)
			throws IOException, URISyntaxException, InvalidXMLException {
		String language = new Locale(lang).getDisplayLanguage(Locale.ENGLISH);
		return getAEDescription(language, "Indexer");
	}

	/**
	 * Returns the description of a {@link SpotterEngine} for the specified
	 * <code>lang</code>
	 * 
	 * @param lang
	 *            The iso language code
	 * @return The engine descriptor
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InvalidXMLException
	 */
	public static AnalysisEngineDescription getSpotterAEDescription(String lang)
			throws IOException, URISyntaxException, InvalidXMLException {
		String language = new Locale(lang).getDisplayLanguage(Locale.ENGLISH);
		return getAEDescription(language, "SpotterController");
	}

	/**
	 * Returns the description of a {@link AlignerEngine} for the specified
	 * <code>lang</code>
	 * 
	 * @param lang
	 *            The iso language code
	 * @return The engine descriptor
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InvalidXMLException
	 */
	public static AnalysisEngineDescription getAlignerAEDescription(String lang)
			throws IOException, URISyntaxException, InvalidXMLException {
		ResourceSpecifier specifier = getResourceSpecifier(ALIGNER_ENGINE);
		if (specifier instanceof AnalysisEngineDescription) {
			return (AnalysisEngineDescription) specifier;
		} else {
			throw new IllegalArgumentException(
					"Invalid XML Analysis Engine Descriptor: " + ALIGNER_ENGINE);
		}
	}

	/**
	 * Returns the descriptor of a given <code>engine</code> for the specified
	 * <code>language</code>
	 * 
	 * @param language
	 *            The language display name in english
	 * @param engine
	 *            The engine type, i.e. SpotterController or Indexer (case sensitive)
	 * @return The engine descriptor
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InvalidXMLException
	 */
	private static AnalysisEngineDescription getAEDescription(String language,
			String engine) throws IOException, URISyntaxException,
			InvalidXMLException {

		String engineName = "eu/project/ttc/" + language.toLowerCase()
				+ "/engines/" + engine.toLowerCase() + "/" + language + engine
				+ ".xml";

		ResourceSpecifier specifier = getResourceSpecifier(engineName);
		if (specifier instanceof AnalysisEngineDescription) {
			return (AnalysisEngineDescription) specifier;
		} else {
			throw new IllegalArgumentException(
					"Invalid XML Analysis Engine Descriptor: " + engineName);
		}
	}

	/**
	 * Returns the descriptor specified by the given <code>path</code>.
	 * 
	 * @param path
	 *            The path to the xml descriptor
	 * @return The engine descriptor
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InvalidXMLException
	 */
	private static ResourceSpecifier getResourceSpecifier(String path)
			throws IOException, URISyntaxException, InvalidXMLException {
		URL url = TermSuiteRunner.class.getClassLoader().getResource(path);
		XMLInputSource in = new XMLInputSource(url.toURI().toString());
		return UIMAFramework.getXMLParser().parseResourceSpecifier(in);
	}
	
	/**
	 * Sets the configuration parameter following the <code>description</code>
	 * of a given <code>AnalysisEngine</code>.
	 * 
	 * @param description
	 *            The <code>AnalysisEngine</code> description
	 * @param parameters
	 *            The parameters of the engine.
	 */
	public static void setConfigurationParameters(
			AnalysisEngineDescription description, Properties parameters) {
		AnalysisEngineMetaData metadata = description
				.getAnalysisEngineMetaData();
		ConfigurationParameterDeclarations declarations = metadata
				.getConfigurationParameterDeclarations();
		ConfigurationParameterSettings settings = metadata
				.getConfigurationParameterSettings();

		for (String option : parameters.stringPropertyNames()) {

			String value = parameters.getProperty(option);
			ConfigurationParameter declaration = declarations.getConfigurationParameter(null, option);

			if (declaration != null) {
				String type = declaration.getType();
				TermSuiteRunner.info(option + "\t" + type + "\t" + value);
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

	/**
	 * Prints the command line usage to the std error output
	 * 
	 * @param e
	 *            The error that raised the help message
	 * @param cmdLine
	 *            The command line usage
	 * @param options
	 *            The options expected
	 */
	public static void printUsage(ParseException e, String cmdLine,
			Options options) {
		System.err.println(e.getMessage());
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptionComparator(new ShortOptionsFirstComparator());
		PrintWriter pw = new PrintWriter(System.err);
		formatter.printUsage(pw, cmdLine.length() + 7, cmdLine, options);
		pw.flush();
	}
}
