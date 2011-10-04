package eu.project.ttc.engines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class TermSuiteCollector extends CollectionReader_ImplBase {
	
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
		this.size = this.getSourceCorpus().size() + this.getTargetCorpus().size();
	}
	
	private int getSize() {
		return this.size;
	}

	protected File getDocument() {
		int index = this.getIndex();
		this.addIndex();
		int source = this.getSourceCorpus().size();
		if (index < source) {
			return this.getSourceCorpus().get(index);
		} else {
			index = index - source;
			return this.getTargetCorpus().get(index);
		}
	}
	
	private List<File> sourceCorpus;
	
	private void setSourceCorpus() {
		this.sourceCorpus = new ArrayList<File>();
	}
		
	private List<File> getSourceCorpus() {
		return this.sourceCorpus;
	}

	private List<File> targetCorpus;
	
	private void setTargetCorpus() {
		this.targetCorpus = new ArrayList<File>();
	}
		
	private List<File> getTargetCorpus() {
		return this.targetCorpus;
	}

	private void setSourceCorpus(File file) throws IOException {
		this.getLogger().log(Level.CONFIG,"Scanning Source Corpus " + file.getAbsolutePath());
		this.doCollect(file,true);
	}
	
	private void setTargetCorpus(File file) throws IOException {
		this.getLogger().log(Level.CONFIG,"Scanning Target Corpus " + file.getAbsolutePath());
		this.doCollect(file,false);
	}
	
	private void setSourceCorpus(String[] directories) throws IOException {
		if (directories != null) {
			for (String directory : directories) {
				File file = new File(directory);
				this.setSourceCorpus(file);			
			}			
		}
	}
	
	private void setTargetCorpus(String[] directories) throws IOException {
		if (directories != null) {
			for (String directory : directories) {
				File file = new File(directory);
				this.setTargetCorpus(file);	
			}			
		}
	}

	private String sourceLanguage;
	
	private void setSourceLanguage(String language) {
		this.getLogger().log(Level.CONFIG,"Setting Source Language " + language);
		this.sourceLanguage = language;
	}
	
	private String targetLanguage;
	
	private void setTargetLanguage(String language) {
		this.getLogger().log(Level.CONFIG,"Setting Target Language " + language);
		this.targetLanguage = language;
	}
	
	private String getDocumentLanguage() {
		int index = this.getIndex();
		int source = this.getSourceCorpus().size();
		if (index <= source) {
			return this.sourceLanguage;
		} else {
			return this.targetLanguage;
		}
	}
	
	private boolean browseSubDirectories;
	
	private void enableBrowseSubDirectories(boolean enabled) {
		this.browseSubDirectories = enabled;
	}
	
	private boolean doBrowseSubDirectories() {
		return this.browseSubDirectories;
	}
	
	@Override
	public void initialize() throws ResourceInitializationException {
		try {
			this.setIndex();
			this.setSourceCorpus();
			this.setTargetCorpus();
			Boolean enabled = (Boolean) this.getConfigParameterValue("BrowseSubDirectories");
			this.enableBrowseSubDirectories(enabled.booleanValue());
			String sourceLanguage = (String) this.getConfigParameterValue("SourceLanguage");
			this.setSourceLanguage(sourceLanguage);
			String targetLanguage = (String) this.getConfigParameterValue("TargetLanguage");
			this.setTargetLanguage(targetLanguage);
			String[] sourceCorpus = (String[]) this.getConfigParameterValue("SourceDirectories");
			this.setSourceCorpus(sourceCorpus);
			String[] targetCorpus = (String[]) this.getConfigParameterValue("TargetDirectories");
			this.setTargetCorpus(targetCorpus);
			this.setSize();
		} catch (Exception e) { 
			throw new ResourceInitializationException (e); 
		}
	}

	@Override
	public void getNext(CAS cas) throws CollectionException {
        try {
        	File file = (File) this.getDocument();
        	UIMAFramework.getLogger().log(Level.INFO,"Processing " + file.getAbsolutePath());
        	File document = this.getFile(file);
        	String text = this.getDocumentText(document);
        	String language = this.getDocumentLanguage(); // this.getCrawledDocumentLanguage(dc);
        	cas.setDocumentText(this.doClean(text));
        	cas.setDocumentLanguage(language);
        	// DublinCore dc = this.getDublinCore(file);
        	this.doAnnotate(cas.getJCas(),file,/*dc,*/text);
        } catch (Exception e) { 
        	throw new CollectionException(e); 
        } 
	}
	
	/*
	private DublinCore getDublinCore(File file) throws Exception { 
		DublinCore dc = new DublinCore(file);
		dc.load();
		return dc;
	}
	*/
	
	private String getDocumentText(File file) throws Exception {
		StringBuffer text = new StringBuffer(1048576);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		for (String line = buffer.readLine(); line != null; line = buffer.readLine()) {
			text.append(line);
			text.append('\n');
		}
		buffer.close();
		return text.toString();
	}
	
	/*
	private String getCrawledDocumentLanguage(DublinCore dc) throws Exception {
		if (dc == null) {
			throw new NullPointerException();
		} else {
			DublinCoreProperty property = dc.getProperty("dc:language");
			String language = property.getValue();
			return this.getLanguages().get(language);
		}
	}
	*/
	
	private File getFile(File file) throws NullPointerException, FileNotFoundException {
		File parent = file.getParentFile();
		String name = this.getCrawledDocument(file.getName());
		if (name == null) {
			throw new NullPointerException(file.getName());
		}
		File crawled = new File(parent,name);
		if (!crawled.exists()) {
			throw new FileNotFoundException(crawled.getAbsolutePath());
		}
		return crawled;
	}
	
	private String getCrawledDocument(String name) {
		if (name.endsWith(".xml")) {
			int len = name.length() - 4;
			return name.substring(0,len) + ".txt";
		} else {
			return null;
		}
	}
	
	public void doAnnotate(JCas cas,File file,/*DublinCore dc,*/String text) throws Exception {
		SourceDocumentInformation info = new SourceDocumentInformation(cas);
		info.setBegin(0);
		info.setEnd(text.length());
		info.setDocumentSize(text.length());
		info.setUri(file.toURI().toString());
		info.addToIndexes();
		/*
		if (dc == null) {
			String msg = "No Dublin Core found for " + file;
			throw new Exception(msg);
		} else {
			int length = dc.getProperties().getSize();
			FSArray array = new FSArray(cas,length);
			DublinCoreDocumentAnnotation dublinCore = new DublinCoreDocumentAnnotation(cas);
			dublinCore.setBegin(0);
			dublinCore.setEnd(text.length());
			dublinCore.setMetadata(array);
			for (int index = 0; index < length; index++) {
				Object element = dc.getProperties().get(index);
				if (element instanceof DublinCoreProperty) {
					DublinCoreProperty prop = (DublinCoreProperty) element;
					DublinCorePropertyAnnotation property = new DublinCorePropertyAnnotation(cas);
					property.setName(prop.getName());
					property.setValue(prop.getValue());
					property.setScheme(prop.getScheme());
					property.setLang(prop.getLanguage());
					property.addToIndexes();
					dublinCore.setMetadata(index,property);
				}
			}
			dublinCore.addToIndexes();
		}
		*/
	}
		
	private FileFilter filter = new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				return file.getName().endsWith(".xml");
			}
		}
		
	};
	
	private void doCollect(File directory,boolean source) {
		if (directory.exists()) {
			if (directory.isDirectory()) {				
				List<File> files = new ArrayList<File>();
				files.addAll(Arrays.asList(directory.listFiles(this.filter)));
				Collections.sort(files);
				for (File file : files) {
					if (file.isDirectory()) {
						if (this.doBrowseSubDirectories()) {
							this.doCollect(file,source);							
						}
					} else {
						if (source) {
							// this.getLogger().log(Level.INFO,"Adding Source Document " + file.getName());
							this.getSourceCorpus().add(file);
						} else {
							// this.getLogger().log(Level.INFO,"Adding Target Document " + file.getName());
							this.getTargetCorpus().add(file);
						}
					}
				}
			}
		}
	}
		
	private String doClean(String l) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < l.length(); i++) {
			char c = l.charAt(i);
			if (c == '’') {
				sb.append("'");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
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
