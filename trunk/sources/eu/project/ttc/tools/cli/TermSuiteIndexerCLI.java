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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import eu.project.ttc.tools.InputSourceTypes;
import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteRunner;

/**
 * Command line interface for the Indexer engines.
 * @author Sebastián Peña Saldarriaga
 */
public class TermSuiteIndexerCLI {

	private static final String PREFERENCES_FILE_NAME = "IndexerCLI.properties";
		
	/**
	 * Application entry point
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		try {
			
			Properties storedProps = TermSuiteCLIUtils.readFromUserHome(PREFERENCES_FILE_NAME);
			
			// If this is the first time, create empty properties
			if (storedProps == null)
				storedProps = new Properties();
			
			// create the command line parser
			PosixParser parser = new PosixParser();
			
			// create the Options
			Options options = new Options();
			options.addOption(TermSuiteCLIUtils.createMandatoryOption(
					"directory", "", true, "input directory"));
			options.addOption(TermSuiteCLIUtils.createMandatoryOption(
					"language", "", true, "language of the input files"));
			options.addOption(TermSuiteCLIUtils.createMandatoryOption(
					"encoding", "", true, "encoding of the input files"));


			// Indexer specific options
					options.addOption(TermSuiteCLIUtils.createMandatoryOption( "", "Directory", true, "output directory"));
					options.addOption( "", "EnableTermGathering", true, "enable term gathering" );
					options.addOption( "", "EditDistanceClassName", true, "edit distance classname" );
					options.addOption( "", "EditDistanceThreshold", true, "edit distance threshold" );
					options.addOption( "", "EditDistanceNgrams", true, "edit distance ngrams" );
					options.addOption( "", "IgnoreDiacriticsInMultiwordTerms", true, "ignore diacritics in multiword terms" );
					options.addOption( "", "OccurrenceThreshold", true, "occurence threshold" );
					options.addOption( "", "AssociationRateClassName", true, "association rate class name" );
					options.addOption( "", "FilterRule", true, "filter rule" );
					options.addOption( "", "FilterRuleThreshold", true, "filter rule threshold" ); 
					options.addOption( "", "KeepVerbsAndOthers", true, "keep verbs and other" );								

				int index = engineName.lastIndexOf(".");
				String propertiesFileName = engineName.substring(engineName.lastIndexOf(".")+1).concat(".properties");

				try {
					//save properties to project root folder
		    		myProperties.load(new FileInputStream(propertiesFileName));
				} catch (IOException ex) {
		    		info("unable to find the properties file name" + propertiesFileName);			
				}

								
				try {
					CommandLine line = parser.parse( options, arguments, false);
					
					for( Option myOption : line.getOptions() ) {
						if (!myOption.hasArg()) {
							inputType = InputSourceTypes.valueOf(myOption.getOpt().toUpperCase());
							System.out.println(myOption.getOpt());
						} else {
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
						//save properties to project root folder
			    		myProperties.store(new FileOutputStream(propertiesFileName), null);
					} catch (IOException ex) {
			    		ex.printStackTrace();			
					}
					
					TermSuiteRunner.process(engineName, new HashMap<String, String>((Map) myProperties), inputType, 
							myProperties.getProperty("directory"), 
							myProperties.getProperty("language"), 
							myProperties.getProperty("encoding"));

				} catch (ParseException e) {
					// automatically generate the help statement
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp( "java -Xms1g  -Xmx2g -cp target/ttc-term-suite-1.3.jar eu.project.ttc.tools.TermSuiteRunner", options );					
				}
				
			} else {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "TermSuiteRunner", options );
			}
			
		} catch (Exception e) {
			TermSuiteRunner.error(e, 1);
		}
	}

}
