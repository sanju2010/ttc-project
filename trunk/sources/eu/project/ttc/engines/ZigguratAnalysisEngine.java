package eu.project.ttc.engines;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.xml.sax.SAXException;

import eu.project.ttc.metrics.SimilarityDistance;
import eu.project.ttc.models.TermContext;
import eu.project.ttc.models.TermContextIndex;
import eu.project.ttc.types.CandidateAnnotation;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.NeoClassicalCompoundTermAnnotation;
import eu.project.ttc.types.SimilarityAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
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
				UIMAFramework.getLogger().log(Level.INFO,"Setting Similarity Distance: " + this.similarityDistance.getClass().getSimpleName());
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
	
	private String sourceLanguage;
	
	private void setSourceLanguage(String language) {
		this.sourceLanguage = language;
	}
	
	private String getSourceLanguage() {
		return this.sourceLanguage;
	}
	
	private String targetLanguage;
	
	private void setTargetLanguage(String language) {
		this.targetLanguage = language;
	}
	
	private String getTargetLanguage() {
		return this.targetLanguage;
	}
	
	private CAS sourceTerminology;
	
	private void setSourceTerminology() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/types/TermSuiteTypeSystem.xml");
		XMLInputSource source = new XMLInputSource(url);
		XMLParser parser = UIMAFramework.getXMLParser();
		TypeSystemDescription ts = parser.parseTypeSystemDescription(source); 
		this.sourceTerminology = CasCreationUtils.createCas(ts,null,new FsIndexDescription[0]);
	}
	
	private void setSourceTerminology(File file) throws Exception {
		InputStream stream = new FileInputStream(file);
		Locale locale = new Locale(this.getSourceLanguage());
		String lang = locale.getDisplayLanguage(Locale.ENGLISH);
		UIMAFramework.getLogger().log(Level.INFO, "Loading " + lang + " Terminology");
		XmiCasDeserializer.deserialize(stream, this.sourceTerminology);
	}
	
	private CAS getSourceTerminology() {
		return this.sourceTerminology;
	}
	
	private CAS targetTerminology;
	
	private void setTargetTerminology() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/types/TermSuiteTypeSystem.xml");
		XMLInputSource source = new XMLInputSource(url);
		XMLParser parser = UIMAFramework.getXMLParser();
		TypeSystemDescription ts = parser.parseTypeSystemDescription(source); 
		this.targetTerminology = CasCreationUtils.createCas(ts,null,new FsIndexDescription[0]);
	}
	
	private void setTargetTerminology(File file) throws Exception {	
		InputStream stream = new FileInputStream(file);
		Locale locale = new Locale(this.getTargetLanguage());
		String lang = locale.getDisplayLanguage(Locale.ENGLISH);
		UIMAFramework.getLogger().log(Level.INFO, "Loading " + lang + " Terminology");
		XmiCasDeserializer.deserialize(stream, this.targetTerminology);
		this.setTargetTerms(this.getTargetTerminology().getJCas());
	}
	
	private CAS getTargetTerminology() {
		return this.targetTerminology;
	}

	private Set<String> targetTerms;
	
	private void setTargetTerms() {
		this.targetTerms = new HashSet<String>();
	}
	
	private void setTargetTerms(JCas cas) throws CASRuntimeException, CASException {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		AnnotationIndex<Annotation> idx = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			Annotation annotation = iterator.next();
			if (annotation instanceof SingleWordTermAnnotation) {
				this.targetTerms.add(annotation.getCoveredText());
			} else if (annotation instanceof MultiWordTermAnnotation) {
				StringBuilder builder = new StringBuilder();
				FSIterator<Annotation> it = idx.subiterator(annotation);
				boolean flag = false;
				while (it.hasNext()) {
					if (flag) {
						builder.append(" ");
					} else {
						flag = true;
					}
					TermComponentAnnotation c = (TermComponentAnnotation) it.next();
					builder.append(c.getLemma());
				}
				this.targetTerms.add(builder.toString());
			} else if (annotation instanceof NeoClassicalCompoundTermAnnotation) {
				this.targetTerms.add(annotation.getCoveredText());
			}
		}
	}
	
	private Set<String> getTargetTerms() {
		return this.targetTerms;
	}
	
	private File sourceDirectory;
	
	private void setSourceDirectory(String path) throws IOException {
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			this.sourceDirectory = directory;
		} else {
			throw new IOException(path);
		}
	}
	
	private File getSourceDirectory() {
		return this.sourceDirectory;
	}
	
	private File targetDirectory;
	
	private void setTargetDirectory(String path) throws IOException {
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			this.targetDirectory = directory;
		} else {
			throw new IOException(path);
		}
	}
	
	private File getTargetDirectory() {
		return this.targetDirectory;
	}
	
	private Map<String, Double> getSimilarityOf(String term, boolean src) throws InvalidXMLException, ResourceInitializationException, IOException, SAXException, CASRuntimeException, CASException {
		Map<String, Double> terms = new HashMap<String, Double>();
		File file = new File(src ? this.getSourceDirectory(): this.getTargetDirectory(), term + ".xmi");
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/types/TermSuiteTypeSystem.xml");
		XMLInputSource source = new XMLInputSource(url);
		XMLParser parser = UIMAFramework.getXMLParser();
		TypeSystemDescription ts = parser.parseTypeSystemDescription(source); 
		CAS cas = CasCreationUtils.createCas(ts,null,new FsIndexDescription[0]);
		InputStream stream = new FileInputStream(file);
		UIMAFramework.getLogger().log(Level.INFO, "Loading '" + term + "'");
		XmiCasDeserializer.deserialize(stream, cas);
		AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(SimilarityAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			SimilarityAnnotation annotation = (SimilarityAnnotation) iterator.next();
			if (annotation.getSource().equals(term)) {
				String key = annotation.getTarget();
				Double value = new Double(annotation.getScore());
				terms.put(key, value);
			}
		}
		ScoreComparator comparator = new ScoreComparator();
		comparator.set(terms);
		Map<String, Double> result = new TreeMap<String, Double>(comparator);
		result.putAll(terms);
		return result;
	}
	
	private boolean interlingualSimilarity;
	
	private void enableInterlingualSimilarity(boolean enabled) {
		this.interlingualSimilarity = enabled;
	}
	
	private boolean interlingualSimilarity() {
		return this.interlingualSimilarity;
	}
	
	private int similaritySize;
	
	private void setSimilaritySize(int size) {
		this.similaritySize = size;
	}
	
	private int getSimilaritySize() {
		return this.similaritySize;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getTargetTerms() == null) {
				this.setTargetTerms();				
			}
			
			String sourceLanguage = (String) context.getConfigParameterValue("SourceLanguage");
			this.setSourceLanguage(sourceLanguage);
			
			String targetLanguage = (String) context.getConfigParameterValue("TargetLanguage");
			this.setTargetLanguage(targetLanguage);
			
			String name = (String) context.getConfigParameterValue("SimilarityDistanceClassName");
			this.setSimilarityDistance(name);
			
			if (this.getSourceIndex() == null) {
				TermContextIndex sourceIndex = (TermContextIndex) context.getResourceObject("SourceIndex");
				this.setSourceIndex(sourceIndex);				
				String sourceIndexFile = (String) context.getConfigParameterValue("SourceTermContextIndexFile");
				if (sourceIndexFile != null) {
					File file = new File(sourceIndexFile);
					this.getSourceIndex().doLoad(file.toURI());
				}
			}
			
			if (this.getTargetIndex() == null) {
				TermContextIndex targetIndex = (TermContextIndex) context.getResourceObject("TargetIndex");
				this.setTargetIndex(targetIndex);				
				String targetIndexFile = (String) context.getConfigParameterValue("TargetTermContextIndexFile");
				if (targetIndexFile != null) {
					File file = new File(targetIndexFile);
					this.getTargetIndex().doLoad(file.toURI());
				}
			}
			
			if (this.getSourceTerminology() == null) {
				this.setSourceTerminology();
				String sourceTerminologyFile = (String) context.getConfigParameterValue("SourceTerminologyFile");
				if (sourceTerminologyFile != null) {
					File file = new File(sourceTerminologyFile);
					this.setSourceTerminology(file);
				}				
			}
			
			if (this.getTargetTerminology() == null) {
				this.setTargetTerminology();
				String targetTerminologyFile = (String) context.getConfigParameterValue("TargetTerminologyFile");
				if (targetTerminologyFile != null) {
					File file = new File(targetTerminologyFile);
					this.setTargetTerminology(file);
				}				
			}
			
			if (this.getDictionary() == null) {
				Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
				this.setDictionary(dictionary);				
				String path = (String) context.getConfigParameterValue("DictionaryFile");
				if (path != null) {
					File file = new File(path);
					this.getDictionary().doLoad(file.toURI());
				}
				this.doShrink();
			}
			
			Boolean interlingualSimilarity = (Boolean) context.getConfigParameterValue("InterlingualSimilarity");
			this.enableInterlingualSimilarity(interlingualSimilarity == null ? false : interlingualSimilarity.booleanValue());
			
			if (this.getSourceDirectory() == null) {
				String sourceDirectory = (String) context.getConfigParameterValue("SourceTermSimilarityDirectory");
				if (sourceDirectory != null) {
					this.setSourceDirectory(sourceDirectory);
				}				
			}
			
			if (this.getTargetDirectory() == null) {
				String targetDirectory = (String) context.getConfigParameterValue("TargetTermSimilarityDirectory");
				if (targetDirectory != null) {
					this.setTargetDirectory(targetDirectory);
				}				
			}
			
			Integer similaritySize = (Integer) context.getConfigParameterValue("InterlingualSimilaritySize");
			this.setSimilaritySize(similaritySize == null ? 50 : similaritySize.intValue());
			
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			String term = cas.getDocumentText();
			// String language = cas.getDocumentLanguage();
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			if (iterator.hasNext()) {
				TermAnnotation annotation = (TermAnnotation) iterator.next();
				if (annotation instanceof SingleWordTermAnnotation) {
					/*
					if (!this.getSourceIndex().getLanguage().equals(language)) {
						throw new Exception("Skiping " + term +  " because of language clash between CAS " + language + " and term content index " + this.getSourceIndex().getLanguage());
					} */
					
					if (this.interlingualSimilarity()) {
						this.alignSimilarSingleWordTerm(cas, term, this.getSimilaritySize());
					} else {
						this.alignSingleWordTerm(cas, term);
					}
				} else {
					JCas terminology = this.getSourceTerminology().getJCas();
					/*
					if (!terminology.getDocumentLanguage().equals(language)) {
						throw new Exception("Skiping " + term +  " because of language clash between CAS " + language + " and Terminology " + this.getSourceIndex().getLanguage());
					} 
					*/
					if (annotation instanceof MultiWordTermAnnotation) {
						this.alignMultiWordTerm(cas, terminology, term);
					} else if (annotation instanceof NeoClassicalCompoundTermAnnotation) {
						this.alignNeoClassicalCompound(cas, terminology, term);
					} else {
						String message = "Don't know what to do with " + term + " that all terms of this type: " + annotation.getType();
						UIMAFramework.getLogger().log(Level.WARNING,message);
						// throw new Exception("Don't know what to do with " + term + " that all terms of this type: " + annotation.getType());
					}
				}
			} else {
				UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + term +  " because it isn't a term.");
			}
		} catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (CASRuntimeException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (InvalidXMLException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (ResourceInitializationException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (SAXException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private void alignSimilarSingleWordTerm(JCas cas, String term, int size) throws CASRuntimeException, InvalidXMLException, ResourceInitializationException, CASException, IOException, SAXException {
		Map<String, Double> sim = this.getSimilarityOf(term, true);
		int i = 0;
		Map<String, Double> candidates = new HashMap<String, Double>();
		for (String t : sim.keySet()) {
			TermContext context = this.getSourceIndex().getTermContexts().get(t);
			TermContext transfer = this.transfer(t, context);
			Map<String, Double> c = this.align(term, transfer);
			for (String translation : c.keySet()) {
				Double s = c.get(translation);
				if (candidates.containsKey(translation)) {
					Double score = candidates.get(translation);
					if (score.compareTo(s) < 0) {
						candidates.put(translation, s);
					}
				} else {
					candidates.put(translation, s);
				}
			}
			if (i++ > size) {
				break;
			}
		}
		this.annotate(cas, candidates);
	}

	private void alignSingleWordTerm(JCas cas, String term) {
		TermContext context = this.getSourceIndex().getTermContexts().get(term);
		if (context == null) {
			UIMAFramework.getLogger().log(Level.WARNING,"Skiping " + term +  " as it doesn't belong to the source index");
		} else {
			TermContext transfer = this.transfer(term, context);
			Map<String, Double> candidates = this.align(term, transfer);
			this.annotate(cas, candidates);
		}
	}

	private void alignMultiWordTerm(JCas cas, JCas terminology, String term) throws CASException {
		TermAnnotation termEntry = (MultiWordTermAnnotation) this.retrieve(terminology, MultiWordTermAnnotation.type, term);
		if (termEntry != null) {
			List<String> components = this.extract(this.getSourceTerminology().getJCas(), termEntry, false);
			List<List<String>> candidates = this.transfer(components);
			candidates = this.combine(candidates);
			candidates = this.generate(candidates);
			components = this.flatten(candidates, " ");
			components = this.select(components);
			this.annotate(cas, components);
		}
	}

	private void alignNeoClassicalCompound(JCas cas, JCas terminology, String term) throws CASException {
		try {
		TermAnnotation termEntry = this.retrieve(terminology, NeoClassicalCompoundTermAnnotation.type, term);
		if (termEntry != null) {
			List<String> components = this.extract(this.getSourceTerminology().getJCas(), termEntry, true);
			components = this.reshape(components);
			List<List<String>> candidates = this.transfer(components);
			candidates = this.combine(candidates);
			// TODO generate
			components = this.flatten(candidates, "");
			components = this.select(components);
			this.annotate(cas, components);			
		}
		} catch (Exception e) {
			this.getContext().getLogger().log(Level.WARNING, "Unable to align '" + term +"'");
		}
	}
	
	private TermContext transfer(String sourceTerm, TermContext sourceContext) {
		TermContext termContext = new TermContext();
		for (String sourceCoTerm : sourceContext.getCoOccurrences().keySet()) {
			Double sourceCoOcc = sourceContext.getCoOccurrences().get(sourceCoTerm);
			Set<String> resultTerms = null;
			if (!sourceTerm.equals(sourceCoTerm)) {
				resultTerms = this.getDictionary().map().get(sourceCoTerm);
			}
			if (resultTerms != null) { 
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
	
	private Map<String, Double> align(String term, TermContext termContext) {
		Map<String, Double> candidates = new HashMap<String, Double>();
		for (String targetTerm : this.getTargetIndex().getTermContexts().keySet()) {
			TermContext targetContext = this.getTargetIndex().getTermContexts().get(targetTerm);
			double score = this.getSimilarityDistance().getValue(termContext.getCoOccurrences(),targetContext.getCoOccurrences());
			if (!Double.isInfinite(score) && !Double.isNaN(score)) {
				candidates.put(targetTerm, new Double(score));
			}
		}
		return candidates;
	}
	
	private void annotate(JCas cas, Map<String, Double> scores) {
		try {
		ScoreComparator comparator = new ScoreComparator();
		comparator.set(scores);
		Map<String, Double> candidates = new TreeMap<String, Double>(comparator);
		candidates.putAll(scores);
		int rank = 0;
		for (String translation : candidates.keySet()) {
			rank++;
			Double score = candidates.get(translation);
			CandidateAnnotation annotation = new CandidateAnnotation(cas,0,cas.getDocumentText().length());
			annotation.setTranslation(translation);
			annotation.setScore(score.doubleValue());
			annotation.setRank(rank);
			annotation.addToIndexes();
			if (rank >= 100) {
				break;
			}
		}
		} catch (Throwable t) {
			t.printStackTrace();
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
		// throw new Exception("Term " + term + " not found");
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
				if (this.getDictionary().map().keySet().contains(component)) {
					continue;
				} else if (index > 1) {
					String prefix = components.remove(index - 2);
					String suffix = components.remove(index - 1);
					components.add(prefix + suffix);
					return this.reshape(components);
				} else {
					throw new NullPointerException();
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
	private List<List<String>> transfer(List<String> components) {
		List<List<String>> candidates = new ArrayList<List<String>>();
		for (int index = 0; index < components.size(); index++) {
			String component = components.get(index);
			Set<String> candidate = this.getDictionary().map().get(component);
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
			for (String item : component) {
				candidate.append(item);
				candidate.append(glue);
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
			if (this.getTargetTerms().contains(candidate)) {
				selection.add(candidate);
			}
		}
		return selection;
	}

	/**
	 * inserts one annotation per candidate pf neoclassical compound.
	 * 
	 * @param cas
	 * @param candidates
	 */
	private void annotate(JCas cas, List<String> candidates) {
		for (String candidate : candidates) {
			CandidateAnnotation annotation = new CandidateAnnotation(cas,0,cas.getDocumentText().length());
			annotation.setTranslation(candidate);
			annotation.setScore(1.0);
			annotation.setRank(1);
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
