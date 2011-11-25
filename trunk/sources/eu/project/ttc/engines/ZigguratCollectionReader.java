package eu.project.ttc.engines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;

import org.apache.uima.resource.ResourceInitializationException;

import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import eu.project.ttc.types.TranslationAnnotation;
import fr.univnantes.lina.uima.dictionaries.DictionaryFactory;
import fr.univnantes.lina.uima.dictionaries.Entry;

public class ZigguratCollectionReader extends CollectionReader_ImplBase {

	private Iterator<Entry> iterator;
	
	private void setIterator(Collection<Entry> entries) {
		this.size = entries.size();
		this.iterator = entries.iterator();
	}
	
	private void setIterator(String path) throws IOException {
		InputStream inputStream = new FileInputStream(path);
		Set<Entry> entries = DictionaryFactory.doParse(inputStream);
		this.setIterator(entries);
	}
	
	private Iterator<Entry> getIterator() {
		return this.iterator;
	}
	
	private int size;
	
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
			String path = (String) this.getConfigParameterValue("File");
			this.setIterator(path);
			this.setIndex();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return this.getIterator().hasNext();
	}
	
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
		try {
			Entry entry = this.getIterator().next();
			cas.setDocumentLanguage(entry.getSourceLanguage());
			cas.setDocumentText(entry.getSourceEntry());
			
			TranslationAnnotation annotation = new TranslationAnnotation(cas.getJCas(), 0, cas.getDocumentText().length());
			annotation.setTerm(entry.getTargetEntry());
			annotation.setLanguage(entry.getTargetLanguage());
			annotation.addToIndexes();
			
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas.getJCas());
			sdi.setBegin(0);
			sdi.setEnd(cas.getDocumentText().length());
			sdi.setDocumentSize(cas.getDocumentText().length());
			sdi.setLastSegment(!this.hasNext());
			sdi.setOffsetInSource(0);
			sdi.setUri("http://" + cas.getDocumentText() + ".term");
			sdi.addToIndexes();
			this.incrIndex();
			Locale locale =  new Locale(cas.getDocumentLanguage());
			UIMAFramework.getLogger().log(Level.INFO,"Processing " + cas.getDocumentText() + " in " + locale.getDisplayLanguage(Locale.ENGLISH));
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
