package eu.project.ttc.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.tartarus.snowball.models.PorterStemmer;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

import uima.sandbox.catcher.resources.Rule;
import uima.sandbox.catcher.resources.RuleSystem;

public class MultiWordTermGatherer extends JCasAnnotator_ImplBase {
	
	private RuleSystem ruleSystem;
	
	private void setRuleSystem(RuleSystem ruleSystem) {
		this.ruleSystem = ruleSystem;
	}
	
	private RuleSystem getRuleSystem() {
		return this.ruleSystem;
	}
	
	private boolean enable;
	
	private void enable(boolean enabled) {
		this.enable = enabled;
	}
	
	@Override 
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getStemmer() == null) {
				this.setStemmer();
			}
			Boolean enabled = (Boolean) context.getConfigParameterValue("Enable");
			this.enable(enabled == null ? false : enabled.booleanValue());
			if (this.enable) {
				RuleSystem ruleSystem = (RuleSystem) context.getResourceObject("RuleSystem");
				this.setRuleSystem(ruleSystem);
			
				this.setAnnotations();
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
			
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException { 
		if (this.enable) {
			this.getAnnotations().clear();
			// this.clean(cas);
			this.index(cas);
			this.clean();
			this.sort();
			this.gather(cas);
		}
	}
	
	private Map<String, List<TermAnnotation>> annotations;
	
	private void setAnnotations() {
		this.annotations = new HashMap<String, List<TermAnnotation>>();
	}
	
	private void setAnnotations(Map<String, List<TermAnnotation>> annotations) {
		TreeMap<String, List<TermAnnotation>> a = new TreeMap<String, List<TermAnnotation>>();
		a.putAll(annotations);
		this.annotations = a;
	}
	
	private Map<String, List<TermAnnotation>> getAnnotations() {
		return this.annotations;
	}
	
	private String setKey(String term, String category) {
		return term;
	}
	
	private String setKey(String component1, String component2, String category) {
		return component1 + "+" + component2;
	}
	
	private List<String> getKeys(TermAnnotation annotation) {
		List<String> keys = new ArrayList<String>();
		if (annotation instanceof SingleWordTermAnnotation) {
			SingleWordTermAnnotation swtAnnotation = (SingleWordTermAnnotation) annotation;
			if (swtAnnotation.getCompound()) {
				try {
					this.getKeys(swtAnnotation, keys, false);
				} catch (Exception e) {
					// ignore
				}
			} else {
				String key = this.setKey(annotation.getLemma(), annotation.getCategory());
				keys.add(key);				
			}
		} else if (annotation instanceof MultiWordTermAnnotation) {
			MultiWordTermAnnotation mwt = (MultiWordTermAnnotation) annotation;
			try {
				this.getKeys(mwt, keys, true);
			} catch (Exception e) {
				// ignore
			}
		}
		return keys;
	}

	private PorterStemmer stemmer;
	
	private void setStemmer() {
		this.stemmer = new PorterStemmer();
	}
	
	private PorterStemmer getStemmer() {
		return this.stemmer;
	}
	
	private void getKeys(TermAnnotation annotation, List<String> keys, boolean norm) throws Exception {
		JCas cas = annotation.getCAS().getJCas();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		List<String> lemmas = new ArrayList<String>();
		Map<String, String> stems = new HashMap<String, String>();
		while (iterator.hasNext()) {
			TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
			String category = component.getCategory();
			if (norm) {
				if (category.equals("noun") || category.equals("adjective") || category.equals("name") || category.equals("verb")) {
					lemmas.add(component.getLemma());
					stems.put(component.getLemma(), component.getStem());
				}	
			} else {
				lemmas.add(component.getCoveredText());
				this.getStemmer().setCurrent(component.getCoveredText());
				this.getStemmer().stem();
				stems.put(component.getCoveredText(), this.getStemmer().getCurrent());
			}
			
		}
		Collections.sort(lemmas);
		String category = annotation.getCategory();
		for (int i = 0 ; i < lemmas.size(); i++) {
			String lemma = lemmas.get(i);
			if (lemma.isEmpty()) {
				continue;
			} else {
				for (int j = i + 1; j < lemmas.size(); j++) {
					String anotherLemma = stems.get(lemmas.get(j));
					if (anotherLemma.isEmpty()) {
						continue;
					} else {
						String key = this.setKey(lemma, anotherLemma, category);
						keys.add(key);
					}
				}
			}
		}
	}
	
	private void getKeysNew(TermAnnotation annotation, List<String> keys, boolean norm) throws Exception {
		JCas cas = annotation.getCAS().getJCas();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		String lemma = null;
		List<String> lemmas = new ArrayList<String>();
		Map<String, String> stems = new HashMap<String, String>();
		while (iterator.hasNext()) {
			TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
			String category = component.getCategory();
			if (category != null) {
				if (lemma == null) {
					lemma = this.lemma(component);
				} else {
					String l = this.lemma(component);
					lemmas.add(l);
					stems.put(l, this.stem(component));
				}
			}		
		}
		
		// if (lemma != null) {
			for (int i = 0 ; i < lemmas.size(); i++) {
				String l = lemmas.get(i);
				String s = stems.get(l);
				if (l.isEmpty()) {
					continue;
				} else {
					// keys.add(this.setKey(lemma, l, ""));
					// keys.add(this.setKey(l, lemma, ""));
					keys.add(this.setKey(lemma, s, ""));
				}
			}
		// }
	}
	
	private String lemma(TermComponentAnnotation annotation) {
		if (annotation.getLemma() == null) {
			return annotation.getCoveredText();
		} else {
			return annotation.getLemma();
		}
	}

	private String stem(TermComponentAnnotation annotation) {
		if (annotation.getStem() == null) {
			this.getStemmer().setCurrent(annotation.getCoveredText());
			this.getStemmer().stem();
			return this.getStemmer().getCurrent();
		} else {
			return annotation.getStem();
		}
	}
	
	private void clean(JCas cas) {
		HashMap<String, Set<TermAnnotation>> map = new HashMap<String, Set<TermAnnotation>>();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			String id = this.getId(cas, annotation);
			Set<TermAnnotation> set = map.get(id);
			if (set == null) {
				set = new HashSet<TermAnnotation>();
				map.put(id, set);
			}
			set.add(annotation);
		}
		for (String id : map.keySet()) {
			Set<TermAnnotation> set = map.get(id);
			if (set.size() > 1) {
				this.setBase(cas, set);
			}
		}
	}
	
	private void setBase(JCas cas, Set<TermAnnotation> set) {
		TermAnnotation base = null;
		List<TermAnnotation> variants = new ArrayList<TermAnnotation>();
		for (TermAnnotation element : set) {
			if (base == null) {
				base = element;
			} else if (element.getFrequency() > base.getFrequency()) {
				variants.add(base);
				base = element;
			} else {
				variants.add(element);
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
		int occ = base.getOccurrences();
		double freq = base.getFrequency();
		double spec = base.getSpecificity();
		for (TermAnnotation  variant : variants) {
			occ += variant.getOccurrences();
			freq += variant.getFrequency();
			spec += variant.getSpecificity();
			base.setVariants(i, variant);
			i++;
			variant.removeFromIndexes();
		}
		base.setOccurrences(occ);
		base.setFrequency(freq);
		base.setSpecificity(spec);
	}

	private String getId(JCas cas, TermAnnotation annotation) {
		if (annotation instanceof SingleWordTermAnnotation) {
			return annotation.getLemma();
		} else if (annotation instanceof MultiWordTermAnnotation) {
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
			FSIterator<Annotation> iterator = index.subiterator(annotation);
			String lemma = "";
			while (iterator.hasNext()) {
				TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
				if (!lemma.isEmpty()) {
					lemma += " ";
				}
				lemma += component.getLemma();
			}
			return lemma;
		} else {
			return null;
		}
	}

	private void index(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			List<String> keys = this.getKeys(annotation);
			for (String key : keys) {
				List<TermAnnotation> list = this.getAnnotations().get(key);
				if (list == null) {
					list = new ArrayList<TermAnnotation>();
					this.getAnnotations().put(key, list);				
				}
				list.add(annotation);				
			}
		}
	}
	
	private void clean() {
		Set<String> keys = new HashSet<String>();
		for (String key : this.getAnnotations().keySet()) {
			List<TermAnnotation> list = this.getAnnotations().get(key);
			if (list.size() < 2) {
				keys.add(key);
			}
		}
		for (String key : keys) {
			this.getAnnotations().remove(key);
		}
	}
	
	private void sort() {
		this.setAnnotations(this.getAnnotations());
	}
	
	private void gather(JCas cas) {
		this.getContext().getLogger().log(Level.INFO, "Rule-based gathering over " + this.getAnnotations().size() + " term classes");
		for (String key : this.getAnnotations().keySet()) {
			List<TermAnnotation> list = this.getAnnotations().get(key);
			this.getContext().getLogger().log(Level.FINE, "Rule-based gathering over the '" + key + "' term class of size " + list.size());			
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (i == j) {
						continue;
					} else {
						for (Rule rule : this.getRuleSystem().get()) {
							TermAnnotation source = list.get(i);
							TermAnnotation target = list.get(j);
							this.process(cas, rule, source, target);
						}
					}
				}
			}
		}
	}
	
	private void process(JCas cas, Rule rule, Annotation first, Annotation second) {
		if (rule.check(cas, first, second)) {
			// UIMAFramework.getLogger().log(Level.INFO,"Applying: " + rule.id());
			try {
				if (rule.match(cas, first, second)) {
					this.release(cas, rule.id(), rule.get());
				} else {
					// UIMAFramework.getLogger().log(Level.WARNING,"Annotation Match Failure: " + rule.id());
				}
			} catch (Exception e) {
				this.getContext().getLogger().log(Level.WARNING,"Term Gathering Failure: " + e.getMessage());
			}
		} else {
			// UIMAFramework.getLogger().log(Level.WARNING,"Type Check Failure: " + rule.id());		
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
	}

}
