package eu.project.ttc.models;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.CompoundTermAnnotation;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.NeoClassicalCompoundTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

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
	
	private boolean enable;
	
	private void enable(boolean enabled) {
		this.enable = enabled;
	}
	
	@Override 
	public void configure(UimaContext context) throws ResourceInitializationException {
		try {
			Boolean enabled = (Boolean) context.getConfigParameterValue("Enable");
			this.enable(enabled == null ? false : enabled.booleanValue());
			if (this.enable) {
				RuleSystem ruleSystem = (RuleSystem) context.getResourceObject("RuleSystem");
				this.setRuleSystem(ruleSystem);
			
				Boolean override = (Boolean) context.getConfigParameterValue("Override");
				this.enableOverride(override.booleanValue());
			
				String path = (String) context.getConfigParameterValue("File");
				if (path != null && this.getPath() == null) {
					this.setPath(path);
				}
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void index(Annotation annotation) { }
		
	@Override
	public void release(JCas cas) { 
		if (this.enable) {
			if (this.getAnnotations() == null) {
				this.setAnnotations();
				this.index(cas);
				this.clean();
				this.sort();
				try {
					this.gather(cas);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
	
	private List<String> getKeys(TermAnnotation annotation, String language) {
		List<String> keys = new ArrayList<String>();
		if (annotation instanceof SingleWordTermAnnotation) {
			String key = this.setKey(annotation.getCoveredText(), annotation.getCategory());
			keys.add(key);
		} else if (annotation instanceof CompoundTermAnnotation) {
			CompoundTermAnnotation compound = (CompoundTermAnnotation) annotation;
			try {
				JCas cas = annotation.getCAS().getJCas();
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
				FSIterator<Annotation> iterator = index.subiterator(compound);
				while (iterator.hasNext()) {
					TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
					String category = component.getCategory();
					if (category.equals("noun") || category.equals("adjective")) {
						String key = this.setKey(component.getCoveredText(), category);
						keys.add(key);
						break;
					}
				}
			} catch (CASException e) {
				// ignore
			}
		} else if (annotation instanceof NeoClassicalCompoundTermAnnotation) {
			NeoClassicalCompoundTermAnnotation ncc = (NeoClassicalCompoundTermAnnotation) annotation;
			try {
				JCas cas = annotation.getCAS().getJCas();
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
				FSIterator<Annotation> iterator = index.subiterator(ncc);
				while (iterator.hasNext()) {
					TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
					String category = component.getCategory();
					if (category.equals("noun") || category.equals("adjective")) {
						String key = this.setKey(component.getCoveredText(), category);
						keys.add(key);
						break;
					}
				}
			} catch (CASException e) {
				// ignore
			}
		} else if (annotation instanceof MultiWordTermAnnotation) {
			MultiWordTermAnnotation mwt = (MultiWordTermAnnotation) annotation;
			try {
				this.getKeys(mwt, keys);
			} catch (CASException e) {
				// ignore
			}
		}
		return keys;
	}

	private void getKeys(MultiWordTermAnnotation mwt, List<String> keys) throws CASException {
		JCas cas = mwt.getCAS().getJCas();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(mwt);
		List<String> components = new ArrayList<String>();
		while (iterator.hasNext()) {
			TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
			String category = component.getCategory();
			if (category.equals("noun") || category.equals("adjective")) {
				components.add(component.getStem());
			}
		}
		Collections.sort(components);
		String category = mwt.getCategory();
		for (int i = 0 ; i < components.size(); i++) {
			for (int j = i + 1; j < components.size(); j++) {
				String key = this.setKey(components.get(i), components.get(j), category);
				keys.add(key);
			}
		}		
	}
	
	private void index(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			List<String> keys = this.getKeys(annotation, cas.getDocumentLanguage());
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
		UIMAFramework.getLogger().log(Level.INFO, "Rule-based gathering over " + this.getAnnotations().size() + " term classes");
		for (String key : this.getAnnotations().keySet()) {
			List<TermAnnotation> list = this.getAnnotations().get(key);
			UIMAFramework.getLogger().log(Level.FINE, "Rule-based gathering over the '" + key + "' term class of size " + list.size());
			
			/*
			Map<String, TermAnnotation> idx = new HashMap<String, TermAnnotation>();
			Map<String, List<TermAnnotation>> index = new HashMap<String, List<TermAnnotation>>();
			for (TermAnnotation item : list) {
				String id = item.getCategory();
				List<TermAnnotation> part = index.get(id);
				if (part == null) {
					part = new ArrayList<TermAnnotation>();
					index.put(id, part);
					idx.put(id, item);
				} else if (item.getFrequency() > idx.get(id).getFrequency()) {
					idx.put(id, item);
				}
				part.add(item);
			}
			for (String id : index.keySet()) {
				List<TermAnnotation> part = index.get(id);
				TermAnnotation item = idx.get(id);
				part.remove(item);
			}
			for (String id : index.keySet()) {
				List<TermAnnotation> part = index.get(id);
				TermAnnotation item = idx.get(id);
				FSArray variants = item.getVariants();
				int offset = 0;
				if (variants == null) {
					variants = new FSArray(cas, part.size());
				} else {
					offset = variants.size();
					TermAnnotation[] fs = new TermAnnotation[offset];
					variants.copyToArray(0, fs, 0, offset);
					variants = new FSArray(cas, offset + part.size());
					variants.copyFromArray(fs, 0, 0, offset);
				}
				item.setVariants(variants);
				for (int i = 0; i < part.size(); i++) {
					item.setVariants(offset + i, part.get(i));
				}
				
			}
			list = new ArrayList<TermAnnotation>(idx.values());
			*/
			/*
			 * faire une partition de cette liste en fonction du patron du terme
			 * 
			 * associer tous les termes de toute les sous partitions entre eux
			 * 
			 * extraire un représentant de chaque partitions (le plus fréquent)
			 * 
			 * appliquer les règles sur ces représentants
			 * 
			 * */
			
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
				UIMAFramework.getLogger().log(Level.WARNING,"Term Gathering Failure: " + e.getMessage());
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
		// String message = "Found " + annotations.length + " annotations with the rule: " + id;
		
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
		/*
		message += "\nbase = " + base.getCoveredText();
		for (int index = 0; index < base.getVariants().size(); index++) {
			message += "\n\tvariant = " + base.getVariants(index).getCoveredText();
		}
		System.out.println(message);
		*/
	}

}
