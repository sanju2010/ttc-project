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

import eu.project.ttc.metrics.DiacriticInsensitiveLevenshtein;
import eu.project.ttc.types.MultiWordTermAnnotation;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;
import uima.sandbox.mapper.resources.Mapping;

import java.util.*;

/**
 * Gathers multiword terms by considering them as a single word, without
 * diacritics.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class MultiWordAsSimpleGatherer {

	private Map<String, List<MultiWordTermAnnotation>> annotations;

	private DiacriticInsensitiveLevenshtein editDistance = new DiacriticInsensitiveLevenshtein();

	public void gather(JCas aJCas, Mapping mapping, double threshold) {
		annotations = new HashMap<String, List<MultiWordTermAnnotation>>();
		editDistance.setFailThreshold(threshold);
		this.index(aJCas, mapping);
		this.clean();
		this.sort();
		this.gatherLoop(aJCas, threshold);
	}

    // TODO : Move to external db
	private void index(JCas aJCas, Mapping mapping) {
		HashSet<MultiWordTermAnnotation> remove = new HashSet<MultiWordTermAnnotation>();
		AnnotationIndex<Annotation> index = aJCas
				.getAnnotationIndex(MultiWordTermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			MultiWordTermAnnotation annotation = (MultiWordTermAnnotation) iterator
					.next();
			String key = getKey(annotation, mapping);
			if (key == null) {
				remove.add(annotation);
			} else {
				List<MultiWordTermAnnotation> list = annotations.get(key);
				if (list == null) {
					list = new ArrayList<MultiWordTermAnnotation>();
					annotations.put(key, list);
				}
				list.add(annotation);
			}
		}
		for (MultiWordTermAnnotation r : remove) {
			r.removeFromIndexes();
		}
	}

	private void clean() {
		HashSet<String> keys = new HashSet<String>();
		for (String key : annotations.keySet()) {
			List<MultiWordTermAnnotation> list = annotations.get(key);
			if (list.size() < 2) {
				keys.add(key);
			}
		}
		for (String key : keys) {
			annotations.remove(key);
		}
	}

	private void sort() {
		this.setAnnotations(annotations);
	}

	private void setAnnotations(
			Map<String, List<MultiWordTermAnnotation>> annotations) {
		Map<String, List<MultiWordTermAnnotation>> a = new TreeMap<String, List<MultiWordTermAnnotation>>();
		a.putAll(annotations);
		this.annotations = a;
	}

	private void gatherLoop(JCas cas, double threshold) {
		UIMAFramework.getLogger().log(
				Level.INFO,
				"Edit-distance gathering over " + annotations.size()
						+ " term classes");

		MultiWordTermAnnotation t1, t2;
		for (String key : annotations.keySet()) {
			List<MultiWordTermAnnotation> list = annotations.get(key);
			UIMAFramework.getLogger().log(
					Level.INFO,
					"Edit-distance gathering over the '" + key
							+ "' term class of size " + list.size());
			double d;
			for (int i = 0; i < list.size(); i++) {
				t1 = list.get(i);
				String source = t1.getCoveredText().toLowerCase();
				int componentCount = t1.getComponents().size();
				for (int j = i + 1; j < list.size(); j++) {
					t2 = list.get(j);

					// If terms don't have the same number of components we
					// continue. We hope to get some speed up
					if (componentCount != t2.getComponents().size())
						continue;

					String target = t2.getCoveredText().toLowerCase();
					d = editDistance.normalize(
							editDistance.compute(source, target), source,
							target);
					if (d >= threshold) {
						this.annotate(cas, list, i, j);
					}
				}
			}
            System.gc();
		}
	}

	private void annotate(JCas cas, List<MultiWordTermAnnotation> list, int i,
			int j) {
		MultiWordTermAnnotation base = list.get(i);
		MultiWordTermAnnotation variant = list.get(j);
		if (base.getFrequency() < variant.getFrequency()) {
			MultiWordTermAnnotation tmp = base;
			base = variant;
			variant = tmp;
		}
		FSArray array = base.getVariants();
		if (array == null) {
			array = new FSArray(cas, 1);
		} else {
			MultiWordTermAnnotation[] fs = new MultiWordTermAnnotation[array
					.size() + 1];
			array.copyToArray(0, fs, 0, array.size());
			array = new FSArray(cas, fs.length);
			array.copyFromArray(fs, 0, 0, fs.length - 1);
		}
		base.setVariants(array);
		base.setVariants(array.size() - 1, variant);
	}

	private String getKey(Annotation annotation, Mapping mapping) {
		String coveredText = annotation.getCoveredText().toLowerCase();
		char ch = coveredText.charAt(0);
		if (Character.isLetter(ch)) {
			String key = Character.toString(ch);
			String value = mapping.get(key);
			if (value == null) {
				return key;
			} else {
				return value;
			}
		} else {
			return null;
		}
	}
}
