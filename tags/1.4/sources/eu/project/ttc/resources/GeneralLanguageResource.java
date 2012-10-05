package eu.project.ttc.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

public class GeneralLanguageResource implements GeneralLanguage {

	private int frequency;
	
	private Map<String, Integer> frequencies;
	
	public GeneralLanguageResource() {
		this.frequency = 0;
		this.frequencies = new HashMap<String, Integer>();
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		try {
			Scanner scanner = new Scanner(data.getInputStream());
			scanner.useDelimiter("\n");
			int index = 0;
			while (scanner.hasNext()) {
				index++;
				String line = scanner.next();
				String[] items = line.split("::");
				if (items.length == 3) {
					String key = items[0].trim();
					Integer value = Integer.valueOf(items[2].trim());
					this.set(key.toLowerCase(), value.intValue());
				} else {
					throw new IOException("Wrong general language format at line " + index + ": " + line);
				}
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void set(String entry, int frequency) {
		this.frequency += frequency;
		this.frequencies.put(entry, new Integer(frequency));
	}

	@Override
	public double get(String entry) {
		Integer frequency = this.frequencies.get(entry.toLowerCase());
		double quotient = new Double(this.frequency).doubleValue();
		if (frequency == null) {
			return 1.0 / quotient;
		} else {
			double freq = new Double(frequency.intValue()).doubleValue();
			return freq / quotient;
		}
	}

}
