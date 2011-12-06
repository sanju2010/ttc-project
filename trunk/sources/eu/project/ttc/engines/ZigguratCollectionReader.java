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

import eu.project.ttc.models.Term;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.NeoClassicalCompoundTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
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
			int begin = 0;
			int end = cas.getDocumentText().length();
			
			String complexity = entry.getSourceCategory();
			TermAnnotation term = null;
			if (complexity.equals(Term.SINGLE_WORD)) {
				term = new SingleWordTermAnnotation(cas.getJCas(), begin, end);
			} else if (complexity.equals(Term.MULTI_WORD)) {
				term = new MultiWordTermAnnotation(cas.getJCas(), begin, end);
			} else if (complexity.equals(Term.NEO_CLASSICAL_COMPOUND)) {
				term = new NeoClassicalCompoundTermAnnotation(cas.getJCas(), begin, end);
			} else {
				term = new TermAnnotation(cas.getJCas(), begin, end);
			}
			term.addToIndexes();
			
			TranslationAnnotation annotation = new TranslationAnnotation(cas.getJCas(), begin, end);
			annotation.setTerm(entry.getTargetEntry());
			annotation.setLanguage(entry.getTargetLanguage());
			annotation.addToIndexes();
			
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas.getJCas(), begin, end);
			sdi.setDocumentSize(end);
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
