package eu.project.ttc.engines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
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

import eu.project.ttc.metrics.EditDistance;
import eu.project.ttc.types.SingleWordTermAnnotation;

public class SingleWordGatherer extends JCasAnnotator_ImplBase {
	
	private EditDistance editDistance;
	
	private void setEditDistance(String name) throws Exception {
		if (this.editDistance == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof EditDistance) {
				this.editDistance = (EditDistance) obj;
				UIMAFramework.getLogger().log(Level.INFO,"Setting Edit Distance: " + this.editDistance.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + EditDistance.class.getCanonicalName());
			}
		}
	}
	
	private EditDistance getEditDistance() {
		return this.editDistance;
	}
	
	private boolean enable;
	
	private void enable(boolean enabled) {
		this.enable = enabled;
	}
	
	private Float treshold;
	
	private void setThreshold(Float value) {
		this.treshold = value;
	}
	
	private Float getThreshold() {
		return this.treshold;
	}
	
	private Integer ngrams;
	
	private void setNgrams(Integer value) {
		this.ngrams = value;
	}
	
	private Integer getNgrams() {
		return this.ngrams;
	}
	
	@Override 
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			Boolean enabled = (Boolean) context.getConfigParameterValue("Enable");
			this.enable(enabled == null ? false : enabled.booleanValue());
			if (this.enable) {
				String editDistance = (String) context.getConfigParameterValue("EditDistanceClassName");
				this.setEditDistance(editDistance);				
			}
			if (this.getThreshold() == null) {
				Float threshold = (Float) context.getConfigParameterValue("Threshold");
				this.setThreshold(threshold);								
			}
			if (this.getNgrams() == null) {
				Integer ngrams = (Integer) context.getConfigParameterValue("Ngrams");
				this.setNgrams(ngrams);
			}
			if (this.getAnnotations() == null) {
				this.setAnnotations();
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private Map<String, List<SingleWordTermAnnotation>> annotations;
	
	private void setAnnotations() {
		this.annotations = new HashMap<String, List<SingleWordTermAnnotation>>();
	}
	
	private void setAnnotations(Map<String, List<SingleWordTermAnnotation>> annotations) {
		Map<String, List<SingleWordTermAnnotation>> a = new TreeMap<String, List<SingleWordTermAnnotation>>();
		a.putAll(annotations);
		this.annotations = a;
	}
	
	private Map<String, List<SingleWordTermAnnotation>> getAnnotations() {
		return this.annotations;
	}
	
	private String getKey(Annotation annotation) {
		String coveredText = annotation.getCoveredText().toLowerCase();
		char ch = coveredText.charAt(0);
		if (ch == 'é' || ch == 'è' || ch == 'ê' || ch == 'ë') {
			ch = 'e';
		}
		if (ch == 'à' || ch == 'â' || ch == 'ä') {
			ch = 'a';
		}
		if (ch == 'ç') {
			ch = 'c';
		}
		if (ch == 'ù' || ch == 'û' || ch == 'ü') {
			ch = 'u';
		}
		if (ch == 'ô' || ch == 'ö') {
			ch = 'o';
		}
		if (ch == 'î' || ch == 'ï') {
			ch = 'i';
		}
		return Character.toString(ch);
	}
	
	private void index(JCas cas) {
		Set<SingleWordTermAnnotation> remove = new HashSet<SingleWordTermAnnotation>();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SingleWordTermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator.next();
			String key = this.getKey(annotation);
			if (key.matches("^[a-z]")) {
				List<SingleWordTermAnnotation> list = this.getAnnotations().get(key);
				if (list == null) {
					list = new ArrayList<SingleWordTermAnnotation>();
					this.getAnnotations().put(key, list);
				}
				list.add(annotation);
			} else {
				remove.add(annotation);
			}
		}
		for (SingleWordTermAnnotation r : remove) {
			r.removeFromIndexes();
		}
	}
	
	private void clean() {
		Set<String> keys = new HashSet<String>();
		for (String key : this.getAnnotations().keySet()) {
			List<SingleWordTermAnnotation> list = this.getAnnotations().get(key);
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
		UIMAFramework.getLogger().log(Level.INFO, "Edit-distance gathering over " + this.getAnnotations().size() + " term classes");
		for (String key : this.getAnnotations().keySet()) {
			List<SingleWordTermAnnotation> list = this.getAnnotations().get(key);
			UIMAFramework.getLogger().log(Level.FINE, "Edit-distance gathering over the '" + key + "' term class of size " + list.size());
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					String source = list.get(i).getCoveredText();
					String target = list.get(j).getCoveredText();
					int distance = this.getEditDistance().compute(source, target);
					double norm = this.getEditDistance().normalize(distance, source, target);
					if (norm >= this.getThreshold().doubleValue()) {
						this.annotate(cas, list, i, j);
					}
				}
			}
		}
	}
	
	private void annotate(JCas cas, List<SingleWordTermAnnotation> list, int i, int j) {
		SingleWordTermAnnotation base = list.get(i);
		SingleWordTermAnnotation variant = list.get(j);
		if (base.getFrequency() < variant.getFrequency()) {
			SingleWordTermAnnotation tmp = base;
			base = variant;
			variant = tmp;
		}
		FSArray array = base.getVariants();
		if (array == null) {
			array = new FSArray(cas, 1);
		} else {
			SingleWordTermAnnotation[] fs = new SingleWordTermAnnotation[array.size() + 1];
			array.copyToArray(0, fs, 0, array.size());
			array = new FSArray(cas, fs.length);
			array.copyFromArray(fs, 0, 0, fs.length - 1);
		}
		base.setVariants(array);
		base.setVariants(array.size() - 1 , variant);
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException { 
		if (this.enable) {
			this.index(cas);
			this.clean();
			this.sort();
			this.gather(cas);
		}
	}
	
}
