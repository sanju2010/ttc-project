package fr.univnantes.lina.uima.dictionaries;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DictionaryFactory {

	public static Set<Entry> doParse(InputStream inputStream) throws IOException {
		Set<Entry> entries = new HashSet<Entry>();
		Scanner scanner = new Scanner(inputStream);
		// scanner.skip("#(.)*");
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] items = line.split("::");
			if (items.length == 5) {
				String sourceEntry = items[0];
				String sourceCategory = items[1];
				if (sourceEntry == null) {
					throw new IOException("Wrong Bilingual Dictionary key at line " + line);
				} else {
					String targetEntry = items[3];
					String targetCategory = items[4];
					if (targetEntry == null) {
						throw new IOException("Wrong Bilingual Dictionary value at line " + line);		
					} else {
						String language = items[2];
						String[] languages = language.split("-");
						if (languages.length == 2) {
							String sourceLanguage = languages[0].toLowerCase();
							String targetLanguage = languages[1].toLowerCase();
							DictionaryEntry entry = new DictionaryEntry();
							entry.setSourceLanguage(sourceLanguage);
							entry.setTargetLanguage(targetLanguage);
							entry.setSourceEntry(sourceEntry);
							entry.setTargetEntry(targetEntry);
							entry.setSourceCategory(sourceCategory);
							entry.setTargetCategory(targetCategory);
							entries.add(entry);
						} else {
							throw new IOException("Wrong Bilingual Dictionary format at line " + line);
						}													
					}
				}
			} else {
				throw new IOException("Wrong Bilingual Dictionary format at line " + line);
			}
		}
		return entries;
	}
	
}
