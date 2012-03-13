package eu.project.ttc.engines;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.metrics.SimilarityDistance;
import eu.project.ttc.models.Context;
import eu.project.ttc.resources.Dictionary;
import eu.project.ttc.resources.Terminology;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import eu.project.ttc.types.TranslationCandidateAnnotation;

public class TermAligner extends JCasAnnotator_ImplBase {
	
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary dictionary) throws Exception {
		this.dictionary = dictionary;
		String src = this.getSourceTerminology().getJCas().getDocumentLanguage();
		String tgt = this.getTargetTerminology().getJCas().getDocumentLanguage();
		String name = src + "-" + tgt + "-dictionary.txt";
		InputStream is = this.getClass().getResourceAsStream("eu/project/ttc/all/dictionaries/" + name);
		this.dictionary.load(name, is);
	}
	
	private Dictionary getDictionary() {
		return this.dictionary;
	}
	
	private SimilarityDistance similarityDistance;
	
	private void setSimilarityDistance(String name) throws Exception {
		if (this.similarityDistance == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof SimilarityDistance) {
				this.similarityDistance = (SimilarityDistance) obj;
				this.getContext().getLogger().log(Level.INFO,"Setting Similarity Distance: " + this.similarityDistance.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + SimilarityDistance.class.getCanonicalName());
			}
		}
	}
	
	private SimilarityDistance getSimilarityDistance() {
		return this.similarityDistance;
	}
	
	private Terminology sourceTerminology;
	
	private void setSourceTerminology(Terminology terminology) {
		this.sourceTerminology = terminology;
	}
	
	private Terminology getSourceTerminology() {
		return this.sourceTerminology;
	}
	
	private Terminology targetTerminology;
	
	private void setTargetTerminology(Terminology terminology) { 
		this.targetTerminology = terminology;
	}
	
	private Terminology getTargetTerminology() {
		return this.targetTerminology;
	}
	
	private boolean distributional;
	
	private void distributional(boolean enabled) {
		this.distributional = enabled;
	}
	
	private boolean distributional() {
		return this.distributional;
	}
	
	private boolean compositional;
	
	private void compositional(boolean enabled) {
		this.compositional = enabled;
	}
	
	private boolean compositional() {
		return this.compositional;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			String name = (String) context.getConfigParameterValue("SimilarityDistanceClassName");
			this.setSimilarityDistance(name);
			
			Boolean distributional = (Boolean) context.getConfigParameterValue("DistributionalMethod");
			this.distributional(distributional == null ? false : distributional.booleanValue());

			Boolean compositional = (Boolean) context.getConfigParameterValue("CompositionalMethod");
			this.compositional(compositional == null ? false : compositional.booleanValue());

			if (this.getSourceTerminology() == null) {
				Terminology terminology = (Terminology) context.getResourceObject("SourceTerminology");
				this.setSourceTerminology(terminology);
				String path = (String) context.getConfigParameterValue("SourceTerminologyFile");
				if (path != null) {
					this.getSourceTerminology().load(path);
				}
			}
			
			if (this.getTargetTerminology() == null) {
				Terminology terminology = (Terminology) context.getResourceObject("TargetTerminology");
				this.setTargetTerminology(terminology);
				String path = (String) context.getConfigParameterValue("TargetTerminologyFile");
				if (path != null) {
					this.getTargetTerminology().load(path);
				}
			}
			
			if (this.getDictionary() == null) {
				Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
				this.setDictionary(dictionary);				
				String path = (String) context.getConfigParameterValue("DictionaryFile");
				if (path != null) {
					File file = new File(path);
					this.getDictionary().load(file.toURI());
				}
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			JCas terminology = this.getSourceTerminology().getJCas();
			String term = this.getTerm(cas);
			this.getContext().getLogger().log(Level.INFO,"Processing '" + term + "'");
			TermAnnotation annotation = this.getSourceTerminology().get(term);
			if (annotation == null) {
				this.getContext().getLogger().log(Level.WARNING, "Skiping '" + term + "' as it doesn't belong to the source terminology");
			} else {
				if (this.compositional()) {
					if (annotation instanceof SingleWordTermAnnotation) {
						SingleWordTermAnnotation swt = (SingleWordTermAnnotation) annotation;
						if (swt.getCompound()) {
							this.alignCompound(cas, terminology, term, swt.getNeoclassical());
						}
					} else if (annotation instanceof MultiWordTermAnnotation) {
						this.alignMultiWord(cas, terminology, term);
					}
				}
				if (this.distributional()) {
					if (annotation instanceof SingleWordTermAnnotation) {
						this.alignSingleWord(cas, term);
					}
				}
			} 
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		} 
	}

	private String getTerm(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			return iterator.next().getCoveredText();
		} else {
			return null;
		}
	}

	private void alignSingleWord(JCas cas, String term) {
		Context context = this.getSourceTerminology().context(term);
		if (context == null) {
			this.getContext().getLogger().log(Level.WARNING,"Skiping '" + term + "'");
		} else {
			this.shrink(context);
			Context transfer = this.transfer(term, context);
			Map<String, Double> candidates = this.align(term, transfer);
			this.annotate(cas, candidates);
		}
	}
	

	private void shrink(Context context) {
		Set<String> filter = this.getDictionary().get().keySet();
		for (String term : context.getCoOccurrences().keySet()) {
			if (!filter.contains(term)) {
				context.getCoOccurrences().remove(term);
			}
		}
	}

	private void alignMultiWord(JCas cas, JCas terminology, String term) throws CASException {
		TermAnnotation entry = this.retrieve(terminology, MultiWordTermAnnotation.type, term);
		if (entry != null) {
			List<String> components = this.extract(terminology, entry, false);
			List<List<String>> candidates = this.transfer(cas, components);
			candidates = this.combine(candidates);
			candidates = this.generate(candidates);
			components = this.flatten(candidates, " ");
			components = this.select(components);
			this.annotate(cas, components);
		}
	}

	private void alignCompound(JCas cas, JCas terminology, String term, boolean reshape) throws CASException {
		TermAnnotation termEntry = this.retrieve(terminology, SingleWordTermAnnotation.type, term);
		if (termEntry == null) { 
			return;
		} else {
			List<String> components = this.extract(terminology, termEntry, true);
			if (reshape) {
				this.reshape(components);
			}		
			List<List<String>> candidates = this.transfer(cas, components);
			candidates = this.combine(candidates);
			// TODO generate
			components = this.flatten(candidates, "");			
			components.addAll(this.flatten(candidates, "-"));
			components.addAll(this.flatten(candidates, " "));
			components = this.select(components);
			this.annotate(cas, components);			
		}
	}
		
	private void alignComponent(JCas cas, String term, Set<String> set) {
		Context context = this.getSourceTerminology().context(term);
		if (context == null) {
			this.getContext().getLogger().log(Level.WARNING,"Skiping component '" + term +  "'");
		} else {
			Context transfer = this.transfer(term, context);
			Map<String, Double> scores = this.align(term, transfer);
			ScoreComparator comparator = new ScoreComparator();
			comparator.set(scores);
			Map<String, Double> candidates = new TreeMap<String, Double>(comparator);
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
			Double sourceCoOcc = sourceContext.getCoOccurrences().get(sourceCoTerm);
			Set<String> resultTerms = null;
			if (!sourceTerm.equals(sourceCoTerm)) {
				resultTerms = this.getDictionary().get().get(sourceCoTerm);
			}
			if (resultTerms != null) {  
				int totalOcc = 0;
				for (String resultTerm : resultTerms) {
					Integer occ = this.getTargetTerminology().occurrences(resultTerm);
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
						Integer occ = this.getTargetTerminology().occurrences(resultTerm);
						if (occ == null) {
							continue;
						} else {
							double score = (sourceCoOcc * occ.intValue()) / totalOcc;
							if (score != 0.0) {
								termContext.setCoOccurrences(resultTerm, score, Context.MAX_MODE);
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
		for (String targetTerm : this.getTargetTerminology().terms()) {
			Context targetContext = this.getTargetTerminology().context(targetTerm);
			if (targetContext == null) {
				continue;
			} else {
				double score = this.getSimilarityDistance().getValue(termContext.getCoOccurrences(),targetContext.getCoOccurrences());	
				if (!Double.isInfinite(score) && !Double.isNaN(score)) {
					candidates.put(targetTerm, new Double(score));
				}
			}
		}
		return candidates;
	}
	
	private void annotate(JCas cas, Map<String, Double> scores) {
		ScoreComparator comparator = new ScoreComparator();
		comparator.set(scores);
		Map<String, Double> candidates = new TreeMap<String, Double>(comparator);
		candidates.putAll(scores);
		int rank = 0;
		for (String translation : candidates.keySet()) {
			rank++;
			Double score = candidates.get(translation);
			TranslationCandidateAnnotation annotation = new TranslationCandidateAnnotation(cas, 0, cas.getDocumentText().length());
			annotation.setTranslation(translation);
			annotation.setScore(score.doubleValue());
			annotation.setRank(rank);
			annotation.addToIndexes();
			if (rank >= 100) {
				break;
			}
		}
	}
	
	/**
	 * extracts the neo-classical compound in the source terminology.
	 * 
	 * @param cas
	 * @param term
	 * @return
	 * @throws Exception 
	 */
	private TermAnnotation retrieve(JCas cas, int type, String term) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			Annotation annotation = iterator.next();
			if (annotation.getCoveredText().equals(term)) {
				return (TermAnnotation) annotation;
			}
		}
		return null;
	}
		
	/**
	 * provides the term components of a given term
	 * if these components cover the term completely.
	 * 
	 * @param cas
	 * @param annotation
	 * @return
	 * @throws Exception
	 */
	private List<String> extract(JCas cas, TermAnnotation annotation, boolean check) {
		List<String> components = new ArrayList<String>();
		List<TermComponentAnnotation> subAnnotations = new ArrayList<TermComponentAnnotation>();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		int offset = annotation.getBegin();
		while (iterator.hasNext()) {
			TermComponentAnnotation subAnnotation = (TermComponentAnnotation) iterator.next();
			if (check && subAnnotation.getBegin() != offset) {
				// throw new Exception(annotation.getCoveredText());
			}
			offset = subAnnotation.getEnd();
			subAnnotations.add(subAnnotation);
		}
		if (check && offset != annotation.getEnd()) {
			// throw new Exception(annotation.getCoveredText() + " " + annotation.getEnd() + " != " + offset);
		}
		for (TermComponentAnnotation a : subAnnotations) {
			components.add(a.getCoveredText());
			// TODO lemma
		}
		return components;
	}
		
	/**
	 * checks if all components belong to the dictionary 
	 * or reduce the component set by merging the last two components otherwise
	 * 
	 * @param components

	 * @return
	 * @throws Exception
	 */
	private List<String> reshape(List<String> components) {
		if (components == null || components.isEmpty() || components.size() == 1) {
			return Collections.emptyList();
		} else {
			int length = components.size();
			for (int index = 0; index < length; index++) {
				String component = components.get(index);
				if (this.getDictionary().get().keySet().contains(component)) {
					continue;
				} else if (index > 1) {
					String prefix = components.remove(index - 2);
					String suffix = components.remove(index - 1);
					components.add(prefix + suffix);
					return this.reshape(components);
				} else {
					return Collections.emptyList();
				}
			}
		return components;
		}
	}
	
	/**
	 * provides the term component dictionary entries 
	 * from a term defined by its components. 
	 * 
	 * @param components
	 * @return
	 */
	private List<List<String>> transfer(JCas cas, List<String> components) {
		List<List<String>> candidates = new ArrayList<List<String>>();
		for (int index = 0; index < components.size(); index++) {
			String component = components.get(index);
			Set<String> candidate = this.getDictionary().get().get(component);
			if (candidate == null) {
				candidate = new HashSet<String>();
			}
			if (candidate.isEmpty() && this.distributional()) {
				this.alignComponent(cas, component, candidate);
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
	 * provides the translated term component combinations 
	 * from term component translations.
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
	private List<String> get(List<List<String>> components, int[]indexes) {
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
	 * provides the translated term 
	 * from translated term component combinations 
	 *   
	 * @param components
	 * @param glues
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
	private List<String> select(List<String> candidates) {
		List<String> selection = new ArrayList<String>();
		for (String candidate : candidates) {
			if (this.getTargetTerminology().exists(candidate)) {
				selection.add(candidate);
			}
		}
		return selection;
	}

	/**
	 * inserts one annotation per candidate of neoclassical compound.
	 * 
	 * @param cas
	 * @param candidates
	 */
	private void annotate(JCas cas, List<String> candidates) {
		for (int index = 0; index < candidates.size(); index++) {
			String candidate = candidates.get(index);
			TranslationCandidateAnnotation annotation = new TranslationCandidateAnnotation(cas,0,cas.getDocumentText().length());
			annotation.setTranslation(candidate);
			annotation.setScore(1.0);
			annotation.setRank(index);
			annotation.addToIndexes();
		}
	}
	
	private class ScoreComparator implements Comparator<String> {

		private Map<String, Double> map;
		
		public void set(Map<String, Double> map) {
			this.map = map;
		}
		
		@Override
		public int compare(String sourceKey,String targetKey) {
			Double sourceValue = this.map.get(sourceKey);
			Double targetValue = this.map.get(targetKey);
			if (sourceValue == null && targetValue == null) {
				return sourceKey.compareTo(targetKey);
			} else if (sourceValue == null) {
				return -1;
			} else if (targetValue == null) {
				return 1;
			} else {
				return targetValue.compareTo(sourceValue);
			}
		}
		
	}
	
}
