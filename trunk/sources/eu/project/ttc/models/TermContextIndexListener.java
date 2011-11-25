package eu.project.ttc.models;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.metrics.AssociationRate;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermContextAnnotation;
import eu.project.ttc.types.VectorAnnotation;
import fr.free.rocheteau.jerome.models.IndexListener;

public class TermContextIndexListener implements IndexListener, TermContextIndex /* Comparable<TermContextIndex> */ {
	
	private String language;
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	private Map<String,Integer> occurrences;
	
	private void setOccurrences() {
		this.occurrences = new HashMap<String, Integer>();
	}
	
	public Map<String,Integer> getOccurrences() {
		return this.occurrences;
	}	
	
	private Map<String,TermContext> termContexts;
	
	private void setTermContexts() {
		this.termContexts = new HashMap<String,TermContext>();
	}
	
	public Map<String,TermContext> getTermContexts() {
		return this.termContexts;
	}
	
	public TermContextIndexListener() {
		this.setTermContexts();
		this.setOccurrences();
		this.setLoaded();
	}
	
	public void doShrink(Set<String> terms) {
		for (String term : this.getTermContexts().keySet()) {
			TermContext context = this.getTermContexts().get(term);
			Set<String> selected = new HashSet<String>();
			for (String coTerm : context.getCoOccurrences().keySet()) {
				if (!terms.contains(coTerm)) {
					selected.add(coTerm);
				}
			}
			for (String coTerm : selected) {
				context.getCoOccurrences().remove(coTerm);
			}			
		}
	}
	
	public void addOccurrences(String term,Integer occurrences) {
		Integer occ = this.occurrences.get(term);
		if (occ == null) {
			occ = new Integer(0);
		} 
		if (occurrences == null) {
			occ = new Integer(occ.intValue() + 1);				
		} else {
			occ = new Integer(occ.intValue() + occurrences.intValue());
		}
		this.occurrences.put(term, occ);
	}
	
	public void setOccurrences(String term,Integer occurrences) {
		this.occurrences.put(term,occurrences);
	}
	
	public void setCoOccurrences(String term,String context,Double coOccurrences,int mode) {
		TermContext termContext = this.getTermContexts().get(term);
		if (termContext == null) {
			termContext = new TermContext();
			this.getTermContexts().put(term,termContext);
		}
		termContext.setCoOccurrences(context, coOccurrences, mode);
	}
	
	public void addCoOccurrences(String term,String context,Double coOccurrences) {
		TermContext termContext = this.getTermContexts().get(term);
		if (termContext == null) {
			termContext = new TermContext();
			this.getTermContexts().put(term,termContext);
		}
		termContext.setCoOccurrences(context, coOccurrences, TermContext.ADD_MODE);
	}

	private AssociationRate associationRate;
	
	private void setAssociationRate(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (this.associationRate == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof AssociationRate) {
				this.associationRate = (AssociationRate) obj;
				UIMAFramework.getLogger().log(Level.INFO,"Setting Association Rate: " + this.associationRate.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + AssociationRate.class.getCanonicalName());
			}
		}
	}
	
	private AssociationRate getAssociationRate() {
		return this.associationRate;
	}
	
	@Override
	public void load(DataResource aData) throws ResourceInitializationException { }
	
	private Set<String> loaded;
	
	private void setLoaded() {
		this.loaded = new HashSet<String>();
	}
	
	private Set<String> getLoaded() {
		return this.loaded;
	}
	
	private boolean isLoaded(URI uri) {
		return this.loaded.contains(uri.toString());
	}
	
	@Override
	public void doLoad(URI uri) throws ResourceInitializationException {
		try {
			if (!this.isLoaded(uri)) {
				this.getLoaded().add(uri.toString());
				UIMAFramework.getLogger().log(Level.OFF, "Loading " + uri);
				InputStream inputStream = uri.toURL().openStream();
				URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/TermContextIndexWriter.xml");
				XMLInputSource source = new XMLInputSource(url);
				XMLParser parser = UIMAFramework.getXMLParser();
				AnalysisEngineDescription ae = parser.parseAnalysisEngineDescription(source); 
				CAS cas = CasCreationUtils.createCas(ae);
				XmiCasDeserializer.deserialize(inputStream, cas);
				this.load(cas.getJCas());				
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	private void load(JCas cas) throws Exception {
		this.setLanguage(cas.getDocumentLanguage());
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(VectorAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			VectorAnnotation annotation = (VectorAnnotation) iterator.next();
			String term = annotation.getItem();
			Integer occurrences = annotation.getFrequency();
			this.setOccurrences(term, occurrences);
			String context = annotation.getCoveredText();
			String[] scores = context.split(":");
			for (String score : scores) {
				String[] items = score.trim().split("#");
				if (items.length == 2) {
					String coTerm = items[0].trim(); 
					Double coOccurrences = Double.valueOf(items[1].trim());
					this.setCoOccurrences(term, coTerm, coOccurrences, TermContext.DEL_MODE);
				}
			}
		}
	}
	
	/**
	 * The priority of this listener against the others.
	 */
	@Override
	public double priority() {
		return 0.5;
	}

	/**
	 * 
	 */
	@Override
	public void configure(UimaContext context) throws ResourceInitializationException {
		try {
			this.enableReleased(false);
			String language = (String) context.getConfigParameterValue("Language");
			this.setLanguage(language);
			String className = (String) context.getConfigParameterValue("AssociationRateClassName");
			this.setAssociationRate(className);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void update(Annotation annotation) throws AnalysisEngineProcessException { 
		if (annotation instanceof TermContextAnnotation) {
			TermContextAnnotation vector = (TermContextAnnotation) annotation; 
			int length = vector.getContext().size();
			String term = vector.getTerm().getLemma();
			this.addOccurrences(term, null);
			for (int i = 0; i < length; i++) {
				TermAnnotation entry = vector.getContext(i);
				String context = entry.getLemma();
				this.addCoOccurrences(term, context, new Double(1));
			}
		}
	}
	
	private boolean realeased = false;

	private void enableReleased(boolean enabled) {
		this.realeased = enabled;
	}
	
	private boolean isReleased() {
		return this.realeased;
	}
	
	@Override
	public void release(JCas cas) throws AnalysisEngineProcessException {
		try {
			if (this.isReleased()) {
				return;
			} else {
				this.enableReleased(true);
				Locale locale = new Locale(this.getLanguage());
				UIMAFramework.getLogger().log(Level.INFO,"Normalizing " + locale.getDisplayLanguage(Locale.ENGLISH) + " Term Context Index");
				this.normalize(this.getAssociationRate());
				UIMAFramework.getLogger().log(Level.INFO,"Annotating " + locale.getDisplayLanguage(Locale.ENGLISH) + " Term Context Index");
				this.annotate(cas);
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private void annotate(JCas cas) {
		cas.setDocumentLanguage(this.getLanguage());
		StringBuilder builder = new StringBuilder();
		Map<String, TermContext> contexts = new TreeMap<String, TermContext>();
		contexts.putAll(this.getTermContexts());
		for (String item : contexts.keySet()) {
			int begin = builder.length();
			Integer frequency = this.getOccurrences().get(item);
			TermContext context = contexts.get(item);
			builder.append(context.toString());
			int end = builder.length();
			builder.append('\n');
			VectorAnnotation annotation = new VectorAnnotation(cas,begin,end);
			annotation.setItem(item);
			annotation.setFrequency(frequency.intValue());
			annotation.addToIndexes();
		}
		cas.setDocumentText(builder.toString());
	}
	
	private void normalize(AssociationRate rate) throws Exception {
		CrossTable crossTable = new CrossTable();
		crossTable.setIndex(this);
		for (String term : this.getTermContexts().keySet()) {
			TermContext context = this.getTermContexts().get(term);
			crossTable.setTerm(term);
			for (String coTerm : context.getCoOccurrences().keySet()) {
				crossTable.setCoTerm(coTerm);	
				crossTable.compute();
				int a = crossTable.getA();
				int b = crossTable.getB();
				int c = crossTable.getC();
				int d = crossTable.getD();
				double norm = rate.getValue(a, b, c, d);
				this.setCoOccurrences(term, coTerm, new Double(norm), TermContext.DEL_MODE);
			}
		}
	}
	
}