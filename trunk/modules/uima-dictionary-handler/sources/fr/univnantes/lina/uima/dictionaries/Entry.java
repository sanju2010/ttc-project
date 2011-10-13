package fr.univnantes.lina.uima.dictionaries;

public interface Entry extends Comparable<Entry> {

	public String getSourceLanguage();
	
	public String getTargetLanguage();
	
	public String getSourceEntry();
	
	public String getTargetEntry();

	public String getSourceCategory();
	
	public String getTargetCategory();
		
}
