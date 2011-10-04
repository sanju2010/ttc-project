package eu.project.ttc.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.util.Level;

public class FeatureMapping implements SharedResourceObject {

	private Map<String,String> map;
	
	private void setMap() {
		this.map = new TreeMap<String,String>();
	}
	
	private Map<String,String> getMap() {
		return this.map;
	}
	
	public String get(String key) {
		if (key == null) {
			return null;
		} else {
			return this.getMap().get(key);
		}
	}
	
	public FeatureMapping() {
		this.setLoaded(false);
		this.setMap();
	}
	
	private boolean loaded;
	
	private void setLoaded(boolean enabled) {
		this.loaded = enabled;
	}
	
	private boolean isLoaded() {
		return this.loaded;
	}
		
	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		try {
			if (!this.isLoaded()) {
				this.setLoaded(true);
				this.doLoad(data.getInputStream());
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	public void doLoad(InputStream inputStream) throws IOException {
		String delimiter = java.lang.System.getProperty("line.separator");
		Scanner scanner = new Scanner(inputStream);
		scanner.useDelimiter(delimiter);
		while (scanner.hasNext()) {
			try {
				this.doParse(scanner.next());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scanner.close();
	}

	private void doParse(String line) throws Exception {
        String[] items = line.split("\t");
        if (items.length == 2) {
        	String source = items[0];
        	String target = items[1];
        	String old = this.getMap().put(source,target);
        	if (old != null) {
        		UIMAFramework.getLogger().log(Level.WARNING,"Overrding mapping from " + source + " to " + old + " by" + target);
        	}
        } else {
        	throw new IOException("Wrong Multext Mapping format at line: " + line);
        }
    }

}
