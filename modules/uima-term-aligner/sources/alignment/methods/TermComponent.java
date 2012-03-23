package alignment.methods;

public interface TermComponent {
	
	/**
	 * This method provides the text of this term component.. 
	 * 
	 * @return the term component text
	 */
	public String getText();
	
	/**
	 * This method provides the grammatical category of this term component.. 
	 * 
	 * @return the term component category
	 */
	public String getCategory();
	
	/**
	 * This method provides the lemma of this term component.. 
	 * 
	 * @return the term component lemma
	 */
	public String getLemma();
	
}
