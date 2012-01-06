package eu.project.ttc.engines;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
import eu.project.ttc.types.SimilarityAnnotation;
import eu.project.ttc.types.VectorAnnotation;
import fr.univnantes.lina.uima.dictionaries.Dictionary;

public class MimesisAnalysisEngine extends JCasAnnotator_ImplBase {
	
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
	
	private TermContextIndex index;
	
	private void setIndex(TermContextIndex index) {
		this.index = index;
	}
	
	private TermContextIndex getIndex() {
		return this.index;
	}
	
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	private Dictionary getDictionary() {
		return this.dictionary;
	}
	
	private void shrink(boolean reverse) {
		Set<String> filter = new HashSet<String>();
		if (reverse) {
			Collection<Set<String>> filters = this.getDictionary().map().values();
			for (Set<String> f : filters) {
				filter.addAll(f);
			}
		} else {
			filter.addAll(this.getDictionary().map().keySet());
		}
		this.getIndex().doShrink(filter);				
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getSimilarityDistance() == null) {
				String name = (String) context.getConfigParameterValue("SimilarityDistanceClassName");
				this.setSimilarityDistance(name);				
			}
			
			if (this.getIndex() == null) {
				TermContextIndex index = (TermContextIndex) context.getResourceObject("Index");
				this.setIndex(index);
				String path = (String) context.getConfigParameterValue("File");
				if (path != null) {
					File file = new File(path);
					this.getContext().getLogger().log(Level.INFO,"Loading Term Context Index from " + path);
					this.getIndex().doLoad(file.toURI());
				}				
			}
			
			if (this.getDictionary() == null) {
				Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
				this.setDictionary(dictionary);				
				String path = (String) context.getConfigParameterValue("DictionaryFile");
				if (path != null) {
					File file = new File(path);
					this.getContext().getLogger().log(Level.INFO,"Loading Dictionary from " + path);
					this.getDictionary().doLoad(file.toURI());
				}
				Boolean reverse = (Boolean) context.getConfigParameterValue("Reverse");
				this.getContext().getLogger().log(Level.INFO,"Shrinking Index " + (reverse == null ? "Domain" : reverse ? "CoDomain" : "Domain"));
				this.shrink(reverse == null ? false : reverse.booleanValue());
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			String language = cas.getDocumentLanguage();
			if (this.getIndex().getLanguage().equals(language)) {
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(VectorAnnotation.type);
				FSIterator<Annotation> iterator = index.iterator();
				while (iterator.hasNext()) {
					VectorAnnotation annotation = (VectorAnnotation) iterator.next();
					String item = annotation.getItem();
					TermContext first = this.getIndex().getTermContexts().get(item);
					for (String key : this.getIndex().getTermContexts().keySet()) {
						if (key.equals(item)) {
							continue;
						} else {
							TermContext second = this.getIndex().getTermContexts().get(key);
							double score = this.getSimilarityDistance().getValue(first.getCoOccurrences(), second.getCoOccurrences());
							if (!Double.isInfinite(score) && !Double.isNaN(score) && score != 0.0) {
								SimilarityAnnotation sim = new SimilarityAnnotation(cas, annotation.getBegin(), annotation.getEnd());
								sim.setSource(item);
								sim.setTarget(key);
								sim.setScore(score);
								sim.setDistance(this.getSimilarityDistance().getClass().getSimpleName().toLowerCase());
								sim.addToIndexes();
							}
						}
					}
				}				
			} else {
				throw new Exception("Language Clash between CAS = " + language + " and INDEX = " + this.getIndex().getLanguage());
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
		
}
