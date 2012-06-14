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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.uima.cas.CASException;

import eu.project.ttc.resources.Terminology;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TranslationCandidateAnnotation;

/**
 * Represents a list of terms to translate and the translation candidates.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public final class TranslationList implements Iterable<String> {

	// ////////////////////////////////////////////////////////////////////////
	// Singleton acces to lists

	/** Stores allowed instances of the class */
	private static final HashMap<String, TranslationList> _Instances = new HashMap<String, TranslationList>();

	/**
	 * Creates a translation list with the specified parameters. Another call to
	 * this method with the same parameters will return the same instance.
	 * 
	 * @param source
	 *            The source language
	 * @param targetTerminology
	 *            The target terminology space
	 * @param scoreType
	 *            The likelihood score name
	 * @return A {@link TranslationList} instance
	 * @throws CASException
	 */
	public static final TranslationList getList(String source,
			Terminology targetTerminology, String scoreType)
			throws CASException {
		String key = source + "-"
				+ targetTerminology.getJCas().getDocumentLanguage() + "-"
				+ scoreType;
		if (!_Instances.containsKey(key))
			_Instances.put(key, new TranslationList(source, targetTerminology,
					scoreType));
		return _Instances.get(key);
	}

	// ////////////////////////////////////////////////////////////////////////
	// Class members

	/** Source language */
	private final String source;

	/** Target language */
	private final String target;

	/** The similarity score name */
	private final String scoreType;

	/** The target terminology space */
	private final Terminology targetTerminology;

	/** Contains the term list */
	private final HashMap<String, TranslationList.TranslationTerm> terms = new HashMap<String, TranslationList.TranslationTerm>();

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            The source language
	 * @param targetTerminology
	 *            The target terminology space
	 * @param scoreType
	 *            The likelihood score name
	 * @throws CASException
	 */
	private TranslationList(String source, Terminology targetTerminology,
			String scoreType) throws CASException {
		this.source = source;
		this.target = targetTerminology.getJCas().getDocumentLanguage();
		this.targetTerminology = targetTerminology;
		this.scoreType = scoreType;
	}

	/**
	 * Gets the source language
	 * 
	 * @return The source language
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Gets the target language
	 * 
	 * @return The target language
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Gets the likelihood score name
	 * 
	 * @return The score name
	 */
	public String getScoreType() {
		return scoreType;
	}

	/**
	 * Adds a term to the translation list
	 * 
	 * @param term
	 *            The term
	 */
	public void addTerm(TermAnnotation term) {
		String text = term.getCoveredText();
		if (!terms.containsKey(text))
			terms.put(text,
					new TranslationTerm(text, term.getLangset()));
	}

	/**
	 * Adds a translation <code>candidate</code> to the specified
	 * <code>term</code>.
	 * 
	 * @param term
	 *            The term
	 * @param candidate
	 *            The candidate
	 */
	public void addTranslationCandidate(TermAnnotation term,
			TranslationCandidateAnnotation candidate) {
		TranslationTerm t = terms.get(term.getCoveredText());
		t.addCandidate(candidate,
				targetTerminology.get(candidate.getTranslation()));
	}

	/**
	 * Gets the {@link TranslationTerm} instance associated to the specified
	 * <code>term</code>.
	 * 
	 * @param term
	 *            The term to look for
	 * @return The term data
	 */
	public TranslationTerm getTermData(String term) {
		return terms.get(term);
	}

	@Override
	public Iterator<String> iterator() {
		return terms.keySet().iterator();
	}

	/**
	 * Represents a term to be translated.
	 * 
	 * @author Sebastián Peña Saldarriaga
	 */
	public static final class TranslationTerm implements
			Iterable<TranslationCandidate> {

		/** The term as a string */
		private final String term;

		/** The term langset id */
		private final String langset;

		/** The term's candidates */
		private final ArrayList<TranslationCandidate> candidates = new ArrayList<TranslationCandidate>();

		/**
		 * Constructor
		 * 
		 * @param term
		 *            The term
		 * @param langset
		 *            The term's langset id
		 */
		protected TranslationTerm(String term, String langset) {
			this.term = term;
			this.langset = langset;
		}

		/**
		 * Adds the specified <code>candidate</code> to this term candidate
		 * list.
		 * 
		 * @param candidate
		 *            The candidate.
		 * @param candidateTerm
		 *            The candidate term annotation.
		 */
		protected void addCandidate(TranslationCandidateAnnotation candidate,
				TermAnnotation candidateTerm) {
			String address = (candidateTerm != null) ? candidateTerm
					.getLangset() : "nonexistent-langset-id";
			candidates.add(new TranslationCandidate(candidate.getTranslation(),
					candidate.getRank(), address, candidate.getScore()));
		}

		@Override
		public Iterator<TranslationCandidate> iterator() {
			return candidates.iterator();
		}

		/**
		 * Gets the term as a {@link String}
		 * 
		 * @return The term string
		 */
		public String getTerm() {
			return term;
		}

		/**
		 * Gets the term langset id
		 * 
		 * @return The term's langset id
		 */
		public String getLangset() {
			return langset;
		}
	}

	/**
	 * Represents a translation candidate
	 * 
	 * @author Sebastián Peña Saldarriaga
	 */
	public static final class TranslationCandidate {

		/** The translation candidate text */
		private final String candidate;

		/** The candidate rank */
		private final int rank;

		/** The candidate langset id */
		private final String langset;

		/** The candidate score */
		private final double score;

		/**
		 * Initializes a new instance of the class
		 * 
		 * @param candidate
		 *            The candidate string
		 * @param rank
		 *            The candidate rank
		 * @param langset
		 *            The candidate langset id
		 * @param score
		 *            The candidate score
		 */
		public TranslationCandidate(String candidate, int rank, String langset,
				double score) {
			this.candidate = candidate;
			this.rank = rank;
			this.langset = langset;
			this.score = score;
		}

		/**
		 * Gets the candidate as a {@link String}
		 * 
		 * @return The candidate string
		 */
		public String getCandidate() {
			return candidate;
		}

		/**
		 * Gets the candidate rank
		 * 
		 * @return The candidate rank
		 */
		public int getRank() {
			return rank;
		}

		/**
		 * Gets the candidate langset id
		 * 
		 * @return The candidate langset id
		 */
		public String getLangset() {
			return langset;
		}

		/**
		 * Gets the candidate score
		 * 
		 * @return The candidate score
		 */
		public double getScore() {
			return score;
		}
	}
}
