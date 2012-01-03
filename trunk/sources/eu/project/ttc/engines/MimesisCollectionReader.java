package eu.project.ttc.engines;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
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

import eu.project.ttc.types.VectorAnnotation;

public class MimesisCollectionReader extends CollectionReader_ImplBase {

	private String language;
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
	
	private Iterator<Annotation> vectors;
	
	private void setVectors(String path) throws Exception {
		File file = new File(path);
		this.setVectors(file);
	}
	
	private void setVectors(File file) throws Exception {
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/types/TermSuiteTypeSystem.xml");
		XMLInputSource source = new XMLInputSource(url);
		XMLParser parser = UIMAFramework.getXMLParser();
		TypeSystemDescription ts = parser.parseTypeSystemDescription(source); 
		CAS cas = CasCreationUtils.createCas(ts,null,new FsIndexDescription[0]);
		InputStream stream = new FileInputStream(file);
		Locale locale = new Locale(this.getLanguage());
		String lang = locale.getDisplayLanguage(Locale.ENGLISH);
		UIMAFramework.getLogger().log(Level.INFO, "Loading " + lang + " Contextual Terminology");
		XmiCasDeserializer.deserialize(stream, cas);
		AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(VectorAnnotation.type);
		this.vectors = index.iterator();
		this.setSize(index.size());
	}
		
	private Iterator<Annotation> getVectors() {
		return this.vectors;
	}
		
	private int size;
	
	private void setSize(int size) {
		this.size = size;
	}
		
	public int getSize() {
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
			String language = (String) this.getConfigParameterValue("Language");
			this.setLanguage(language);
			String terminology = (String) this.getConfigParameterValue("File");
			this.setVectors(terminology);
			this.setIndex();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return this.getVectors().hasNext();
	}
	
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
		try {
			VectorAnnotation vector = (VectorAnnotation) this.getVectors().next();
			cas.setDocumentText(vector.getCoveredText());
			cas.setDocumentLanguage(this.getLanguage());
			VectorAnnotation annotation = new VectorAnnotation(cas.getJCas(), 0, cas.getDocumentText().length());
			annotation.setItem(vector.getItem());
			annotation.setFrequency(vector.getFrequency());
			annotation.addToIndexes();
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas.getJCas(), 0, cas.getDocumentText().length());
			sdi.setDocumentSize(cas.getDocumentText().length());
			sdi.setLastSegment(!this.hasNext());
			sdi.setOffsetInSource(0);
			sdi.setUri("http://" + vector.getItem() + ".term");
			sdi.addToIndexes();
			UIMAFramework.getLogger().log(Level.INFO, "Processing '" + vector.getItem() + "'");
			this.incrIndex();
		} catch (Exception e) {
			throw new CollectionException(e);
		}
	}

	@Override
	public Progress[] getProgress() {
		Progress progress = new ProgressImpl(this.getIndex(), this.getSize(), Progress.ENTITIES);
		Progress[] result = { progress } ;
		return result ;
	}

	@Override
	public void close() throws IOException { }
	
}
