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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.cli.Option;

import eu.project.ttc.tools.TermSuite;

/**
 * This class consists of static method used by the CLI.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public final class TermSuiteCLIUtils {

	/** Preferences directory for the current user */
	private static final String USER_HOME = System.getProperty("user.home")
			+ File.separator + ".term-suite" + File.separator
			+ TermSuite.TERMSUITE_VERSION;

	/**
	 * Instances should NOT be constructed in standard programming.
	 */
	private TermSuiteCLIUtils() {}

	/**
	 * Unconditionally close an <code>InputStream</code>. Equivalent to
	 * {@link Reader#close()}, except any exceptions will be ignored.
	 * <p>Code from apache IOUtils.
	 * @param input
	 *            A (possibly null) Reader
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
	 * @return The option instance
	 */
	public static Option createMandatoryOption(String opt, String longOpt,
			boolean hasArg, String description) {
		Option option = new Option(opt, longOpt, hasArg, description);
		option.setRequired(true);
		return option;
	}

}
