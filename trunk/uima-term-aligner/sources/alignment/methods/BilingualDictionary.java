package alignment.methods;

import java.util.Set;

public interface BilingualDictionary {

	/**
	 * This method looks up in this dictionary and provides all 
	 * bindings for a given entry.
	 * 
	 * NOTE: I points out that rule systems can be seen as dictionaries
	 * because it also provides zero, one or more alternatives 
	 * for a given source term components whereas bilingual dictionaries
	 * provides zero, one or more translations of a given source term component. 
	 * But their signatures are the same... 
	 * 
	 * @param entry an entry
	 * @return the dictionary bindings of the entry
	 */
	public Set<String> get(String entry);
	
}
