package eu.project.ttc.engines.aligner;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermContextEntryAnnotation;
import eu.project.ttc.types.TermContextIndexAnnotation;
import eu.project.ttc.types.TermEntryAnnotation;

public class TermContextLoader extends CollectionReader_ImplBase {

	private Collection sourceCollection;
	
	private void setSourceCollection(String name, String language, String path, String terminology) throws Exception {
		File directory = new File(path);
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				throw new IOException(path);
			}
		} else {
			throw new FileNotFoundException(path);
		}
		File file = new File(terminology);
		if (file.exists()) {
			if (!file.isFile()) {
				throw new IOException(terminology);
			}
		} else {
			throw new FileNotFoundException(terminology);
		}
		this.sourceCollection = new Collection(name, language, directory, file);
	}
	
	private Collection getSourceCollection() {
		return this.sourceCollection;
	}
	
	private Collection targetCollection;
	
	private void setTargetCollection(String name, String language, String path, String terminology) throws Exception {
		File directory = new File(path);
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				throw new IOException(path);
			}
		} else {
			throw new FileNotFoundException(path);
		}
		File file = new File(terminology);
		if (file.exists()) {
			if (!file.isFile()) {
				throw new IOException(terminology);
			}
		} else {
			throw new FileNotFoundException(terminology);
		}
		this.targetCollection = new Collection(name, language, directory, file);
	}
	
	private Collection getTargetCollection() {
		return this.targetCollection;
	}
	

	private int size;
	
	private void setSize(int size) {
		this.size = size;
	}
		
	private int getSize() {
		return this.size;
	}
	
	private int index;
	
	private void setIndex() {
		this.index = 0;
	}
	
	private void incrIndex() {
		this.index++;
	}
	
	private int getIndex() {
		return this.index;
	}
	
	@Override
	public void initialize() throws ResourceInitializationException {
		try {
			String sourceName = (String) this.getConfigParameterValue("SourceName");
			String sourceLanguage = (String) this.getConfigParameterValue("SourceLanguage");
			String sourceDirectory = (String) this.getConfigParameterValue("SourceDirectory");
			String sourceTerminologyFile = (String) this.getConfigParameterValue("SourceTerminologyFile");
			this.setSourceCollection(sourceName, sourceLanguage, sourceDirectory, sourceTerminologyFile);
			String targetName = (String) this.getConfigParameterValue("TargetName");
			String targetLanguage = (String) this.getConfigParameterValue("TargetLanguage");
			String targetDirectory = (String) this.getConfigParameterValue("TargetDirectory");
			String targetTerminologyFile = (String) this.getConfigParameterValue("TargetTerminologyFile");
			this.setTargetCollection(targetName, targetLanguage, targetDirectory, targetTerminologyFile);
			this.setIndex();
			this.setSize(this.getSourceCollection().getSize() + this.getTargetCollection().getSize());
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return this.getIndex() < this.getSize();
	}
	
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
		try {
			if (this.getSourceCollection().hasNext()) {
				this.incrIndex();
				this.getSourceCollection().getNext(cas);
			} else if (this.getTargetCollection().hasNext()) {
				this.incrIndex();
				this.getTargetCollection().getNext(cas);
			} else {
				throw new CollectionException();
			}
		} catch (Exception e) {
			throw new CollectionException(e);
		}
	}

	@Override
	public Progress[] getProgress() {
		Progress progress = new ProgressImpl(this.getIndex(),this.getSize(),Progress.ENTITIES);
		Progress[] result = { progress } ;
		return result ;
	}

	@Override
	public void close() throws IOException { 
		this.getSourceCollection().close();
		this.getTargetCollection().close();
	}

	private class Collection {
		
		public Collection(String name, String language,File directory,File file) throws Exception {
			this.setFilter();
			this.setName(name);
			this.setLanguage(language);
			this.setTerminology(file);
			this.setFiles(directory);
		}
				
		private String name;
		
		private void setName(String name) {
			this.name = name;
		}
		
		private String getName() {
			return this.name;
		}
		
		private String language;
		
		private void setLanguage(String language) {
			this.language = language;
		}
		
		private String getLanguage() {
			return this.language;
		}
		
		private Set<String> terminology;
		
		private void setTerminology(File file) throws Exception {
			this.terminology = new HashSet<String>();
			URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/types/TermSuiteTypeSystem.xml");
			XMLInputSource source = new XMLInputSource(url);
			XMLParser parser = UIMAFramework.getXMLParser();
			TypeSystemDescription ts = parser.parseTypeSystemDescription(source); 
			CAS cas = CasCreationUtils.createCas(ts,null,new FsIndexDescription[0]);
			InputStream stream = new FileInputStream(file);
			Locale locale = new Locale(this.getLanguage());
			String lang = locale.getDisplayLanguage(Locale.ENGLISH);
			UIMAFramework.getLogger().log(Level.INFO, "Loading " + this.getName() + " " + lang + " Terminology");
			XmiCasDeserializer.deserialize(stream, cas);
			this.setTerminology(cas);
		}

		private void setTerminology(CAS cas) throws CASException {
			AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(TermEntryAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			while (iterator.hasNext()) {
				try {
					TermEntryAnnotation annotation = (TermEntryAnnotation) iterator.next();					
					String term = annotation.getTerm().toLowerCase();
					if (term.length() > 1) {
						this.terminology.add(term);						
					}
				} catch (Exception e) {
					// ignore
				}
			}
		}
				
		private Set<String> getTerminology() {
			return this.terminology;
		}
		
		private Iterator<File> files;

		private void setFiles(File directory) throws FileNotFoundException {
			if (directory.exists()) {
				if (directory.isDirectory()) {				
					List<File> files = new ArrayList<File>();
					files.addAll(Arrays.asList(directory.listFiles(this.getFilter())));
					Collections.sort(files);
					this.files = files.iterator();
					this.setSize(files.size());
				} else {
					throw new FileNotFoundException(directory.getAbsolutePath());
				}
			} else {
				throw new FileNotFoundException(directory.getAbsolutePath());
			}
		}

		private Iterator<File> getFiles() {
			return this.files;
		}
		
		public boolean hasNext() {
			return this.files.hasNext();
		}
		
		public void getNext(CAS cas) throws IOException, CollectionException {
			try {
				File file = this.getFiles().next();
				InputStream stream = new FileInputStream(file);
				XmiCasDeserializer.deserialize(stream, cas);
				AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(SingleWordTermAnnotation.type);
				FSIterator<Annotation> iterator = index.iterator();
				while (iterator.hasNext()) {
					SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator.next();
					// String term = annotation.getCoveredText().toLowerCase();
					String lemma = annotation.getLemma().toLowerCase();
					if (this.getTerminology().contains(lemma)) {
						String category = annotation.getCategory().toLowerCase();
						TermContextEntryAnnotation entry = new TermContextEntryAnnotation(cas.getJCas(), annotation.getBegin(), annotation.getEnd());
						entry.setCategory(category);
						entry.setLemma(lemma);
						entry.addToIndexes();
					}
				}
				TermContextIndexAnnotation annotation = new TermContextIndexAnnotation(cas.getJCas(), cas.getDocumentAnnotation().getEnd(), cas.getDocumentAnnotation().getEnd());
				annotation.setLanguage(this.getLanguage());
				annotation.setName(this.getName());
				annotation.addToIndexes();
				UIMAFramework.getLogger().log(Level.INFO, "Processing " + this.getName() + " " + file.getAbsolutePath());
			} catch (Exception e) {
				throw new CollectionException(e);
			}
		}
		
		public void close() throws IOException { }
		
		private int size;
		
		private void setSize(int size) {
			this.size = size;
		}
			
		public int getSize() {
			return this.size;
		}
			
		private FileFilter filter;
		
		private void setFilter() {

			this.filter = new FileFilter() {
			
				@Override
				public boolean accept(File file) {
					if (file.isDirectory()) {
						return false;
					} else {
						return file.getName().endsWith(".xmi");
					}
				}
				
			};
		
		}
		
		private FileFilter getFilter() {
			return this.filter;
		}
		
	}
	
}
