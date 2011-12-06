package eu.project.ttc.engines;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.metrics.SimilarityDistance;
import eu.project.ttc.models.TermContext;
import eu.project.ttc.models.TermContextIndex;
import eu.project.ttc.types.CandidateAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import fr.univnantes.lina.uima.dictionaries.Dictionary;

public class ZigguratAnalysisEngine extends JCasAnnotator_ImplBase {
	
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
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
				// UIMAFramework.getLogger().log(Level.INFO,"Setting Similarity Distance: " + this.similarityDistance.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + SimilarityDistance.class.getCanonicalName());
			}
		}
	}
	
	private SimilarityDistance getSimilarityDistance() {
		return this.similarityDistance;
	}
	
	private TermContextIndex sourceIndex;
	
	private void setSourceIndex(TermContextIndex index) {
		this.sourceIndex = index;
	}
	
	private TermContextIndex getSourceIndex() {
		return this.sourceIndex;
	}
	
	private TermContextIndex targetIndex;
	
	private void setTargetIndex(TermContextIndex index) {
		this.targetIndex = index;
	}
	
	private TermContextIndex getTargetIndex() {
		return this.targetIndex;
	}
	
	private Map<Double, String> candidates;
	
	private void setCandidtates() {
		this.candidates = new TreeMap<Double, String>(new ScoreComparator());
	}
	
	private Map<Double, String> getCandidates() {
		return this.candidates;
	}
	
	private static boolean shrinked = false;
	
	private void doShrink() {
		if (!shrinked) {
			shrinked = true;
			Set<String> sourceFilter = this.getDictionary().map().keySet();
			this.getSourceIndex().doShrink(sourceFilter);
	        Collection<Set<String>> targetFilters = this.getDictionary().map().values();
	        Set<String> targetFilter = new HashSet<String>();
	        for (Set<String> filter : targetFilters) {
	                targetFilter.addAll(filter);
	        }
	        this.getTargetIndex().doShrink(targetFilter);			
		}
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			this.setCandidtates();
			String name = (String) context.getConfigParameterValue("SimilarityDistanceClassName");
			this.setSimilarityDistance(name);
			Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
			this.setDictionary(dictionary);
			TermContextIndex sourceIndex = (TermContextIndex) context.getResourceObject("SourceIndex");
			this.setSourceIndex(sourceIndex);
			TermContextIndex targetIndex = (TermContextIndex) context.getResourceObject("TargetIndex");
			this.setTargetIndex(targetIndex);
			String path = (String) context.getConfigParameterValue("DictionaryFile");
			if (path != null) {
				File file = new File(path);
				this.getDictionary().doLoad(file.toURI());
			}
			this.doShrink();
			String sourceIndexFile = (String) context.getConfigParameterValue("SourceTermContextIndexFile");
			if (sourceIndexFile != null) {
				File file = new File(sourceIndexFile);
				this.getSourceIndex().doLoad(file.toURI());
			}
			String targetIndexFile = (String) context.getConfigParameterValue("TargetTermContextIndexFile");
			if (targetIndexFile != null) {
				File file = new File(targetIndexFile);
				this.getTargetIndex().doLoad(file.toURI());
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			String term = cas.getDocumentText();
			String language = cas.getDocumentLanguage();
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			if (iterator.hasNext()) {
				TermAnnotation annotation = (TermAnnotation) iterator.next();
				if (this.getSourceIndex().getLanguage().equals(language)) {
					TermContext context = this.getSourceIndex().getTermContexts().get(term);
					if (context == null) {
						UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + term +  " as it doesn't belong to the source index");
					} else if (annotation instanceof SingleWordTermAnnotation) {
						TermContext transfer = this.transfer(term, context);
						this.align(term, transfer);
						this.annotate(cas);
					} else {
						UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + term + " because of method not yet implemented.");
					}
				} else {
					UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + term +  " because of language clash between " + language + " and " + this.getSourceIndex().getLanguage());
				}
			} else {
				UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + term +  " because it isn't a term.");
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private TermContext transfer(String sourceTerm, TermContext sourceContext) {
		TermContext termContext = new TermContext();
		for (String sourceCoTerm : sourceContext.getCoOccurrences().keySet()) {
			Double sourceCoOcc = sourceContext.getCoOccurrences().get(sourceCoTerm);
			Set<String> resultTerms = this.getDictionary().map().get(sourceCoTerm);
			if (resultTerms == null) { 
				// TODO
			} else {
				int totalOcc = 0;
				for (String resultTerm : resultTerms) {
					Integer occ = this.getTargetIndex().getOccurrences().get(resultTerm);
					totalOcc += occ == null ? 0 : occ.intValue();
				}
				for (String resultTerm : resultTerms) {
					Integer resultOcc = this.getTargetIndex().getOccurrences().get(resultTerm);
					int candidateOcc = resultOcc == null ? 0 : resultOcc.intValue(); 
					double score = totalOcc == 0.0 ? 0.0 : (sourceCoOcc * candidateOcc) / totalOcc;
					if (score != 0.0) {
						termContext.setCoOccurrences(resultTerm, score, TermContext.MAX_MODE);
					}
				}
			}
		}
		return termContext;
	}
	
	private void align(String term, TermContext termContext) {
		this.getCandidates().clear();
		for (String targetTerm : this.getTargetIndex().getTermContexts().keySet()) {
			TermContext targetContext = this.getTargetIndex().getTermContexts().get(targetTerm);
			double score = this.getSimilarityDistance().getValue(termContext.getCoOccurrences(),targetContext.getCoOccurrences());
			if (!Double.isInfinite(score) && !Double.isNaN(score)) {
				this.getCandidates().put(new Double(score), targetTerm);
			}
		}
	}
	
	private void annotate(JCas cas) {
		int rank = 0;
		for (Double score : this.getCandidates().keySet()) {
			rank++;
			String translation = this.getCandidates().get(score);
			CandidateAnnotation annotation = new CandidateAnnotation(cas,0,cas.getDocumentText().length());
			annotation.setTranslation(translation);
			annotation.setScore(score.doubleValue());
			annotation.setRank(rank);
			annotation.addToIndexes();
			if (rank >= 100) {
				break;
			}
		}
	}
	
	private List<Set<String>> transfer(List<String> components) {
		List<Set<String>> candidates = new ArrayList<Set<String>>();
		for (String component : components) {
			Set<String> candidate = this.getDictionary().map().get(component);
			candidates.add(candidate);
		}
		return candidates;
	}
	
	private Set<List<String>> combine(List<Set<String>> components) {
		Set<List<String>> combinaisons = new HashSet<List<String>>();
		// TODO
		return combinaisons;
	}
	
	private Set<List<String>> select(Set<List<String>> components) {
		for (List<String> component : components) {
			// TODO
		}
		return components;
	}
	
	private class ScoreComparator implements Comparator<Double> {

		@Override
		public int compare(Double sourceKey,Double targetKey) {
			return targetKey.compareTo(sourceKey);
		}
		
	}
	
}
