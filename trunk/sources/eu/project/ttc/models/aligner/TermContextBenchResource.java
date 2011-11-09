package eu.project.ttc.models.aligner;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.TermContextEntryAnnotation;
import eu.project.ttc.types.TermContextIndexAnnotation;

public class TermContextBenchResource implements TermContextBench {

	private Map<String, Map<String, Integer>> frequencies;
	
	private Map<String, Integer> frequency;
	
	private Map<String, List<List<TermContextEntry>>> documents;

	private Map<String, List<TermContextEntry>> currents;
	
	private Map<String, String> names;
	
	@Override
	public void assoc(String language,String name) {
		this.names.put(language, name);
	}
	
	public TermContextBenchResource() {
		this.currents = new HashMap<String, List<TermContextEntry>>();
		this.documents = new HashMap<String, List<List<TermContextEntry>>>();
		this.frequencies = new HashMap<String, Map<String, Integer>>();
		this.names = new HashMap<String, String>();
	}	
	
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		
	}

	@Override
	public synchronized void index(String id,String term, String category, String lemma) {
		List<TermContextEntry> document = this.currents.get(id);
		TermContextEntry entry = new TermContextEntry();
		entry.setTerm(term);
		entry.setCategory(category);
		entry.setLemma(lemma);
		document.add(entry);
		Integer freq = this.frequency.get(lemma);
		if (freq == null) {
			freq = new Integer(1);
		} else {
			freq = new Integer(freq.intValue() + 1);
		}
		this.frequency.put(lemma, freq);
	}
	
	@Override
	public synchronized void start(String id,String language) {
		List<List<TermContextEntry>> documents = this.documents.get(language);
		if (documents == null) {
			documents = new ArrayList<List<TermContextEntry>>();
			this.documents.put(language, documents);
		}
		Map<String, Integer> frequency = this.frequencies.get(language);
		if (frequency == null) {
			frequency = new HashMap<String, Integer>();
			this.frequencies.put(language, frequency);
		}
		this.frequency = frequency;
		this.currents.put(id, new ArrayList<TermContextEntry>());
	}

	@Override
	public synchronized void stop(String id, String language) {
		List<TermContextEntry> document = this.currents.get(id);
		List<List<TermContextEntry>> documents = this.documents.get(language);
		documents.add(document);
		this.currents.remove(id);
	}
	
	private synchronized void clean() {
		for (String language : this.documents.keySet()) {
			List<List<TermContextEntry>> documents = this.documents.get(language);
			Map<String, Integer> frequency = this.frequencies.get(language);			
			for (List<TermContextEntry> document : documents) {
				List<Integer> hapax = new ArrayList<Integer>();
				for (int index = 0; index < document.size(); index++) {
					TermContextEntry entry = document.get(index);
					String lemma = entry.getLemma();
					if (frequency.get(lemma).intValue() == 1) {
						hapax.add(index);
					}
				}
				for (Integer index : hapax) {
					document.remove(index);
				}
				Entry<String, List<TermContextEntry>> entry = new AbstractMap.SimpleEntry<String, List<TermContextEntry>>(language,document);
				this.collection.add(entry);
			}
		}
	}

	private List<Entry<String, List<TermContextEntry>>> collection;
	private Iterator<Entry<String, List<TermContextEntry>>> iterator;
	
	@Override
	public void release() {
		this.collection = new ArrayList<Entry<String, List<TermContextEntry>>>();
		this.clean();
		this.iterator = collection.iterator();
	}
		
	@Override
	public boolean hasNext() {
		if (this.iterator == null) {
			return false;
		} else {
			return this.iterator.hasNext();
		}
	}

	@Override
	public JCas next(JCas cas) {
		Entry<String, List<TermContextEntry>> document = this.iterator.next();
		String language = document.getKey();
		List<TermContextEntry> entries = document.getValue();
		StringBuilder text = new StringBuilder();
		int begin = 0;
		int end = 0;
		for (TermContextEntry entry : entries) {
			String term = entry.getTerm();
			String category = entry.getCategory();
			String lemma = entry.getLemma();
			text.append(term);
			end += term.length();
			text.append(' ');
			TermContextEntryAnnotation annotation = new TermContextEntryAnnotation(cas, begin, end);
			annotation.setCategory(category);
			annotation.setLemma(lemma);
			annotation.addToIndexes();
			begin = text.length();
			end = begin;
		}
		cas.setDocumentText(text.toString());
		cas.setDocumentLanguage(language);
		int length = text.length();
		TermContextIndexAnnotation annotation = new TermContextIndexAnnotation(cas, 0, length);
		annotation.setLanguage(language);
		annotation.setName(this.names.get(language));
		annotation.addToIndexes();
		SourceDocumentInformation info = new SourceDocumentInformation(cas, 0, length);
		info.setLastSegment(this.hasNext());
		info.setDocumentSize(length);
		info.setOffsetInSource(0);
		info.addToIndexes();		
		return cas;
	}
	
	/*
	@Override
	public void update(InputStream inStream) throws Exception {
		UIMAFramework.getLogger().log(Level.INFO,"Updating Term Context Bench");
		this.setTypeSystem();
		this.setTypePriorities();
		this.setCAS();
		XmiCasDeserializer.deserialize(inStream,this.cas,false);
	}
	
	@Override
	public void release(OutputStream outStream) throws Exception {
		this.setTypeSystem();
		this.setTypePriorities();
		this.setCAS();
		Type type = this.cas.getTypeSystem().getType(TermContextEntryAnnotation.class.getCanonicalName());
		int index = 0;
		for (String language : this.documents.keySet()) {
			List<List<String>> documents = this.documents.get(language);
			for (List<String> document : documents) {
				StringBuilder text = new StringBuilder();
				CAS view = this.cas.createView("TermContextIndex-" + index);
				index++;
				int begin = 0;
				int end = 0;
				for (String entry : document) {
					text.append(entry);
					end += entry.length();
					text.append(' ');
					AnnotationFS annotation = view.createAnnotation(type, begin, end);
					view.addFsToIndexes(annotation);
					begin = text.length();
					end = begin;
				}
				view.setDocumentText(text.toString());
				view.setDocumentLanguage(language);
			}
		}
		XmiCasSerializer.serialize(this.cas,this.cas.getTypeSystem(),outStream);
		UIMAFramework.getLogger().log(Level.CONFIG,"Releasing Term Context Bench");
	}
	
	private TypeSystemDescription typeSystem;
	
	private void setTypeSystem() throws IOException, InvalidXMLException, ResourceInitializationException {
		String path = "fr/univnantes/lina/uima/types/TermContextAnnotation.xml";
		URL url = this.getClass().getClassLoader().getResource(path);
		XMLInputSource source = new XMLInputSource(url);
		this.typeSystem = UIMAFramework.getXMLParser().parseTypeSystemDescription(source);
	}
	
	private TypePriorities typePriorities;
	
	private void setTypePriorities() throws IOException, InvalidXMLException, ResourceInitializationException {
		String path = "fr/univnantes/lina/uima/types/TermContextPriorities.xml";
		URL url = this.getClass().getClassLoader().getResource(path);
		XMLInputSource source = new XMLInputSource(url);
		this.typePriorities = UIMAFramework.getXMLParser().parseTypePriorities(source);
	}
	
	private CAS cas;
		
	private void setCAS() throws IOException, InvalidXMLException, ResourceInitializationException {
		this.cas = CasCreationUtils.createCas(this.typeSystem, this.typePriorities, new FsIndexDescription[0]);
	}

	@Override
	public CAS getCAS() {
		return this.cas;
	}

	*/
	
}
