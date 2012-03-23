package alignment.methods;

import java.util.List;
import java.util.Set;

public interface CompositionalMethod {

	/**
	 * This method provides the list of translations 
	 * for every term components of a given list. 
	 * 
	 * @param components a term given by its term component list
	 * @param dictionary a bilingual dictionary
	 * @return the list of term component translations
	 */
	public List<Set<String>> map(List<TermComponent> components, BilingualDictionary dictionary);
	
	/**
	 * This method provides the translation candidates of a given term
	 * from its component translations.  
	 * 
	 * @param translations the list of component translations
	 * @return the translation candidates of 
	 */
	public Set<List<TermComponent>> combine(List<Set<String>> translations);

	/**
	 * This method removes wrong term translation candidates 
	 * from a set of translation candidates. 
	 * 
	 * @param candidates a translation candidate set of a term given by its component list
	 * @return the given candidates without the wrong ones
	 */
	public Set<List<TermComponent>> reduce(Set<List<TermComponent>> candidates);

	
}
