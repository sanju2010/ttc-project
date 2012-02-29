package eu.project.ttc.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
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
	
	public String source() {
		return this.source;
	}
	
	private String target;
	
	public String target() {
		return this.target;
	}
	
	private Map<String, Set<String>> entries;
	
	private void set() {
		this.entries = new HashMap<String, Set<String>>();
	}	
	
	@Override
	public Map<String, Set<String>> get() {
		return this.entries;
	}
	
	public DictionaryResource() {
		this.setLoaded();
		this.set();
	}

	@Override
	public void add(String source,String target) {
		Set<String> targetEntries = this.entries.get(source);
		if (targetEntries == null) {
			targetEntries = new HashSet<String>();
			this.entries.put(source, targetEntries);
		}
		targetEntries.add(target);
	}
	
	private Set<Entry<String, String>> parse(InputStream inputStream) throws IOException {
		Set<Entry<String, String>> entries = new HashSet<Entry<String, String>>();
		Scanner scanner = new Scanner(inputStream);
		scanner.useDelimiter(System.getProperty("line.separator"));
		while (scanner.hasNext()) {
			String line = scanner.next();
			String[] items = line.split("\t");
			String source = items[0];
			if (source != null) {
				String target = items[1];
				if (target != null) {
					Entry<String, String> entry = new SimpleEntry<String, String>(source, target);
					entries.add(entry);
				}
			}
		}
		return entries;
	}

	
	@Override
	public void load(URI resourceIdentifier) throws Exception {
		if (!this.isLoaded(resourceIdentifier)) {
			this.getLoaded().add(resourceIdentifier.toString());
			URLConnection connection = resourceIdentifier.toURL().openConnection();
			UIMAFramework.getLogger().log(Level.INFO,"Loading " + resourceIdentifier.getPath());
			InputStream inputStream = connection.getInputStream();
			Set<Entry<String, String>> entries = this.parse(inputStream);
			for (Entry<String, String> entry : entries) {
				String source = entry.getKey();
				String target = entry.getValue();
				this.add(source, target);
			}
		}
	}

	public void load(DataResource data) throws ResourceInitializationException { }

}
