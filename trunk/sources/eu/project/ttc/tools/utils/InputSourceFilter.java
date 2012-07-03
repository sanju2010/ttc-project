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
package eu.project.ttc.tools.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

import eu.project.ttc.tools.InputSourceTypes;

/**
 * A {@link FilenameFilter} that verifies that the specified file is of the
 * appropriate {@link InputSourceTypes}.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class InputSourceFilter implements FilenameFilter {

	private final InputSourceTypes input;

	public InputSourceFilter(InputSourceTypes input) {
		this.input = input;
	}

	@Override
	public boolean accept(File directory, String name) {
		File file = new File(directory, name);
		boolean res = false;
		if (file.isFile()) {
			try {
				URL url = file.toURI().toURL();

				switch (input) {
				case TXT:
					res = url.openConnection().getContentType()
							.equals("text/plain");
					break;

				case XMI:
					res = url.openConnection().getContentType()
							.equals("application/xml")
							&& name.endsWith(".xmi");
					break;

				case URI:
					res = true;
					break;
				}

			} catch (Exception e) {
				res = false;
			}
		}
		return res;
	}
}