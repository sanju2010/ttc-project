package eu.project.ttc.engines;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.models.TermContextSpace;
import fr.univnantes.lina.uima.dictionaries.Dictionary;

public class TermContextShrinker extends JCasAnnotator_ImplBase {
	
	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
	}
	
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary translations) {
		this.dictionary = translations;
	}
	
	private Dictionary getDictionary() {
		return this.dictionary;
	}
	
	private String sourceName;
	
	private void setSourceName(String name) {
		this.sourceName = name;
	}
	
	private String getSourceName() { 
		return this.sourceName;
	}
	
	private String targetName;
	
	private void setTargetName(String name) {
		this.targetName = name;
	}
	
	private String getTargetName() { 
		return this.targetName;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			TermContextSpace space = (TermContextSpace) context.getResourceObject("TermContextSpace");
			this.setTermContextSpace(space);

			Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
			this.setDictionary(dictionary);
			
			String sourceName = (String) context.getConfigParameterValue("SourceName");
			this.setSourceName(sourceName);

			String targetName = (String) context.getConfigParameterValue("TargetName");
			this.setTargetName(targetName);
			
			String path = (String) context.getConfigParameterValue("DictionaryFile");
			if (path != null) {
				File file = new File(path);
				this.getDictionary().doLoad(file.toURI());
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			Set<String> sourceFilter = this.getDictionary().map().keySet();
			this.getTermContextSpace().doShrink(this.getSourceName(),sourceFilter);
			Collection<Set<String>> targetFilters = this.getDictionary().map().values();
			Set<String> targetFilter = new HashSet<String>();
			for (Set<String> filter : targetFilters) {
				targetFilter.addAll(filter);
			}
			this.getTermContextSpace().doShrink(this.getTargetName(),targetFilter);
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
