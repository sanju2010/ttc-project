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
package eu.project.ttc.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

public class GeneralLanguageResource implements GeneralLanguage {

	private int frequency;
	
	private Map<String, Integer> frequencies;
	
	public GeneralLanguageResource() {
		this.frequency = 0;
		this.frequencies = new HashMap<String, Integer>();
	}
	
	@SuppressWarnings("resource")
    @Override
	public void load(DataResource data) throws ResourceInitializationException {
		Scanner scanner = null;
	    try {
			scanner = new Scanner(data.getInputStream());
			scanner.useDelimiter("\n");
			int index = 0;
			while (scanner.hasNext()) {
				index++;
				String line = scanner.next();
				String[] items = line.split("::");
				if (items.length == 3) {
					String key = items[0].trim();
					Integer value = Integer.valueOf(items[2].trim());
					this.set(key.toLowerCase(), value.intValue());
				} else {
					throw new IOException("Wrong general language format at line " + index + ": " + line);
				}
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		} finally {
		    IOUtils.closeQuietly(scanner);
		}
	}
	
	@Override
	public void set(String entry, int frequency) {
		this.frequency += frequency;
		this.frequencies.put(entry, new Integer(frequency));
	}

	@Override
	public double get(String entry) {
		Integer frequency = this.frequencies.get(entry.toLowerCase());
		double quotient = new Double(this.frequency).doubleValue();
		if (frequency == null) {
			return 1.0 / quotient;
		} else {
			double freq = new Double(frequency.intValue()).doubleValue();
			return freq / quotient;
		}
	}

}
