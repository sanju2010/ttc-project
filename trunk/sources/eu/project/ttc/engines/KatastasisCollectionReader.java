package eu.project.ttc.engines;

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
import eu.project.ttc.types.TermAnnotation;

public class KatastasisCollectionReader extends CollectionReader_ImplBase {

	private String language;
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
	
	private Set<String> terminology;
	
	private void setTerminology(String path) throws Exception {
		File file = new File(path);
		this.setTerminology(file);
	}
	
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
		UIMAFramework.getLogger().log(Level.INFO, "Loading " + lang + " Terminology");
		XmiCasDeserializer.deserialize(stream, cas);
		this.setTerminology(cas);
	}

	private void setTerminology(CAS cas) throws CASException {
		AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(SingleWordTermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator.next();					
			String term = annotation.getCoveredText().toLowerCase();
			this.terminology.add(term);						
		}
	}
			
	private Set<String> getTerminology() {
		return this.terminology;
	}
	
	private Iterator<File> files;

	private void setFiles(String path) throws FileNotFoundException {
		File directory = new File(path);
		this.setFiles(directory);
	}
	
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
			this.setFilter();
			String language = (String) this.getConfigParameterValue("Language");
			this.setLanguage(language);
			String directory = (String) this.getConfigParameterValue("Directory");
			this.setFiles(directory);
			String terminology = (String) this.getConfigParameterValue("TerminologyFile");
			this.setTerminology(terminology);
			this.setIndex();
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
			File file = this.getFiles().next();
			InputStream stream = new FileInputStream(file);
			XmiCasDeserializer.deserialize(stream, cas);
			AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(TermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			Set<Annotation> annotations = new HashSet<Annotation>();
			while (iterator.hasNext()) {
				TermAnnotation annotation = (TermAnnotation) iterator.next();
				if (annotation instanceof SingleWordTermAnnotation) {
					String lemma = annotation.getLemma().toLowerCase();
					if (!this.getTerminology().contains(lemma)) { 
						annotations.add(annotation);
					}
				} else {
					annotations.add(annotation);
				}
			}
			for (Annotation annotation : annotations) {
				annotation.removeFromIndexes();
			}
			UIMAFramework.getLogger().log(Level.INFO, "Processing " + file.getAbsolutePath());
			this.incrIndex();
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
	public void close() throws IOException { }
	
}
