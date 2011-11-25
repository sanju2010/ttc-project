package eu.project.ttc.models;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.ResourceInitializationException;

public class TermContextIndexResource implements TermContextIndex /* Comparable<TermContextIndex> */ {
	
	private String language;
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#setLanguage(java.lang.String)
	 */
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#getLanguage()
	 */
	@Override
	public String getLanguage() {
		return this.language;
	}
	
	private Map<String,Integer> occurrences;
	
	private void setOccurrences() {
		this.occurrences = new HashMap<String, Integer>();
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#getOccurrences()
	 */
	@Override
	public Map<String,Integer> getOccurrences() {
		return this.occurrences;
	}	
	
	private Map<String,TermContext> termContexts;
	
	private void setTermContexts() {
		this.termContexts = new HashMap<String,TermContext>();
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#getTermContexts()
	 */
	@Override
	public Map<String,TermContext> getTermContexts() {
		return this.termContexts;
	}
	
	public TermContextIndexResource() {
		this.setTermContexts();
		this.setOccurrences();
	}
		
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#doShrink(java.util.Set)
	 */
	@Override
	public void doShrink(Set<String> terms) {
		for (String term : this.getTermContexts().keySet()) {
			TermContext context = this.getTermContexts().get(term);
			Set<String> selected = new HashSet<String>();
			for (String coTerm : context.getCoOccurrences().keySet()) {
				if (!terms.contains(coTerm)) {
					selected.add(coTerm);
				}
			}
			for (String coTerm : selected) {
				context.getCoOccurrences().remove(coTerm);
			}			
		}
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#doFilter(java.util.Set)
	 */
	@Override
	public void doFilter(Set<String> terms) {
		Set<String> selected = new HashSet<String>();
		for (String term : this.getTermContexts().keySet()) {
			if (!terms.contains(term)) {
				selected.add(term);
			}
		}
		for (String term : selected) {
			this.getTermContexts().remove(term);
		}
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#addOccurrences(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void addOccurrences(String term,Integer occurrences) {
		Integer occ = this.occurrences.get(term);
		if (occ == null) {
			occ = new Integer(0);
		} 
		if (occurrences == null) {
			occ = new Integer(occ.intValue() + 1);				
		} else {
			occ = new Integer(occ.intValue() + occurrences.intValue());
		}
		this.occurrences.put(term, occ);
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#setOccurrences(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void setOccurrences(String term,Integer occurrences) {
		this.occurrences.put(term,occurrences);
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#setCoOccurrences(java.lang.String, java.lang.String, java.lang.Double, int)
	 */
	@Override
	public void setCoOccurrences(String term,String context,Double coOccurrences,int mode) {
		TermContext termContext = this.getTermContexts().get(term);
		if (termContext == null) {
			termContext = new TermContext();
			this.getTermContexts().put(term,termContext);
		}
		termContext.setCoOccurrences(context, coOccurrences, mode);
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.models.TermContextIndex#addCoOccurrences(java.lang.String, java.lang.String, java.lang.Double)
	 */
	@Override
	public void addCoOccurrences(String term,String context,Double coOccurrences) {
		TermContext termContext = this.getTermContexts().get(term);
		if (termContext == null) {
			termContext = new TermContext();
			this.getTermContexts().put(term,termContext);
		}
		termContext.setCoOccurrences(context, coOccurrences, TermContext.ADD_MODE);
	}

	@Override
	public void doLoad(URI uri) throws ResourceInitializationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	/**************************************************************************************/
	
	/*
	public void doStore(OutputStream outputStream) throws IOException {
		outputStream.write(this.toString().getBytes());
	}
	
	public String doStore(Set<String> terms,int depth) {
		StringBuilder builder = new StringBuilder();
		for (String term : terms) {
			TermContext context = this.getTermContexts().get(term);
			if (context != null) {
				builder.append(context.toString(term,depth));
			}
		}
		return builder.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String key : this.getTermContexts().keySet()) {
			Integer occurrence = this.getOccurrences().get(key);
			builder.append(key);
			builder.append('#');
			builder.append(occurrence.intValue());
			builder.append(':');
			TermContext context = this.getTermContexts().get(key);
			builder.append(context.toString());
			builder.append('\n');
		}
		return builder.toString();
	}
	*/
		
	/*
	public void doLoad(InputStream inputStream) throws IOException {
		try {
			Scanner scanner = new Scanner(inputStream);
			this.doScan(scanner);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	*/

	/*
	public void fromString(String string) throws Exception {
		Scanner scanner = new Scanner(string);
		this.doScan(scanner);
	}
	*/

	/*
	private void doScan(Scanner scanner) throws Exception {
		String delimiter = System.getProperty("line.separator");
		scanner.useDelimiter(delimiter);
		int progress = 0;
		while (scanner.hasNextLine()) {
			progress++;
			String line = scanner.nextLine();
			String[] elements = line.split(":");
			int size = elements.length;
			if (size > 1) {
				String[] termElements = elements[0].split("#");
				if (termElements.length == 2) {
					String term = termElements[0];
					Integer occurrences = Integer.valueOf(termElements[1]);
					this.setOccurrences(term, occurrences);
					for (int index = 1; index < size; index++) {
						String[] contextElements = elements[index].split("#");
						if (contextElements.length == 2) {
							String context = contextElements[0];
							Double coOccurrences = Double.valueOf(contextElements[1]);
							
							this.setCoOccurrences(term, context, coOccurrences, TermContext.DEL_MODE);
						} else {
							throw new IOException("Wrong context format (at line " + progress + "): " + line);	
						}
					}
				} else {
					throw new IOException("Wrong term format (at line " + progress + "): " + line);	
				}
			} else {
				throw new IOException("Wrong term context format (at line " + progress + "): " + line);
			}
		}
		
	}
	*/

	/**
	 * This method checks if this index is included into a given index. 
	 * It returns -1 if this index is less than the given index, 
	 * it return 1 if this index is greater than this given index, 
	 * and it returns 0 otherwise. 
	 * 
	 * @param index	a term context index.
	 * @return	0, 1, -1 respectively if this index is included into, 
	 * if it is greater then or if it is less than the given index. 
	 */
	/*
	@Override
	public int compareTo(TermContextIndex index) {
		int diff = 0;
		for (String key : this.getTermContexts().keySet()) {
			TermContext sourceContext = this.getTermContexts().get(key);
			TermContext targetContext = index.getTermContexts().get(key);
			if (targetContext == null) {
				System.out.println("missing target entry for " + key);
				// return 1;
			} else {
				diff = sourceContext.compareTo(targetContext);
				if (diff != 0) {
					System.out.println("missing target entry for " + key);
					// return diff;
				}
			}
		}
		return diff;
	}
	*/
	
}