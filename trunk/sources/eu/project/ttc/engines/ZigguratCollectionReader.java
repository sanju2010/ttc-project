package eu.project.ttc.engines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
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
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TranslationAnnotation;
import fr.univnantes.lina.uima.dictionaries.DictionaryFactory;
import fr.univnantes.lina.uima.dictionaries.Entry;

public class ZigguratCollectionReader extends CollectionReader_ImplBase {

	private Iterator<Translation> iterator;
		
	private void setIterator(Collection<Entry> entries) {
		Set<Translation> translations = new HashSet<Translation>();
		for (Entry entry : entries) {
			String term = entry.getSourceEntry();
			String language = entry.getSourceLanguage();
			String category = entry.getSourceCategory();
			Translation translation = null;
			for (Translation t : translations) {
				if (t.getTerm().equals(term) && t.getLanguage().equals(language) && t.getCategory().equals(category)) {
					translation = t;
				}
			}
			if (translation == null) {
				translation = new Translation(term, language, category);
				translations.add(translation);
			}
			translation.addTranslation(entry.getTargetLanguage(), entry.getTargetEntry());
		}
		this.size = translations.size();
		this.iterator = translations.iterator();
	}
	
	private void setIterator(String path) throws IOException {
		InputStream inputStream = new FileInputStream(path);
		Set<Entry> entries = DictionaryFactory.doParse(inputStream);
		this.setIterator(entries);
	}
	
	private Iterator<Translation> getIterator() {
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
			Translation translation = this.getIterator().next();
			cas.setDocumentLanguage(translation.getLanguage());
			cas.setDocumentText(translation.getTerm());
			int begin = 0;
			int end = cas.getDocumentText().length();
			
			String complexity = translation.getCategory();
			if (complexity.equals(Term.SINGLE_WORD)) {
				SingleWordTermAnnotation term = new SingleWordTermAnnotation(cas.getJCas(), begin, end);
				term.setCompound(false);
				term.setNeoclassical(false);
				term.addToIndexes();
			} else if (complexity.equals(Term.MULTI_WORD)) {
				MultiWordTermAnnotation term = new MultiWordTermAnnotation(cas.getJCas(), begin, end);
				term.addToIndexes();
			} else if (complexity.equals(Term.COMPOUND)) {
				SingleWordTermAnnotation term = new SingleWordTermAnnotation(cas.getJCas(), begin, end);
				term.setCompound(true);
				term.setNeoclassical(false);
				term.addToIndexes();
			} else if (complexity.equals(Term.NEO_CLASSICAL_COMPOUND)) {
				SingleWordTermAnnotation term = new SingleWordTermAnnotation(cas.getJCas(), begin, end);
				term.setCompound(true);
				term.setNeoclassical(true);
				term.addToIndexes();
			} else {
				TermAnnotation term = new TermAnnotation(cas.getJCas(), begin, end);
				term.addToIndexes();
			}
			
			Map<String, Set<String>> translations = translation.getTranslations();
			for (String language : translations.keySet()) {
				for (String goldStandard : translations.get(language)) {
					TranslationAnnotation annotation = new TranslationAnnotation(cas.getJCas(), begin, end);
					annotation.setTerm(goldStandard);
					annotation.setLanguage(language);
					annotation.addToIndexes();				
				}
			}
			
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

	private class Translation {
		
		private String term;
		
		public String getTerm() {
			return this.term;
		}
		
		private String language;
		
		public String getLanguage() {
			return this.language;
		}
		
		private String category;
		
		public String getCategory() {
			return this.category;
		}
		
		private Map<String, Set<String>> translations;
		
		public Map<String, Set<String>> getTranslations() {
			return this.translations;
		}
		
		public void addTranslation(String language, String translation) {
			if (this.translations == null) {
				this.translations = new HashMap<String, Set<String>>();
			}
			Set<String> set = this.translations.get(language);
			if (set == null) {
				set = new HashSet<String>();
				this.translations.put(language, set);
			}
			set.add(translation);
		}
		
		public Translation(String term, String language, String category) {
			this.term = term;
			this.language = language;
			this.category = category;
		}

		public boolean equals(Object object) {
			if (object instanceof Translation) {
				Translation translation = (Translation) object;
				return this.term.equals(translation.getTerm()) 
						&& this.language.equals(translation.getLanguage())
						&& this.category.equals(translation.getCategory());
			} else {
				return false;
			}
		}
		
	}
	
}
