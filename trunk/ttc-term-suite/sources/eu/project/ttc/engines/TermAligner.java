package eu.project.ttc.engines;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import eu.project.ttc.tools.aligner.AlignerBinding;
import eu.project.ttc.tools.aligner.AlignerModel;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.metrics.SimilarityDistance;
import eu.project.ttc.models.Context;
import eu.project.ttc.resources.Dictionary;
import eu.project.ttc.resources.Terminology;
import eu.project.ttc.tools.utils.InMemoryMWTIndex;
import eu.project.ttc.tools.utils.PermutationTree;
import eu.project.ttc.tools.utils.TranslationList;
import eu.project.ttc.tools.utils.TranslationListTBXWriter;
import eu.project.ttc.tools.utils.TranslationListTSVWriter;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import eu.project.ttc.types.TranslationCandidateAnnotation;

public class TermAligner extends JCasAnnotator_ImplBase {

	/** Stores the alignment result */
	private TranslationList result;

	/** Handles bilingual tbx output */
	private TranslationListTBXWriter tbxWriter;

	/** Handles TSV output */
	private TranslationListTSVWriter tsvWriter;

	/** Maximum number of translation candidates accepted */
	private int translationCandidateCutOff;

	private Dictionary dictionary;

	private SimilarityDistance similarityDistance;

	private Terminology sourceTerminology;

	private InMemoryMWTIndex sourceIndex = new InMemoryMWTIndex();

	private Terminology targetTerminology;

	private InMemoryMWTIndex targetIndex = new InMemoryMWTIndex();

	private boolean distributional;

	private boolean isCompositional;

	private File outputFile;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		try {

			String name = (String) context
					.getConfigParameterValue(AlignerBinding.PRM.SIMILARITY.getParameter());
			this.setSimilarityDistance(name);

			this.setDistributional(Boolean.TRUE.equals(context
					.getConfigParameterValue(AlignerBinding.PRM.DISTRIBUTIONAL.getParameter())));
			this.setCompositional(Boolean.TRUE.equals(context
					.getConfigParameterValue(AlignerBinding.PRM.COMPOSITIONAL.getParameter())));

			if (sourceTerminology == null) {
				sourceTerminology = (Terminology) context
						.getResourceObject("SourceTerminology");
				String path = (String) context
						.getConfigParameterValue(AlignerBinding.PRM.SRCTERMINOLOGY.getParameter());
				if (path != null) {
					sourceTerminology.load(path);
				}
			}

			if (targetTerminology == null) {
				targetTerminology = (Terminology) context
						.getResourceObject("TargetTerminology");
				String path = (String) context
						.getConfigParameterValue(AlignerBinding.PRM.TGTTERMINOLOGY.getParameter());
				if (path != null) {
					targetTerminology.load(path);
				}
			}

			if (dictionary == null) {
				Dictionary dictionary = (Dictionary) context
						.getResourceObject("Dictionary");
				this.setDictionary(dictionary);
				String path = (String) context
						.getConfigParameterValue(AlignerBinding.PRM.DICTIONARY.getParameter());
				if (path != null) {
					File file = new File(path);
					dictionary.load(file.toURI());
				}
			}

			// Addendum S. Peña Saldarriaga
			result = TranslationList.getList(sourceTerminology.getJCas()
					.getDocumentLanguage(), // Source language
					targetTerminology, // Target terminology
					name.substring(name.lastIndexOf('.') + 1).toLowerCase()); // Score
																				// type
			tbxWriter = new TranslationListTBXWriter();
			tsvWriter = new TranslationListTSVWriter();
			outputFile = createOutputFile(context);

			translationCandidateCutOff = (Integer) context
					.getConfigParameterValue(AlignerBinding.PRM.MAXCANDIDATES.getParameter());
			if (translationCandidateCutOff > 100)
				translationCandidateCutOff = 100;

		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			JCas terminology = sourceTerminology.getJCas();
			String term = this.getTerm(cas);
			this.getContext().getLogger()
					.log(Level.INFO, "Processing '" + term + "'");
			TermAnnotation annotation = searchTermInSourceTerminology(term);
			if (annotation == null) {
				this.getContext()
						.getLogger()
						.log(Level.WARNING,
								"Skipping '"
										+ term
										+ "' as it doesn't belong to the source terminology");
			} else {
				// Lazy index initialization
				if (sourceIndex.size() == 0) {
					initIndex(sourceIndex, sourceTerminology);
					initIndex(targetIndex, targetTerminology);
				}
				this.align(cas, terminology, annotation, term);
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private TermAnnotation searchTermInSourceTerminology(String term) {
		boolean isMWT = term.contains(" ");
		TermAnnotation t = sourceTerminology.get(term);

		// Handle multiword terms that could not be found specifically
		if (t == null && isMWT) {
			String[] components = term.split(" ");
			Set<MultiWordTermAnnotation> candidates = sourceIndex
					.getByLemma(components);
			if (candidates.size() > 0)
				t = candidates.iterator().next();
		}

		return t;
	}

	@Override
	public void collectionProcessComplete()
			throws AnalysisEngineProcessException {
		try {
			tbxWriter.write(result, outputFile);
			tsvWriter.write(result, new File(outputFile.getAbsolutePath()
					.replace(".tbx", ".tsv")));
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private File createOutputFile(UimaContext context) throws CASException {
		return new File(
				(String) context
						.getConfigParameterValue(AlignerBinding.PRM.OUTPUT.getParameter()),
				sourceTerminology.getJCas().getDocumentLanguage() + "-"
						+ targetTerminology.getJCas().getDocumentLanguage()
						+ "-alignment.tbx");
	}

	private void setDictionary(Dictionary dictionary) throws Exception {
		this.dictionary = dictionary;
		String src = sourceTerminology.getJCas().getDocumentLanguage();
		String tgt = targetTerminology.getJCas().getDocumentLanguage();
		String name = "/eu/project/ttc/all/dictionaries/dictionary-" + src
				+ "-" + tgt + ".txt";
		InputStream is = this.getClass().getResourceAsStream(name);
		if (is == null) {
			throw new NullPointerException(name);
		} else {
			this.dictionary.load(name, is);
		}
	}

	private void setSimilarityDistance(String name) throws Exception {
		if (this.similarityDistance == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof SimilarityDistance) {
				this.similarityDistance = (SimilarityDistance) obj;
				this.getContext()
						.getLogger()
						.log(Level.INFO,
								"Setting Similarity Distance: "
										+ this.similarityDistance.getClass()
												.getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name
						+ "' doesn't comply "
						+ SimilarityDistance.class.getCanonicalName());
			}
		}
	}

	private void setDistributional(boolean enabled) {
		this.distributional = enabled;
	}

	private boolean isDistributional() {
		return this.distributional;
	}

	private void setCompositional(boolean enabled) {
		this.isCompositional = enabled;
	}

	private void align(JCas cas, JCas terminology, TermAnnotation annotation,
			String term) throws Exception {
		// Single word terms
		if (annotation instanceof SingleWordTermAnnotation) {

			// is neo classical compound ?
			SingleWordTermAnnotation swt = (SingleWordTermAnnotation) annotation;
			if (swt.getCompound() && isCompositional) {
				this.alignCompound(cas, terminology, swt, term);
			}

			this.alignSingleWord(cas, swt, term);
		} else if (annotation instanceof MultiWordTermAnnotation) {

			if (isCompositional) {
				// Multi word terms
				MultiWordTermAnnotation mwt = (MultiWordTermAnnotation) annotation;
				this.alignMultiWord(cas, terminology, mwt, term);
			}
		}
	}

	private void initIndex(InMemoryMWTIndex index, Terminology terminology)
			throws CASException {
		AnnotationIndex<Annotation> idx = terminology.getJCas()
				.getAnnotationIndex(MultiWordTermAnnotation.type);
		for (Annotation a : idx) {
			index.addTerm(terminology.getJCas(), (MultiWordTermAnnotation) a);
		}
	}

	private String getTerm(JCas cas) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			return iterator.next().getCoveredText();
		} else {
			return null;
		}
	}

	private void alignSingleWord(JCas cas, SingleWordTermAnnotation annotation,
			String term) {
		Context context = sourceTerminology.context(term);
		Map<String, Double> dictionaryCandidates = new HashMap<String, Double>();
		HashMap<String, Double> ones = new HashMap<String, Double>();
		Set<String> directTranslations = dictionary.get().get(term);
		if (directTranslations != null && !directTranslations.isEmpty()) {
			for (String trans : directTranslations) {
				TermAnnotation annot = targetTerminology.get(trans);
				if (annot != null) {
					dictionaryCandidates.put(trans, annot.getSpecificity());
					ones.put(trans, 1.0);
				}
			}
			normalize(dictionaryCandidates);
			dictionaryCandidates = merge(dictionaryCandidates, ones);
		}
		if (context == null) {
			if (dictionaryCandidates.isEmpty()) {
				this.getContext()
						.getLogger()
						.log(Level.WARNING,
								"Skiping '"
										+ term
										+ "' because no context vector could be found for it.");
			} else {

				this.annotate(cas, annotation, dictionaryCandidates);
			}
		} else {
			this.shrink(context);
			Context transfer = this.transfer(term, context);
			Map<String, Double> candidates = this.align(term, transfer);
			normalize(candidates);
			candidates = merge(dictionaryCandidates, candidates);
			this.annotate(cas, annotation, candidates);
		}
	}

	private Map<String, Double> merge(Map<String, Double> distrib1,
			Map<String, Double> distrib2) {
		HashMap<String, Double> first = new HashMap<String, Double>(distrib1);
		HashMap<String, Double> second = new HashMap<String, Double>(distrib2);

		for (String sample : first.keySet()) {
			Double v = second.get(sample);
			if (v != null) {
				first.put(sample, v + first.get(sample));
				second.remove(sample);
			}
		}
		first.putAll(second);
		return first;
	}

	private void normalize(Map<String, Double> distrib) {
		double norm = 0.0;
		for (Double v : distrib.values())
			norm += v.doubleValue();
		for (Entry<String, Double> sample : distrib.entrySet())
			distrib.put(sample.getKey(), sample.getValue() / norm);
	}

	private void shrink(Context context) {
		Set<String> filter = dictionary.get().keySet();
		for (String term : context.getCoOccurrences().keySet()) {
			if (!filter.contains(term)) {
				context.getCoOccurrences().remove(term);
			}
		}
	}

	private void alignMultiWord(JCas cas, JCas terminology,
			MultiWordTermAnnotation entry, String term) throws CASException {
		ArrayList<TermComponentAnnotation> components = this.extract(
				terminology, entry, null);

		// Transfer only noun, name and adjectives
		List<List<String>> candidates = this.transfer(cas, components);

		// Get source pattern
		String[] pattern = getPattern(components);

		List<String> transferred;
		List<MultiWordTermAnnotation> mwtTransferred;
		candidates = this.combine(candidates);
		candidates = this.generate(candidates);
		transferred = this.flatten(candidates, " ");
		mwtTransferred = this.selectMultiwordCandidates(transferred);
		this.annotate(cas, entry, mwtTransferred, pattern);
	}

	private void alignCompound(JCas cas, JCas terminology,
			SingleWordTermAnnotation entry, String term) throws CASException {
		List<List<String>> componentLists = this.extract(terminology, entry);
		Set<String> componentCandidates = new HashSet<String>();
		for (List<String> components : componentLists) {
			components = this.reshape(components);
			if (components.size() >= 2) {
				List<List<String>> candidates = this.transfer(cas, components);
				candidates = this.combine(candidates);
				components = this.flatten(candidates, "");
				components.addAll(this.flatten(candidates, "-"));
				components = this.select(components);
				componentCandidates.addAll(components);
			}
		}

		this.annotate(cas, entry, new ArrayList<String>(componentCandidates));
	}

	private void alignComponent(JCas cas, String term, Set<String> set) {
		Context context = sourceTerminology.context(term);
		if (context != null) {
			Context transfer = this.transfer(term, context);
			Map<String, Double> scores = this.align(term, transfer);
			ScoreComparator comparator = new ScoreComparator();
			comparator.set(scores);
			Map<String, Double> candidates = new TreeMap<String, Double>(
					comparator);
			candidates.putAll(scores);
			int rank = 0;
			for (String translation : candidates.keySet()) {
				rank++;
				set.add(translation);
				if (rank >= 20) {
					break;
				}
			}
		}
	}

	private Context transfer(String sourceTerm, Context sourceContext) {
		Context termContext = new Context();
		for (String sourceCoTerm : sourceContext.getCoOccurrences().keySet()) {
			Double sourceCoOcc = sourceContext.getCoOccurrences().get(
					sourceCoTerm);
			Set<String> resultTerms = null;
			if (!sourceTerm.equals(sourceCoTerm)) {
				resultTerms = dictionary.get().get(sourceCoTerm);
			}
			if (resultTerms != null) {
				int totalOcc = 0;
				for (String resultTerm : resultTerms) {
					Integer occ = targetTerminology.occurrences(resultTerm);
					if (occ == null) {
						continue;
					} else {
						totalOcc += occ.intValue();
					}
				}
				if (totalOcc == 0) {
					continue;
				} else {
					for (String resultTerm : resultTerms) {
						Integer occ = targetTerminology.occurrences(resultTerm);
						if (occ == null) {
							continue;
						} else {
							double score = (sourceCoOcc * occ.intValue())
									/ totalOcc;
							if (score != 0.0) {
								termContext.setCoOccurrences(resultTerm, score,
										Context.MAX_MODE);
							}
						}
					}
				}
			}
		}
		return termContext;
	}

	private Map<String, Double> align(String term, Context termContext) {
		Map<String, Double> candidates = new HashMap<String, Double>();
		for (String targetTerm : targetTerminology.terms()) {
			Context targetContext = targetTerminology.context(targetTerm);
			if (targetContext == null) {
				continue;
			} else {
				TermAnnotation annot = targetTerminology.get(targetTerm);
				double score = similarityDistance.getValue(
						termContext.getCoOccurrences(),
						targetContext.getCoOccurrences());
                // FIXME is it normal that annot can be null here ?
				if ((annot!=null) && !Double.isInfinite(score) && !Double.isNaN(score)) {
					if (InMemoryMWTIndex
							.isAcceptedCategory(annot.getCategory()))
						candidates.put(targetTerm, new Double(score));
				}
			}
		}
		return candidates;
	}

	private void annotate(JCas cas, SingleWordTermAnnotation term,
			Map<String, Double> scores) {
		ScoreComparator comparator = new ScoreComparator();
		comparator.set(scores);
		Map<String, Double> candidates = new TreeMap<String, Double>(comparator);
		candidates.putAll(scores);
		int rank = 0;

		double norm = 0.0;
		for (String translation : candidates.keySet()) {
			rank++;
			norm += candidates.get(translation);
			if (rank >= translationCandidateCutOff) {
				break;
			}
		}

		// Add term to translation list
		result.addTerm(term);
		rank = 0;
		for (String translation : candidates.keySet()) {
			rank++;
			Double score = candidates.get(translation);
			TranslationCandidateAnnotation annotation = new TranslationCandidateAnnotation(
					cas, 0, cas.getDocumentText().length());
			annotation.setTranslation(translation);
			annotation.setScore(score.doubleValue() / norm);
			annotation.setRank(rank);
			annotation.addToIndexes();

			// Add all candidates
			result.addTranslationCandidate(term, annotation);
			if (rank >= translationCandidateCutOff) {
				break;
			}
		}
	}

	/**
	 * provides the term components of a given term if these components cover
	 * the term completely.
	 * 
	 * @param cas
	 * @param annotation
	 * @return
	 * @throws Exception
	 */
	private ArrayList<TermComponentAnnotation> extract(JCas cas,
			TermAnnotation annotation, Object dummy) {
		ArrayList<TermComponentAnnotation> subAnnotations = new ArrayList<TermComponentAnnotation>();
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		while (iterator.hasNext()) {
			TermComponentAnnotation subAnnotation = (TermComponentAnnotation) iterator
					.next();
			subAnnotations.add(subAnnotation);
		}
		return subAnnotations;
	}

	private List<List<String>> extract(JCas cas, TermAnnotation annotation) {
		PermutationTree<TermComponentAnnotation> tree = new PermutationTree<TermComponentAnnotation>(
				null);
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		while (iterator.hasNext()) {
			TermComponentAnnotation component = (TermComponentAnnotation) iterator
					.next();
			boolean done = false;
			for (PermutationTree<TermComponentAnnotation> componentTree : tree
					.children()) {
				done = this.add(component, componentTree);
			}
			if (!done && annotation.getBegin() == component.getBegin()) {
				PermutationTree<TermComponentAnnotation> t = new PermutationTree<TermComponentAnnotation>(
						component);
				tree.addChild(t);
			}
		}
		return tree.strings();
	}

	private boolean add(TermComponentAnnotation component,
			PermutationTree<TermComponentAnnotation> tree) {
		int size = tree.children().size();
		if (size == 0) {
			TermComponentAnnotation last = tree.node();
			if (last.getEnd() <= component.getBegin()) {
				PermutationTree<TermComponentAnnotation> t = new PermutationTree<TermComponentAnnotation>(
						component);
				tree.addChild(t);
				return true;
			} else {
				return false;
			}
		} else {
			boolean done = false;
			for (PermutationTree<TermComponentAnnotation> subtree : tree
					.children()) {
				done = this.add(component, subtree);
			}
			return done;
		}
	}

	/**
	 * checks if all components belong to the dictionary or reduce the component
	 * set by merging the last two components otherwise
	 * 
	 * @param components
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<String> reshape(List<String> components) {
		if (components == null || components.isEmpty()
				|| components.size() == 1) {
			return Collections.emptyList();
		} else {
			int length = components.size();
			for (int index = length - 1; index >= 0; index--) {
				String component = components.get(index);
				if (dictionary.get().keySet().contains(component)) {
					continue;
				} else if (index > 0) {
					List<String> alt = new ArrayList<String>();
					alt.addAll(components.subList(0, index - 1));
					alt.add(components.get(index - 1) + components.get(index));
					alt.addAll(components.subList(index + 1, length));
					return this.reshape(alt);
				} else {
					return Collections.emptyList();
				}
			}
			return components;
		}
	}

	/**
	 * provides the term component dictionary entries from a term defined by its
	 * components.
	 * 
	 * @param components
	 * @return
	 */
	private List<List<String>> transfer(JCas cas, List<String> components) {
		List<List<String>> candidates = new ArrayList<List<String>>();
		for (int index = 0; index < components.size(); index++) {
			String component = components.get(index);
			Set<String> candidate = dictionary.get().get(component);
			if (candidate == null) {
				candidate = new HashSet<String>();
			}
			if (candidate.isEmpty() && isDistributional()) {
				this.alignComponent(cas, component, candidate);
			}
			candidates.add(new ArrayList<String>(candidate));
		}
		return candidates;
	}

	private List<List<String>> transfer(JCas cas,
			ArrayList<TermComponentAnnotation> components) {
		List<List<String>> candidates = new ArrayList<List<String>>();
		TermComponentAnnotation component;
		for (int index = 0; index < components.size(); index++) {
			component = components.get(index);

			// Filter bad categories
			if (!InMemoryMWTIndex.isAcceptedCategory(component.getCategory()))
				continue;

			String compoTerm = component.getCoveredText();
			Set<String> candidate = dictionary.get().get(compoTerm);
			if (candidate == null) {
				candidate = dictionary.get().get(component.getLemma());
			}

			boolean alignComponent = false;
			if (candidate == null) {
				candidate = new HashSet<String>();
				alignComponent = true;
			}

			if (alignComponent && isDistributional()) {
				this.alignComponent(cas, compoTerm, candidate);
			}

			if (alignComponent) {
				if ("noun".equals(component.getCategory()))
					candidate.add(compoTerm);
			}
			candidates.add(new ArrayList<String>(candidate));
		}
		return candidates;
	}

	/**
	 * provides the permutation list
	 * 
	 * @param lists
	 * @return
	 */
	private List<List<String>> generate(List<List<String>> lists) {
		List<List<String>> permutations = new ArrayList<List<String>>();
		for (List<String> list : lists) {
			List<List<String>> permutation = this.permut(list);
			permutations.addAll(permutation);
		}
		return permutations;
	}

	private List<List<String>> permut(List<String> list) {
		if (list.isEmpty()) {
			return Collections.emptyList();
		} else {
			List<List<String>> result = new ArrayList<List<String>>();
			String head = list.get(0);
			List<String> tail = list.subList(1, list.size());
			List<List<String>> permutations = this.permut(tail);
			if (permutations.isEmpty()) {
				result.addAll(this.permut(head, new ArrayList<String>()));
			} else {
				for (List<String> permutation : permutations) {
					result.addAll(this.permut(head, permutation));
				}
			}
			return result;
		}
	}

	/**
	 * provides the list of permutations
	 * 
	 * @param head
	 * @param tail
	 * @return
	 */
	private List<List<String>> permut(String head, List<String> tail) {
		List<List<String>> permutations = new ArrayList<List<String>>();
		for (int index = 0; index <= tail.size(); index++) {
			List<String> clone = new ArrayList<String>(tail);
			clone.add(index, head);
			permutations.add(clone);
		}
		return permutations;
	}

	/**
	 * provides the translated term component combinations from term component
	 * translations.
	 * 
	 * @param components
	 * @return
	 */
	private List<List<String>> combine(List<List<String>> components) {
		List<List<String>> combinaisons = new ArrayList<List<String>>();
		int length = components.size();
		int[] indexes = new int[length];
		int[] sizes = new int[length];
		for (int index = 0; index < length; index++) {
			List<String> items = components.get(index);
			sizes[index] = items.size();
			indexes[index] = sizes[index] - 1;
		}
		while (this.hasNext(0, length, indexes, sizes)) {
			List<String> combinaison = this.get(components, indexes);
			combinaisons.add(combinaison);
			this.next(0, length, indexes, sizes);
		}
		// TODO permutations
		return combinaisons;
	}

	/**
	 * provides the list of items of the current indexes
	 * 
	 * @param components
	 * @param indexes
	 * @return
	 */
	private List<String> get(List<List<String>> components, int[] indexes) {
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < indexes.length; index++) {
			list.add(components.get(index).get(indexes[index]));
		}
		return list;
	}

	/**
	 * increments the indexes
	 * 
	 * @param begin
	 * @param end
	 * @param indexes
	 * @param sizes
	 */
	private void next(int begin, int end, int[] indexes, int[] sizes) {
		for (int i = end - 1; i >= begin; i--) {
			if (indexes[i] > 0) {
				indexes[i]--;
				break;
			} else if (i > begin && indexes[i - 1] > 0) {
				indexes[i] = sizes[i] - 1;
				indexes[i - 1]--;
				break;
			} else if (i == begin && indexes[i] >= 0) {
				indexes[i]--;
				break;
			}
		}
	}

	/**
	 * checks if the domain of indexes is defined
	 * 
	 * @param begin
	 * @param end
	 * @param indexes
	 * @param sizes
	 * @return
	 */
	private boolean hasNext(int begin, int end, int[] indexes, int[] sizes) {
		for (int i = begin; i < end; i++) {
			if (indexes[i] < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * provides the translated term from translated term component combinations
	 * 
	 * @param components
	 * @param glue
	 * @return
	 */
	private List<String> flatten(List<List<String>> components, String glue) {
		List<String> candidates = new ArrayList<String>();
		for (List<String> component : components) {
			StringBuilder candidate = new StringBuilder();
			for (int index = 0; index < component.size(); index++) {
				String item = component.get(index);
				candidate.append(item);
				if (index < component.size() - 1) {
					candidate.append(glue);
				}
			}
			candidates.add(candidate.toString());
		}
		return candidates;
	}

	/**
	 * filter in generated candidates against the target terminology
	 * 
	 * @param candidates
	 * @return
	 */
	private List<MultiWordTermAnnotation> selectMultiwordCandidates(
			List<String> candidates) {
		TreeSet<MultiWordTermAnnotation> prelim = new TreeSet<MultiWordTermAnnotation>(
				InMemoryMWTIndex.LANGSET_MWT_COMPARATOR);
		if (candidates.isEmpty()) {
			this.getContext().getLogger()
					.log(Level.WARNING, "No suitable candidates were found.");
			return new ArrayList<MultiWordTermAnnotation>();
		}

		// I hope the component lemmas are used !
		Set<MultiWordTermAnnotation> matchs;
		for (String candidate : candidates) {
			String[] componentLemmas = candidate.split(" ");
			matchs = targetIndex.getByLemma(componentLemmas);
			for (MultiWordTermAnnotation match : matchs) {
				if (!startsWithDet(match) && !endsWithPrepOrConj(match))
					prelim.add(match);
			}
		}

		if (prelim.isEmpty()) {
			this.getContext()
					.getLogger()
					.log(Level.WARNING,
							"Proposed candidates do not exist in target terminology.");
		}
		return new ArrayList<MultiWordTermAnnotation>(prelim);
	}

	private boolean endsWithPrepOrConj(MultiWordTermAnnotation match) {
		String lastCat = match.getComponents(match.getComponents().size() - 1)
				.getCategory();
		return "adposition".equals(lastCat) || "conjunction".equals(lastCat);
	}

	private boolean startsWithDet(MultiWordTermAnnotation match) {
		String firstCat = match.getComponents(0).getCategory();
		return "article".equals(firstCat) || "determiner".equals(firstCat);
	}

	/**
	 * filter in generated candidates against the target terminology
	 * 
	 * @param candidates
	 * @return
	 */
	private List<String> select(List<String> candidates) {
		List<String> selection = new ArrayList<String>();
		for (String candidate : candidates) {
			if (targetTerminology.exists(candidate)) {
				selection.add(candidate);
			}
		}
		if (candidates.isEmpty()) {
			this.getContext().getLogger()
					.log(Level.WARNING, "No suitable candidates were found.");
		}
		if (!candidates.isEmpty() && selection.isEmpty()) {
			this.getContext()
					.getLogger()
					.log(Level.WARNING,
							"Proposed candidates do not exist in target terminology.");
		}
		return selection;
	}

	/**
	 * inserts one annotation per candidate of neoclassical compound.
	 * 
	 * @param cas
	 * @param candidates
	 */
	private void annotate(JCas cas, TermAnnotation entry,
			List<String> candidates) {
		// Add entry to translation list
		result.addTerm(entry);
		HashMap<String, Double> scoredCandidates = new HashMap<String, Double>();
		for (String candidate : candidates) {
			TermAnnotation annot = targetTerminology.get(candidate);
			if (annot != null) {
				scoredCandidates.put(candidate, annot.getSpecificity());
			}
		}
		normalize(scoredCandidates);

		ScoreComparator comparator = new ScoreComparator();
		comparator.set(scoredCandidates);
		TreeMap<String, Double> sortedCandidates = new TreeMap<String, Double>(
				comparator);
		sortedCandidates.putAll(scoredCandidates);
		int index = 1;
		for (Entry<String, Double> candEntry : sortedCandidates.entrySet()) {
			TranslationCandidateAnnotation annotation = new TranslationCandidateAnnotation(
					cas, 0, cas.getDocumentText().length());
			annotation.setTranslation(candEntry.getKey());
			annotation.setScore(1.0 + candEntry.getValue());
			annotation.setRank(index++);
			annotation.addToIndexes();
			// Add candidates for entry
			result.addTranslationCandidate(entry, annotation);
		}
	}

	/**
	 * inserts one annotation per candidate of multiword term.
	 * 
	 * @param cas
	 * @param candidates
	 */
	private void annotate(JCas cas, TermAnnotation entry,
			List<MultiWordTermAnnotation> candidates, String[] sourcePattern) {
		// Add entry to translation list
		result.addTerm(entry);
		ArrayList<TranslationCandidateAnnotation> annotCand = new ArrayList<TranslationCandidateAnnotation>();
		for (MultiWordTermAnnotation candidate : candidates) {
			TranslationCandidateAnnotation annotation = new TranslationCandidateAnnotation(
					cas, 0, cas.getDocumentText().length());
			annotation.setTranslation(candidate.getCoveredText());
			String[] pattern = getPattern(candidate.getComponents().toArray());
			annotation.setScore(getJaccardIndex(sourcePattern, pattern));
			annotCand.add(annotation);
		}

		Collections.sort(annotCand,
				new Comparator<TranslationCandidateAnnotation>() {

					public int compare(TranslationCandidateAnnotation o1,
							TranslationCandidateAnnotation o2) {
						int r = Double.compare(o2.getScore(), o1.getScore());
						return r == 0 ? o1.getCoveredText().compareTo(
								o2.getCoveredText()) : r;
					}
				});

		int index = 1;
		double norm = 0.0;
		for (TranslationCandidateAnnotation annotation : annotCand) {
			norm += annotation.getScore();
			if (index > translationCandidateCutOff)
				break;
		}

		for (TranslationCandidateAnnotation annotation : annotCand) {
			annotation.setRank(index);
			annotation.addToIndexes();
			annotation.setScore(annotation.getScore() / norm);
			// Add candidates for entry
			result.addTranslationCandidate(entry, annotation);
			index++;
			if (index > translationCandidateCutOff)
				break;
		}
	}

	private double getJaccardIndex(String[] pattern1, String[] pattern2) {
		ArrayList<String> l1 = new ArrayList<String>(Arrays.asList(pattern1));
		List<String> l2 = Arrays.asList(pattern2);
		TreeSet<String> union = new TreeSet<String>(l1);
		union.addAll(l2);
		l1.retainAll(l2);

		return ((double) l1.size()) / union.size();
	}

	private String[] getPattern(Iterable<TermComponentAnnotation> components) {
		HashMap<String, Integer> compIndices = new HashMap<String, Integer>();
		StringBuilder sb = new StringBuilder();
		String cat;
		for (TermComponentAnnotation compo : components) {
			cat = compo.getCategory();
			Integer idx = compIndices.get(cat);
			if (idx == null) {
				idx = 0;
			}
			sb.append(cat + idx).append(" ");
			compIndices.put(cat, idx + 1);

		}
		return sb.toString().trim().split(" ");
	}

	private String[] getPattern(FeatureStructure[] components) {
		HashMap<String, Integer> compIndices = new HashMap<String, Integer>();
		StringBuilder sb = new StringBuilder();
		String cat;
		for (int i = 0; i < components.length; i++) {
			TermComponentAnnotation compo = (TermComponentAnnotation) components[i];
			cat = compo.getCategory();
			Integer idx = compIndices.get(cat);
			if (idx == null) {
				idx = 0;
			}
			sb.append(cat + idx).append(" ");
			compIndices.put(cat, idx + 1);

		}
		return sb.toString().trim().split(" ");
	}

	private class ScoreComparator implements Comparator<String> {

		private Map<String, Double> map;

		public void set(Map<String, Double> map) {
			this.map = map;
		}

		public int compare(String sourceKey, String targetKey) {
			Double sourceValue = this.map.get(sourceKey);
			Double targetValue = this.map.get(targetKey);

			if (sourceValue == null && targetValue == null) {
				return sourceKey.compareTo(targetKey);
			} else if (sourceValue == null) {
				return -1;
			} else if (targetValue == null) {
				return 1;
			} else {
				int r = targetValue.compareTo(sourceValue);
				return r == 0 ? sourceKey.compareTo(targetKey) : r;
			}
		}

	}

}