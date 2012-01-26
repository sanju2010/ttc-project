package eu.project.ttc.models;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.TermAnnotation;

import uima.sandbox.catcher.resources.Rule;
import uima.sandbox.catcher.resources.RuleSystem;
import uima.sandbox.indexer.resources.IndexListener;

public class TermVariantListener implements IndexListener {
	
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
	public void configure(UimaContext context) throws ResourceInitializationException {
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
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void index(Annotation annotation) { }
	
	private boolean done = false;

	@Override
	public void release(JCas cas) { 
		if (!this.done) {
			this.done = true;
			this.process(cas);
		}
	}
	
	void process(JCas cas) {
		try {
			for (Rule rule : this.getRuleSystem().get()) {
				UIMAFramework.getLogger().log(Level.INFO,"Checking: " + rule.id());
				try {
				if (rule.check(cas)) {
					UIMAFramework.getLogger().log(Level.INFO,"Applying: " + rule.id());
					try {
					if (rule.match(cas)) {
						this.release(cas, rule.id(), rule.get());
					} else {
						UIMAFramework.getLogger().log(Level.WARNING,"Annotation Match Failure: " + rule.id());	
					}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIMAFramework.getLogger().log(Level.WARNING,"Type Check Failure: " + rule.id());		
				}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (Throwable e) {
			UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		}
	}

	private void release(JCas cas, String id, List<List<Annotation>> lists) {
		for (List<Annotation> list : lists) {
			Annotation[] annotations = new Annotation[list.size()];
			list.toArray(annotations);
			this.release(cas, id, annotations);
		}
	}

	protected void release(JCas cas, String id, Annotation[] annotations) {
		String message = "Found " + annotations.length + " annotations with the rule: " + id;
		
		TermAnnotation base = null;
		Set<TermAnnotation> variants = new HashSet<TermAnnotation>();
		for (Annotation annotation : annotations) {
			if (annotation instanceof TermAnnotation) {
				TermAnnotation term = (TermAnnotation) annotation;
				if (base == null) {
					base = term;
				} else if (base.getFrequency() < term.getFrequency()) {
					variants.add(base);
					base = term;
				} else {
					variants.add(term);
				}
			}
		}
		FSArray old = base.getVariants();
		if (old != null) {
			for (int i = 0; i < base.getVariants().size(); i++) {
				TermAnnotation variant = base.getVariants(i);
				variants.add(variant);
			}
		}
		FSArray ar = new FSArray(cas, variants.size());
		base.setVariants(ar);
		int i = 0;
		for (TermAnnotation  variant : variants) {
			base.setVariants(i, variant);
			i++;
		}
		message += "\nbase = " + base.getCoveredText();
		for (int index = 0; index < base.getVariants().size(); index++) {
			message += "\n\tvariant = " + base.getVariants(index).getCoveredText();
		}
		System.out.println(message);
	}

}
