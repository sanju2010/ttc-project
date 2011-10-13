package fr.univnantes.lina.uima.dictionaries;

public class DictionaryEntry implements Entry {

	private String sourceLanguage;
	
	public void setSourceLanguage(String language) {
		this.sourceLanguage = language;
	}

	@Override
	public String getSourceLanguage() {
		return this.sourceLanguage;
	}

	private String targetLanguage;
	
	public void setTargetLanguage(String language) {
		this.targetLanguage = language;
	}

	@Override
	public String getTargetLanguage() {
		return this.targetLanguage;
	}
	
	private String sourceEntry;
	
	public void setSourceEntry(String entry) {
		this.sourceEntry = entry;
	}

	@Override
	public String getSourceEntry() {
		return this.sourceEntry;
	}

	private String targetEntry;
	
	public void setTargetEntry(String entry) {
		this.targetEntry = entry;
	}

	@Override
	public String getTargetEntry() {
		return this.targetEntry;
	}

	private String sourceCategory;
	
	public void setSourceCategory(String language) {
		this.sourceCategory = language;
	}

	@Override
	public String getSourceCategory() {
		return this.sourceCategory;
	}

	private String targetCategory;
	
	public void setTargetCategory(String language) {
		this.targetCategory = language;
	}

	@Override
	public String getTargetCategory() {
		return this.targetCategory;
	}
	
	@Override
	public int compareTo(Entry entry) {
		int diff = this.getSourceLanguage().compareTo(entry.getSourceLanguage());
		if (diff == 0) {
			diff = this.getTargetLanguage().compareTo(entry.getTargetLanguage());
			if (diff == 0) {
				diff = this.getSourceEntry().compareTo(entry.getSourceEntry());
				if (diff == 0) {
					diff = this.getTargetEntry().compareTo(entry.getTargetEntry());
					if (diff == 0) {
						diff = this.getSourceCategory().compareTo(entry.getSourceCategory());
						if (diff == 0) {
							return this.getTargetCategory().compareTo(entry.getTargetCategory());
						} else {
							return diff;
						}
					} else {
						return diff;
					}
				} else {
					return diff;
				}
			} else {
				return diff;
			}
		} else {
			return diff;
		}		
	}

}
