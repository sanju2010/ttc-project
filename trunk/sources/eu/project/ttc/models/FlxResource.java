package eu.project.ttc.models;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class FlxResource implements SharedResourceObject {

	private Map<String, Integer> frequencies;
		
	private Map<String, Map<String, Integer>> occurrences;
	
	public FlxResource() {
		this.frequencies = new HashMap<String, Integer>();
		this.occurrences = new HashMap<String, Map<String, Integer>>();
	}
	
	public void index(String word,String tag,String lemma) {
		if (tag == null || (!tag.equals("noun") && !tag.equals("verb") && !tag.equals("adjective"))) {
			return;
		}
		String tab = lemma + " " + tag;
		Integer frequency = this.frequencies.get(tab);
		int value = 1;
		if (frequency != null) {
			value += frequency.intValue();
		}
		this.frequencies.put(tab, new Integer(value));
		Map<String, Integer> occurrences = this.occurrences.get(tab);
		if (occurrences == null) {
			occurrences = new HashMap<String, Integer>();
			occurrences.put(word, new Integer(1));
			this.occurrences.put(tab, occurrences);
		} else {
			Integer occurrence = occurrences.get(word);
			if (occurrence == null) { 
				occurrences.put(word, new Integer(1));
			} else {
				occurrences.put(word, new Integer(occurrence.intValue() + 1));
			}
		}
		
	}
		
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Map<String, Integer> frequencies = new TreeMap<String, Integer>(new TabComparator());
		frequencies.putAll(this.frequencies);
		for (String lemma : frequencies.keySet()) {
			builder.append(lemma);
			builder.append(' ');
			builder.append(frequencies.get(lemma).toString());
			builder.append('\n');
			Map<String, Integer> occurrences = this.occurrences.get(lemma);
			for (String word : occurrences.keySet()) {
				builder.append('\t');
				builder.append(word);
				builder.append(' ');
				builder.append(occurrences.get(word).toString());
				builder.append('\n');	
			}
		}
		return builder.toString();
	}
	
	public void load(DataResource aData) throws ResourceInitializationException { }
	
	private class TabComparator implements Comparator<String> {

		@Override
		public int compare(String sourceKey,String targetKey) {
			Integer sourceValue = frequencies.get(sourceKey);
			Integer targetValue = frequencies.get(targetKey);
			int source = sourceValue == null ? 0 : sourceValue.intValue();
			int target = targetValue == null ? 0 : targetValue.intValue();
			if (source < target) {
				return 1;
			} else if (source > target) {
				return -1;
			} else {
				return sourceKey.compareTo(targetKey);
			}
		}
	}

	public void release(OutputStream stream) throws IOException {
		String string = this.toString();
		stream.write(string.getBytes());
	}
	
}
