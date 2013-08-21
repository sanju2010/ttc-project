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
import java.io.FileWriter;
import java.io.IOException;

import eu.project.ttc.tools.utils.TranslationList.TranslationCandidate;
import eu.project.ttc.tools.utils.TranslationList.TranslationTerm;

/**
 * Writes a {@link TranslationList} to TSV format.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public final class TranslationListTSVWriter {

	// ////////////////////////////////////////////////////////////////////////
	// Class members

	/** Default constructor */
	public TranslationListTSVWriter() {
	}

	public void write(TranslationList list, File output) throws IOException {
		FileWriter fw = new FileWriter(output);
		TranslationTerm trans;
		String srcTerm;
		int srcCount = 1;
		int tgtCount;
		for (String term : list) {
			trans = list.getTermData(term);
			srcTerm = trans.getTerm();
			tgtCount = 1;
			for (TranslationCandidate cand : trans) {
				fw.append(Integer.toString(srcCount)).append('\t');
				fw.append(srcTerm).append('\t');
				fw.append(Integer.toString(tgtCount)).append('\t');
				fw.append(cand.getCandidate()).append('\n');
				tgtCount++;
			}
			srcCount++;
		}
		fw.close();
	}

}