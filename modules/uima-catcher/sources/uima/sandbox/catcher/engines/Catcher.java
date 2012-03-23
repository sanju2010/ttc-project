package uima.sandbox.catcher.engines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.catcher.resources.Rule;
import uima.sandbox.catcher.resources.RuleSystem;

public abstract class Catcher extends JCasAnnotator_ImplBase {

	private RuleSystem ruleSystem;
	
	private void setRuleSystem(RuleSystem ruleSystem) {
		this.ruleSystem = ruleSystem;
	}
	
	private RuleSystem getRuleSystem() {
		return this.ruleSystem;
	}
	
	private String path;
	
	private void setPath(String path) throws IOException {
		this.path = path;
		if (this.override()) {
			this.getRuleSystem().clear();
		}
		InputStream inputStream = new FileInputStream(path);
		this.getRuleSystem().load(inputStream);
	}
	
	private String getPath() {
		return this.path;
	}
	
	private boolean override;
	
	private void enableOverride(boolean enabled) {
		this.override = enabled;
	}
	
	private boolean override() {
		return this.override;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			RuleSystem ruleSystem = (RuleSystem) context.getResourceObject("RuleSystem");
			this.setRuleSystem(ruleSystem);
			
			Boolean override = (Boolean) context.getConfigParameterValue("Override");
			this.enableOverride(override.booleanValue());
			
			String path = (String) context.getConfigParameterValue("File");
			if (path != null && this.getPath() == null) {
				this.setPath(path);
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			for (Rule rule : this.getRuleSystem().get()) {
				if (rule.check(cas)) {
					if (rule.match(cas)) {
						this.release(cas, rule.id(), rule.get());
					} 
				}
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private void release(JCas cas, String id, List<List<Annotation>> lists) {
		for (List<Annotation> list : lists) {
			Annotation[] annotations = new Annotation[list.size()];
			list.toArray(annotations);
			this.release(cas, id, annotations);
		}
	}

	protected abstract void release(JCas cas, String id, Annotation[] annotations);

}
