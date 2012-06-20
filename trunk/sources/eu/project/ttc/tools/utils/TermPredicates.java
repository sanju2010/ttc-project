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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import eu.project.ttc.types.TermAnnotation;

/**
 * Consists exclusively of terms that operate or create {@link TermPredicate}s.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class TermPredicates {

	/** No instances */
	private TermPredicates() {
	};

	/** A predicate that accepts every term */
	public static final TermPredicate TRIVIAL_ACCEPTOR = new TermPredicate() {

		@Override
		public boolean accept(TermAnnotation term) {
			return true;
		}
	};

	/**
	 * Returns a {@link TermPredicate} the accepts terms whose occurrences are
	 * bigger than the specified <code>threshold</code>.
	 * 
	 * @param threshold
	 *            The occurrences threshold
	 * @return The term predicate
	 */
	public static TermPredicate createOccurrencesPredicate(final int threshold) {
		return new TermPredicate() {

			@Override
			public boolean accept(TermAnnotation term) {
				return term.getOccurrences() >= threshold;
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} the accepts terms whose specificity is
	 * bigger than the specified <code>threshold</code>.
	 * 
	 * @param threshold
	 *            The specificity threshold
	 * @return The term predicate
	 */
	public static TermPredicate createSpecificityPredicate(
			final double threshold) {
		return new TermPredicate() {

			@Override
			public boolean accept(TermAnnotation term) {
				return term.getSpecificity() >= threshold;
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} the accepts terms whose relative
	 * frequency is bigger than the specified <code>threshold</code>.
	 * 
	 * @param threshold
	 *            The frequency threshold
	 * @return The term predicate
	 */
	public static TermPredicate createFrequencyPredicate(final double threshold) {
		return new TermPredicate() {

			@Override
			public boolean accept(TermAnnotation term) {
				return term.getFrequency() >= threshold;
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} that accepts terms whose grammatical
	 * categories are <i>noun</i> or <i>adjective</i>.
	 * 
	 * @return The term predicate
	 */
	public static TermPredicate createNounAdjectivePredicate() {
		return new TermPredicate() {

			@Override
			public boolean accept(TermAnnotation term) {
				String cat = term.getCategory();
				return cat.startsWith("noun") || cat.startsWith("adjective");
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} that accepts terms whose grammatical
	 * categories are <i>verb</i> or <i>adverb</i>.
	 * 
	 * @return The term predicate
	 */
	public static TermPredicate createVerbAdverbPredicate() {
		return new TermPredicate() {

			@Override
			public boolean accept(TermAnnotation term) {
				String cat = term.getCategory();
				return "verb".equals(cat) || "adverb".equals(cat);
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} that accepts a term that is accepted by
	 * <code>pred1</code> or <code>pred2</code>.
	 * 
	 * @param pred1
	 *            The first predicate
	 * @param pred2
	 *            The second predicate
	 * @return The resulting term predicate
	 */
	public static TermPredicate createOrPredicate(final TermPredicate pred1,
			final TermPredicate pred2) {
		return new TermPredicate() {
			@Override
			public boolean accept(TermAnnotation term) {
				return pred1.accept(term) || pred2.accept(term);
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} that accepts a term that is accepted by
	 * <code>pred1</code> and <code>pred2</code>.
	 * 
	 * @param pred1
	 *            The first predicate
	 * @param pred2
	 *            The second predicate
	 * @return The resulting term predicate
	 */
	public static TermPredicate createAndPredicate(final TermPredicate pred1,
			final TermPredicate pred2) {
		return new TermPredicate() {
			@Override
			public boolean accept(TermAnnotation term) {
				return pred1.accept(term) && pred2.accept(term);
			}
		};
	}

	/**
	 * Returns a {@link TermPredicate} that accepts terms in sorted occurrence
	 * order up to the specified <code>cutoffRank</code>.
	 * 
	 * @param cutoffRank
	 *            The rank cutoff
	 * @return The term predicate object
	 */
	public static TermPredicate createTopNByOccurrencesPredicate(int cutoffRank) {
		return createSortedTermPredicate(cutoffRank,
				DESCENDING_OCCURRENCE_ORDER);
	}

	/**
	 * Returns a {@link TermPredicate} that accepts terms in sorted frequency
	 * order up to the specified <code>cutoffRank</code>.
	 * 
	 * @param cutoffRank
	 *            The rank cutoff
	 * @return The term predicate object
	 */
	public static TermPredicate createTopNByFrequencyPredicate(int cutoffRank) {
		return createSortedTermPredicate(cutoffRank, DESCENDING_FREQUENCY_ORDER);
	}

	/**
	 * Returns a {@link TermPredicate} that accepts terms in sorted specificity
	 * order up to the specified <code>cutoffRank</code>.
	 * 
	 * @param cutoffRank
	 *            The rank cutoff
	 * @return The term predicate object
	 */
	public static TermPredicate createTopNBySpecificityPredicate(int cutoffRank) {
		return createSortedTermPredicate(cutoffRank,
				DESCENDING_SPECIFICITY_ORDER);
	}

	/**
	 * Returns a {@link TermPredicate} that accepts terms sorted by the
	 * specified <code>comparator</code> up to the <code>cutoffRank</code>.
	 * 
	 * @param cutoffRank
	 *            The rank cutoff
	 * @param comparator
	 *            The comparator
	 * @return The term predicate instance
	 */
	private static TermPredicate createSortedTermPredicate(
			final int cutoffRank, final Comparator<TermAnnotation> comparator) {
		return new ListBasedTermPredicate() {

			@Override
			public void initialize(List<TermAnnotation> termList) {
				
				// Sort annotations
				TreeSet<TermAnnotation> annotations = new TreeSet<TermAnnotation>(comparator);
				annotations.addAll(termList);				
				
				// Copy annotation ids up to the cutoff rank
				selectedIds = new int[cutoffRank];
				int i = 0;
				for (TermAnnotation term : annotations) {
					if (i >= cutoffRank)
						break;
					selectedIds[i++] = term.getAddress();
				}
			}
		};
	}

	/**
	 * Creates a {@link TermPredicate} that accepts only terms contained in the
	 * specified <code>collection</code>
	 * 
	 * @param collection
	 *            The containment collection
	 * @return A term predicate instance
	 */
	public static TermPredicate createContainsPredicate(
			final Collection<TermAnnotation> collection) {
		return new TermPredicate() {
			@Override
			public boolean accept(TermAnnotation term) {
				return collection.contains(term);
			}
		};
	}
	
	/**
	 * A term predicate that can be initialized with a specific term list.
	 * 
	 * @author Sebastián Peña Saldarriaga
	 */
	public static abstract class ListBasedTermPredicate implements
			TermPredicate {

		/** Stores ids that are accepted by the predicate */
		protected int[] selectedIds;

		/**
		 * Initializes the predicate with the initial term list.
		 * 
		 * @param termList
		 *            The initial term list.
		 */
		public abstract void initialize(List<TermAnnotation> termList);

		@Override
		public boolean accept(TermAnnotation term) {
			return Arrays.binarySearch(selectedIds, term.getAddress()) >= 0;
		}

	}

	// ////////////////////////////////////////////////////////////////////////
	// Comparators

	/**
	 * A comparator that imposes a descending occurrence order on
	 * {@link TermAnnotation}s.
	 */
	public static final Comparator<TermAnnotation> DESCENDING_OCCURRENCE_ORDER = new Comparator<TermAnnotation>() {

		@Override
		public int compare(TermAnnotation o1, TermAnnotation o2) {
			int comp = o1.getOccurrences() - o2.getOccurrences();
			return comp == 0 ? o1.getCoveredText().compareTo(
					o2.getCoveredText()) : -comp;
		}
	};

	/**
	 * A comparator that imposes a descending frequency order on
	 * {@link TermAnnotation}s.
	 */
	public static final Comparator<TermAnnotation> DESCENDING_FREQUENCY_ORDER = new Comparator<TermAnnotation>() {

		@Override
		public int compare(TermAnnotation o1, TermAnnotation o2) {
			int comp = Double.compare(o1.getFrequency(), o2.getFrequency());
			return comp == 0 ? o1.getCoveredText().compareTo(
					o2.getCoveredText()) : -comp;
		}
	};

	/**
	 * A comparator that imposes a descending specificity order on
	 * {@link TermAnnotation}s.
	 */
	public static final Comparator<TermAnnotation> DESCENDING_SPECIFICITY_ORDER = new Comparator<TermAnnotation>() {

		@Override
		public int compare(TermAnnotation o1, TermAnnotation o2) {
			int comp = Double.compare(o1.getSpecificity(), o2.getSpecificity());
			return comp == 0 ? o1.getCoveredText().compareTo(
					o2.getCoveredText()) : -comp;
		}
	};
}
