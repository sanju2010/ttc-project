package fr.univnantes.lina.uima.models;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.TermContextEntryAnnotation;
import org.apache.uima.TermContextIndexAnnotation;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

public class TermContextBenchResource implements TermContextBench {

	private Map<String, Map<String, Integer>> frequencies;
	
	private Map<String, Integer> frequency;
	
	private Map<String, List<List<String>>> documents;

	private Map<String, List<String>> currents;
	
	private Map<String, String> names;
	
	@Override
	public void assoc(String language,String name) {
		this.names.put(language, name);
	}
	
	public TermContextBenchResource() {
		this.currents = new HashMap<String, List<String>>();
		this.documents = new HashMap<String, List<List<String>>>();
		this.frequencies = new HashMap<String, Map<String, Integer>>();
		this.names = new HashMap<String, String>();
	}	
	
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		
	}

	@Override
	public synchronized void index(String id,String entry) {
		List<String> doc = this.currents.get(id);
		doc.add(entry);
		Integer freq = this.frequency.get(entry);
		if (freq == null) {
			freq = new Integer(1);
		} else {
			freq = new Integer(freq.intValue() + 1);
		}
		this.frequency.put(entry, freq);
	}
	
	@Override
	public synchronized void start(String document,String language) {
		List<List<String>> docs = this.documents.get(language);
		if (docs == null) {
			docs = new ArrayList<List<String>>();
			this.documents.put(language, docs);
		}
		Map<String, Integer> frequency = this.frequencies.get(language);
		if (frequency == null) {
			frequency = new HashMap<String, Integer>();
			this.frequencies.put(language, frequency);
		}
		this.frequency = frequency;
		this.currents.put(document, new ArrayList<String>());
	}

	@Override
	public synchronized void stop(String document, String language) {
		List<String> doc = this.currents.get(document);
		List<List<String>> docs = this.documents.get(language);
		docs.add(doc);
		this.currents.remove(document);
	}
	
	private void clean() {
		synchronized (this.documents) {
			for (String language : this.documents.keySet()) {
				List<List<String>> documents = this.documents.get(language);
				Map<String, Integer> frequency = this.frequencies.get(language);			
				for (List<String> document : documents) {
					List<Integer> hapax = new ArrayList<Integer>();
					for (int index = 0; index < document.size(); index++) {
						String entry = document.get(index);
						if (frequency.get(entry).intValue() == 1) {
							hapax.add(index);
						}
					}
					for (Integer index : hapax) {
						document.remove(index);
					}
					Entry<String, List<String>> entry = new AbstractMap.SimpleEntry<String, List<String>>(language,document);
					this.collection.add(entry);
				}
			}			
		}
	}

	private List<Entry<String, List<String>>> collection;
	private Iterator<Entry<String, List<String>>> iterator;
	
	@Override
	public void release() {
		this.collection = new ArrayList<Entry<String, List<String>>>();
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
		Entry<String, List<String>> document = this.iterator.next();
		String language = document.getKey();
		List<String> words = document.getValue();
		StringBuilder text = new StringBuilder();
		int begin = 0;
		int end = 0;
		for (String word : words) {
			text.append(word);
			end += word.length();
			text.append(' ');
			TermContextEntryAnnotation entry = new TermContextEntryAnnotation(cas, begin, end);
			entry.addToIndexes();
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
