package fr.univnantes.lina.uima.engines;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.uima.SingleWordTermAnnotation;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class TermContextLoader extends CollectionReader_ImplBase {

	private int index;
	
	private void setIndex() {
		this.index = 0;
	}
	
	private void addIndex() {
		this.index++;
	}
	
	private int getIndex() {
		return this.index;
	}
	
	private int size;
	
	private void setSize() {
		this.size = this.getSourceDocuments().size() + this.getTargetDocuments().size();
	}
	
	private int getSize() {
		return this.size;
	}
	
	protected Document getDocument() {
		int index = this.getIndex();
		this.addIndex();
		int source = this.getSourceDocuments().size();
		if (index < source) {
			return this.getSourceDocuments().get(index);
		} else {
			index = index - source;
			return this.getTargetDocuments().get(index);
		}
	}
	
	private List<Document> sourceDocuments;
	
	private void setSourceDocuments() throws IOException {
		this.sourceDocuments = new ArrayList<Document>();
		Iterator<Word> sourceIterator = this.sourceWords.iterator();
		List<Word> words = new ArrayList<Word>();
		while (sourceIterator.hasNext()) {
			Word word = sourceIterator.next();
			if (word.doFlush()) {
				if (!words.isEmpty()) {
					Document doc = new Document(this.sourceLanguage,words);
					this.sourceDocuments.add(doc);
					words = new ArrayList<Word>();					
				}
			} else {
				words.add(word);
			}
		}
		if (!words.isEmpty()) {
			Document doc = new Document(this.sourceLanguage,words);
			this.sourceDocuments.add(doc);
		}
	}
	
	private List<Document> getSourceDocuments() {
		return this.sourceDocuments;
	}
	
	private List<Document> targetDocuments;
	
	private void setTargetDocuments() throws IOException {
		this.targetDocuments = new ArrayList<Document>();
		Iterator<Word> targetIterator = this.targetWords.iterator();
		List<Word> words = new ArrayList<Word>();
		while (targetIterator.hasNext()) {
			Word word = targetIterator.next();
			if (word.doFlush()) {
				if (!words.isEmpty()) {
					Document doc = new Document(this.targetLanguage,words);
					this.targetDocuments.add(doc);
					words = new ArrayList<Word>();					
				}
			} else {
				words.add(word);
			}
		}
		if (!words.isEmpty()) {
			Document doc = new Document(this.targetLanguage,words);
			this.targetDocuments.add(doc);
		}
	}
	
	private List<Document> getTargetDocuments() {
		return this.targetDocuments;
	}
	
	private List<Word> sourceWords;
	
	private void setSourceWords() {
		this.sourceWords = new ArrayList<Word>();
	}
	
	private List<Word> targetWords;
	
	private void setTargetWords() {
		this.targetWords = new ArrayList<Word>();
	}

	private void setSourceCorpus(File file) throws IOException {
		InputStream stream = new FileInputStream(file);
		Scanner scanner = new Scanner(stream);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] items = line.split("\t");
			if (items.length == 3) {
				Word word = new Word();
				word.setText(items[0]);
				word.setCategory(items[1]);
				word.setLemma(items[2]);
				this.sourceWords.add(word);
			} else {
				throw new IOException("Wrong format at line: " + line);
			}
		}
	}
	
	private void setTargetCorpus(File file) throws IOException {
		InputStream stream = new FileInputStream(file);
		Scanner scanner = new Scanner(stream);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] items = line.split("\t");
			if (items.length == 3) {
				Word word = new Word();
				word.setText(items[0]);
				word.setCategory(items[1]);
				word.setLemma(items[2]);
				this.targetWords.add(word);
				// System.err.println("Adding Target " + line);
			} else {
				throw new IOException("Wrong format at line: " + line);
			}
		}
	}
	
	private void setSourceCorpus(String path) throws IOException {
		if (path != null) {
			File file = new File(path);
			this.setSourceCorpus(file);			
		}
	}
	
	private void setTargetCorpus(String path) throws IOException {
		if (path != null) {
			File file = new File(path);
			this.setTargetCorpus(file);			
		}
	}
	
	private String sourceLanguage;
	
	private void setSourceLanguage(String language) {
		this.sourceLanguage = language;
	}
	
	private String targetLanguage;
	
	private void setTargetLanguage(String language) {
		this.targetLanguage = language;
	}
	
	@Override
	public void initialize() throws ResourceInitializationException {
		try {
			String sourceLanguage = (String) this.getConfigParameterValue("SourceLanguage");
			String targetLanguage = (String) this.getConfigParameterValue("TargetLanguage");
			String sourceCorpus = (String) this.getConfigParameterValue("SourceFile");
			String targetCorpus = (String) this.getConfigParameterValue("TargetFile");
			this.setSourceWords();
			this.setTargetWords();
			this.setSourceLanguage(sourceLanguage);
			this.setTargetLanguage(targetLanguage);
			this.setSourceCorpus(sourceCorpus);
			this.setTargetCorpus(targetCorpus);
			this.setSourceDocuments();
			this.setTargetDocuments();
			this.setIndex();
			this.setSize();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	// @Override
	public void getNext(CAS cas) throws CollectionException {
		try {
			Document doc = (Document) this.getDocument();
			doc.setDocumentText(cas.getJCas());
		} catch (Exception e) {
			throw new CollectionException(e);
		}
	}

	private class Word {
		
		private String text;
		
		private String category;
		
		private String lemma;

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getCategory() {
			return category;
		}

		public void setLemma(String lemma) {
			this.lemma = lemma;
		}

		public String getLemma() {
			return lemma;
		}
		
		public boolean doFlush() {
			return this.text.equals("__FILE__");
		}
		
	}
	
	private class Document {
		
		private List<Word> words;
		
		private String language;
		
		private File file;
				
		public String toString() {
			return this.file.getAbsoluteFile() + "(" + this.language + ")";
		}
		
		public Document(String language,List<Word> words) throws IOException {
			this.language = language;
			this.words = words;
			this.file = File.createTempFile("breast-cancer-",".txt");
			this.file.deleteOnExit();
		}
		
		public void setDocumentText(JCas cas) throws Exception {			
			StringBuffer buffer = new StringBuffer();
			for (int index = 0; index < this.words.size(); index++) {
				Word word = this.words.get(index);
				if (index > 0) {
					buffer.append(" ");
				}
				int begin = buffer.length();
				buffer.append(word.getText());
				int end = buffer.length();
				
				if (word.getCategory().equals("SENT")) {
					continue;
				}
				SingleWordTermAnnotation annotation = new SingleWordTermAnnotation(cas, begin, end);
				annotation.setCategory(word.getCategory());
				annotation.setLemma(word.getLemma());
				annotation.setReCoveredText(word.getText());
				annotation.setLanguage(this.language);
				annotation.addToIndexes();
				
			}
			cas.setDocumentText(buffer.toString());
			cas.setDocumentLanguage(this.language);
			int length = buffer.length();
			
			/*
			TermContextIndexAnnotation annotation = new TermContextIndexAnnotation(cas, 0, length);
			annotation.setLanguage(this.language);
			annotation.setName(this.name);
			annotation.addToIndexes();
			*/
						
			SourceDocumentInformation info = new SourceDocumentInformation(cas,0,length);
			info.setUri(this.file.toURI().toString());
			info.setOffsetInSource(0);
			info.setLastSegment(!hasNext());
			info.setDocumentSize(length);
			info.addToIndexes();
		}
		
	}

	/**
	 * This method returns a boolean value that is true if and only if their still have a resource 
	 * to populate as CAS in the collection.
	 * 
	 * @return true if all resources are not populated into CAS. 
	 */
	@Override
	public boolean hasNext() {
		return this.getIndex() < this.getSize();
	}

	/**
	 * This method returns the state of the CAS populate process from collection resources   
	 * as a progress array. 
	 * 
	 * @return a progress array of CAS populate process among the collection resources.  
	 */
	@Override
	public Progress[] getProgress() {
		Progress progress = new ProgressImpl(this.getIndex(),this.getSize(),Progress.ENTITIES);
		Progress[] result = { progress } ;
		return result ;
	}

	@Override
	public void close() throws IOException { }
		
	
}
