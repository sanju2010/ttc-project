package fr.univnantes.lina.uima.dictionaries;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import fr.univnantes.lina.uima.dictionaries.DictionaryEntry;
import fr.univnantes.lina.uima.dictionaries.Entry;

public class Converter {
	
	private String source;
	
	private String target;
	
	private Set<Entry> entries;
	
	public Converter() {
		entries = new HashSet<Entry>();
	}
	
	public void doStore(OutputStream outputStream) throws IOException {
		StringBuilder string = new StringBuilder();
		for (Entry entry : entries) {
			string.append(entry.getSourceEntry());
			string.append("::");
			string.append(entry.getSourceCategory() == null ? "noun" : entry.getSourceCategory());
			string.append("::");
			string.append(entry.getSourceLanguage());
			string.append("-");
			string.append(entry.getTargetLanguage());
			string.append("::");
			string.append(entry.getTargetEntry());
			string.append("::");
			string.append(entry.getTargetCategory() == null ? "noun" : entry.getTargetCategory());
			string.append("\n");
		}
		outputStream.write(string.toString().getBytes());
	}
	
	public void doLoad(InputStream inputStream) throws IOException {
		Scanner scanner = new Scanner(inputStream);
		scanner.useDelimiter("\n");
		int index = 0;
		while (scanner.hasNext()) {
			index++;
			String entry = scanner.next();
			String[] items = entry.split("::");
			if (items.length == 5) {
				String targetEntry = items[0].trim();
				String targetCategory = items[1].trim();
				String sourceEntry = items[3].trim();
				String sourceCategory = items[4].trim();
				DictionaryEntry dicoEntry = new DictionaryEntry();
				dicoEntry.setSourceLanguage(this.source);
				dicoEntry.setTargetLanguage(this.target);
				dicoEntry.setSourceEntry(sourceEntry);
				dicoEntry.setTargetEntry(targetEntry);
				dicoEntry.setSourceCategory(sourceCategory);
				dicoEntry.setTargetCategory(targetCategory);
				this.entries.add(dicoEntry);
				
			} else {
				throw new IOException("At line " + index + ": " + entry);
			}
		}
	}
	
	public void run(String source,String target,String input,String output) throws IOException {
		this.source = source;
		this.target = target;
		InputStream inputStream = new FileInputStream(input);
		doLoad(inputStream);
		OutputStream outputStream = new FileOutputStream(output);
		doStore(outputStream);
		inputStream.close();
		outputStream.close();
	}
	
	public static void main(String[] arguments) {
		try {
			int mode = 0;
			String source = null;
			String target = null;
			String input = null;
			String output = null;
			Converter convert = new Converter();
			for (String argument : arguments) {
				if (mode == 0) {
					if(argument.equals("--input")) {
						mode = 1;
					} else if (argument.equals("--output")) {
						mode = -1;
					} else if (argument.equals("--source")) {
						mode = 2;
					} else if (argument.equals("--target")) {
						mode = -2;
					}
				} else if (mode == 1) {
					mode = 0;
					input = argument;
				} else if (mode == -1) {
					mode = 0;
					output = argument;
				} else if (mode == 2) {
					mode = 0;
					source = argument;
				} else if (mode == -2) {
					mode = 0;
					target = argument;
				}
			}
			if (mode == 0 && source != null && target != null && input != null && output != null) { 
				convert.run(source,target,input,output);
			} else {
				String message = "Usage: convert --source <lang> --target <lang> --input <file> --output <file>";
				throw new IOException(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
