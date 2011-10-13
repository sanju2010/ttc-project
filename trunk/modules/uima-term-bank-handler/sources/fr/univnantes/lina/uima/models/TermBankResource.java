package fr.univnantes.lina.uima.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;

public class TermBankResource implements TermBank {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3699707737959624316L;

	private void clean() {
		Set<String> hapax = new HashSet<String>();
		for (String key : this.bank.keySet()) {
			Term term = this.bank.get(key);
			if (term.size() == 1) {
				hapax.add(key);
			}
		}
		for (String key : hapax) {
			this.bank.remove(key);			
		}
	}
	
	/**
	 * the bank of terms.
	 */
	private HashMap<String, Term> bank;
	
	/**
	 * initialize the term bank.
	 */
	private void setBank() {
		this.bank = new HashMap<String, Term>();
	}
	
	@Override
	public synchronized void index(String coveredText,String category,String lemma,String complexity,String language,String document,int begin,int end) {
		String key = category + "::" + lemma + "::" + complexity + "::" + language;
		Term term = this.bank.get(key);
		if (term == null) {
			term = new Term(category, lemma, complexity, language);
			this.bank.put(key,term);			
		}
		TermOccurrence occurrence = new TermOccurrence(coveredText, document, begin, end);
		term.add(occurrence);
	}
		
	/************************************************************************************/
	
	public TermBankResource() throws InvalidXMLException, ResourceInitializationException, IOException {
		this.setBank();
		UIMAFramework.getLogger().log(Level.CONFIG,"Building Term Bank " + this);
	}
	
	/************************************************************************************/
	
	@Override
	public void load(DataResource aData) throws ResourceInitializationException { }

	@SuppressWarnings("unchecked")
	public synchronized void update(InputStream inputStream) throws Exception {
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		this.bank = (HashMap<String, Term>) objectStream.readObject();
	}
	
	public synchronized void release(OutputStream outputStream) throws Exception {
		this.clean();
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(this.bank);
		objectStream.flush();
		objectStream.close();
	}	

	public void embed(DefaultTreeModel model) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		Map<String, DefaultMutableTreeNode> languages = new HashMap<String, DefaultMutableTreeNode>();
		for (String key : this.bank.keySet()) {
			Term term = this.bank.get(key);
			String language = term.language();
			DefaultMutableTreeNode lang = languages.get(language); 
			if (lang == null) {
				lang = new DefaultMutableTreeNode();
				Locale locale = new Locale(language);
				lang.setUserObject(locale.getDisplayLanguage(Locale.ENGLISH) + " Terminology Bank");
				languages.put(language, lang);
				root.add(lang);
			}
			DefaultMutableTreeNode node = new DefaultMutableTreeNode();
			node.setUserObject(term);
			// this.addNote(node, "language", term.language);
			this.addNote(node, "complexity", term.complexity());
			this.addNote(node, "category", term.category());
			this.addNote(node, "frequency", term.size());
			lang.add(node);
			for (TermOccurrence occurrence : term.occurrences()) {
				DefaultMutableTreeNode note = new DefaultMutableTreeNode();
				note.setUserObject(occurrence);
				this.addNote(note, "document", occurrence.identifier());
				this.addNote(note, "begin", occurrence.begin());
				this.addNote(note, "end", occurrence.end());
				node.add(note);
			}
		}
		model.reload();
	}
	
	private void addNote(DefaultMutableTreeNode root,String key,Object value) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		if (value != null) {
			String string = key + ": " + value.toString();
			node.setUserObject(string);
			root.add(node);
		}	
	}	
	
}
