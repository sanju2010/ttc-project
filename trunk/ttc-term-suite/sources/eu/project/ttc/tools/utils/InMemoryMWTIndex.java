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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

/**
 * An index for multiword terms, where a term can be searched by any of its
 * components.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class InMemoryMWTIndex {

	/** Accepted index term grammatical categories */
	private static final String[] CATEGORIES = new String[] { "adjective",
			"name", "noun" };

	/** Compares multiword terms by Langset */
	public static final Comparator<MultiWordTermAnnotation> LANGSET_MWT_COMPARATOR = new Comparator<MultiWordTermAnnotation>() {

		@Override
		public int compare(MultiWordTermAnnotation mwt1,
				MultiWordTermAnnotation mwt2) {
			return mwt1.getLangset().compareTo(mwt2.getLangset());
		}
	};

	/** Stores the lemma -> annotation index */
	private final HashMap<String, TreeSet<MultiWordTermAnnotation>> mIndex = new HashMap<String, TreeSet<MultiWordTermAnnotation>>();

	/**
	 * Adds the specified term <code>annotation</code> to the index.
	 * 
	 * @param cas
	 *            The annotation source CAS.
	 * @param annotation
	 *            The annotation.
	 */
	public void addTerm(JCas cas, MultiWordTermAnnotation annotation) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		TermComponentAnnotation component;
		while (iterator.hasNext()) {
			component = (TermComponentAnnotation) iterator.next();
			if (isAcceptedCategory(component.getCategory())) {
				addToIndex(component.getLemma(), annotation);
			}
		}
	}

	/**
	 * Gets the MWT containing the specified <code>lemma</code>.
	 * 
	 * @param lemma
	 *            The lemma to look for.
	 * @return The set of MWT matching the specified <code>lemma</code>.
	 */
	public Set<MultiWordTermAnnotation> getByLemma(String lemma) {
		TreeSet<MultiWordTermAnnotation> hits = mIndex.get(lemma);
		return hits == null ? Collections.<MultiWordTermAnnotation> emptySet()
				: Collections.unmodifiableSet(hits);
	}

	/**
	 * Gets the MWT containing the specified <code>lemmas</code>.
	 * 
	 * @param lemmas
	 *            The lemmas to look for.
	 * @return The set of MWT matching the specified lemmas, can be the empty
	 *         set.
	 */
	public Set<MultiWordTermAnnotation> getByLemma(String... lemmas) {
		Set<MultiWordTermAnnotation> hits = mIndex.get(lemmas[0]);
		if (hits == null)
			return Collections.<MultiWordTermAnnotation> emptySet();
		hits = new HashSet<MultiWordTermAnnotation>(hits);
		TreeSet<MultiWordTermAnnotation> otherHits;
		for (int i = 1; i < lemmas.length; i++) {
			otherHits = mIndex.get(lemmas[i]);
			if (otherHits == null)
				return Collections.<MultiWordTermAnnotation> emptySet();

			hits.retainAll(otherHits);
		}
		return hits;
	}

	/**
	 * Determines whether the specified <code>category</code> should be accepted
	 * in the index.
	 * 
	 * @param category
	 *            The category to test.
	 * @return <code>true</code> if the category is accepted or
	 *         <code>false</code> otherwise.
	 */
	public static boolean isAcceptedCategory(String category) {
		return Arrays.binarySearch(CATEGORIES, category) > -1;
	}

	/**
	 * Associates the specified <code>annotation</code> to the given
	 * <code>lemma</code>.
	 * 
	 * @param lemma
	 *            The key lemma
	 * @param annotation
	 *            The MWT annotation.
	 */
	private void addToIndex(String lemma, MultiWordTermAnnotation annotation) {
		TreeSet<MultiWordTermAnnotation> list = mIndex.get(lemma);
		if (list == null) {
			list = new TreeSet<MultiWordTermAnnotation>(LANGSET_MWT_COMPARATOR);
			mIndex.put(lemma, list);
		}
		list.add(annotation);
	}

	/**
	 * Returns the number of mapped lemmas in this index.
	 * 
	 * @return the number of mapped lemmas in this index.
	 */
	public int size() {
		return mIndex.size();
	}
}
