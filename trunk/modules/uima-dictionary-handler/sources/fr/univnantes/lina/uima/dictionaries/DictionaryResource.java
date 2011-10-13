package fr.univnantes.lina.uima.dictionaries;

import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

public class DictionaryResource implements Dictionary {
		
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
	
	private String source;
	
	private String target;
	
	private Map<String,Set<String>> entries;
	
	private void setEntries() {
		this.entries = new HashMap<String, Set<String>>();
	}	
	
	@Override
	public Map<String, Set<String>> map() {
		return this.entries;
	}
	
	@Override
	public Set<String> domain(String language) {
		if (language.equals(this.source)) {
			return this.entries.keySet();
		} else {
			Set<String> set = new HashSet<String>();
			Collection<Set<String>> sets = this.entries.values();
			for (Set<String> s : sets) {
				set.addAll(s);
			}
			return set;
		}
	}
	
	public DictionaryResource() {
		this.setLoaded();
		this.setEntries();
	}

	private void add(String sourceLanguage,String targetLanguage,String sourceEntry,String targetEntry) {
		if (this.source == null && this.target == null) {
			this.source = sourceLanguage;
			this.target = targetLanguage;
		} 
		if (sourceLanguage.equals(this.source) && targetLanguage.equals(this.target)) {
			Set<String> targetEntries = this.entries.get(sourceEntry);
			if (targetEntries == null) {
				targetEntries = new HashSet<String>();
				this.entries.put(sourceEntry, targetEntries);
			}
			targetEntries.add(targetEntry);
		}
	}
	
	@Override
	public void doLoad(URI resourceIdentifier) throws Exception {
		if (!this.isLoaded(resourceIdentifier)) {
			this.getLoaded().add(resourceIdentifier.toString());
			URLConnection connection = resourceIdentifier.toURL().openConnection();
			UIMAFramework.getLogger().log(Level.CONFIG,"Loading Dictionary: " + resourceIdentifier.toString());
			InputStream inputStream = connection.getInputStream();
			Set<Entry> entries = DictionaryFactory.doParse(inputStream);
			for (Entry entry : entries) {
				String sourceEntry = entry.getSourceEntry();
				String targetEntry = entry.getTargetEntry();
				String sourceLanguage = entry.getSourceLanguage();
				String targetLanguage = entry.getTargetLanguage();
				this.add(sourceLanguage, targetLanguage, sourceEntry, targetEntry);
				// this.add(targetLanguage, sourceLanguage, targetEntry, sourceEntry);
			}
		}
	}

	public void load(DataResource data) throws ResourceInitializationException { }

}
