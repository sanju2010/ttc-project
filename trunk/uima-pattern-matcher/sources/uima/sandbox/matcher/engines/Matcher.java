package uima.sandbox.matcher.engines;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.matcher.models.trees.Tree;
import uima.sandbox.matcher.resources.RuleSystem;

public class Matcher extends JCasAnnotator_ImplBase {
	
	private RuleSystem ruleSystem;
	
	private void setRuleSystem(RuleSystem ruleSystem) {
		this.ruleSystem = ruleSystem;
	}
	
	private RuleSystem getRuleSystem() {
		return this.ruleSystem;
	}
	
	private String path;

	private void setPath(String path) throws Exception {
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
		
	private Map<Integer, List<Annotation>> annotations;
	
	private void setAnnotations() {
		this.annotations = new ConcurrentHashMap<Integer, List<Annotation>>();
	}
	
	private Map<Integer, List<Annotation>> getAnnotations() {
		return this.annotations;
	}
	
	private int getBegin(int index) {
		return this.getAnnotations().get(index).get(0).getBegin();
	}
	
	private int getEnd(int index) {
		int last = this.getAnnotations().get(index).size() - 1;
		return this.getAnnotations().get(index).get(last).getEnd();
	}
	
	private Map<Integer, List<Tree>> trees;
	
	private void setTrees() {
		this.trees = new ConcurrentHashMap<Integer, List<Tree>>();
	}
	
	private Map<Integer, List<Tree>> getTrees() {
		return this.trees;
	}
	
	private String type;
	
	private void setType(String type) {
		this.type = type;
	}
	
	private Type getType(JCas cas) {
		return cas.getTypeSystem().getType(this.type);
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			RuleSystem ruleSystem = (RuleSystem) context.getResourceObject("RuleSystem");
			this.setRuleSystem(ruleSystem);
			
			String type = (String) context.getConfigParameterValue("Type");
			this.setType(type);
			
			Boolean override = (Boolean) context.getConfigParameterValue("Override");
			this.enableOverride(override.booleanValue());
			
			String path = (String) context.getConfigParameterValue("File");
			if (path != null && this.getPath() == null) {
				this.setPath(path);
			}
			this.setComparator();			
			this.setTrees();
			this.setAnnotations();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	protected void annotate(JCas cas, String id, int begin, int end,Annotation[] annotations) { }
	
	private void doAnnotate(JCas cas, String id, int index) throws AnalysisEngineProcessException {
		if (this.getAnnotations().get(index) == null || this.getAnnotations().get(index).isEmpty()) {
			throw new AnalysisEngineProcessException();
		} else {
			int begin = this.getBegin(index);
			int end = this.getEnd(index);
			List<Annotation> list = this.getAnnotations().get(index);
			// TODO clean
			// Collections.sort(list, this.getComparator());
			Set<Annotation> set = new TreeSet<Annotation>(this.getComparator());
			set.addAll(list);
			Annotation[] annotations = new Annotation[set.size()];
			set.toArray(annotations);
			this.annotate(cas, id, begin, end, annotations);
		}
	}

	/**
	 * process every annotations of the given CAS index
	 * 
	 */
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		int i = 0;
		Type type = this.getType(cas);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {			
			Annotation annotation = (Annotation) iterator.next();
			this.prepare(i);
			this.process(cas, annotation);
			i++;
		}
	}

	/**
	 * process the given annotation of the given CAS i.e. 
	 * process every current tree constraints and retain 
	 * only those which constraint matches the annotation.
	 * 
	 */
	private void process(JCas cas, Annotation annotation) throws AnalysisEngineProcessException {
		for (Integer index : this.getTrees().keySet()) {
			List<Tree> trees = this.getTrees().get(index); 
			Set<Tree> currents = new HashSet<Tree>();
			for (Tree tree : trees) {
				this.check(cas, annotation, tree, index.intValue());
				currents.add(tree);
			}
			trees.removeAll(currents);
			if (trees.isEmpty()) {
				this.getTrees().remove(index);
				this.getAnnotations().remove(index);
			}
		}
	}

	private void prepare(int index) {
		List<Tree> trees = new CopyOnWriteArrayList<Tree>();
		trees.add(this.getRuleSystem().get());
		this.getTrees().put(index, trees);
	}
	
	private void check(JCas cas, Annotation annotation, Tree current, int index) throws AnalysisEngineProcessException {
		Set<? extends Tree> children = current.get();
		for (Tree child : children) {
			if (child.getConstraint(cas).match(annotation)) {
				this.fire(cas, annotation, child, index);
			} else if (child != current && child.optional()) {
				this.check(cas, annotation, child, index);
			}
		}
	}

	private void fire(JCas cas, Annotation annotation, Tree tree, int index) throws AnalysisEngineProcessException {
		this.forward(tree, index);
		this.add(annotation, index);
		if (tree.terminal()) {
			this.doAnnotate(cas, tree.id(), index);
		}
	}

	private void add(Annotation annotation, int index) {
		List<Annotation> list = this.getAnnotations().get(index);
		if (list == null) {
			list = new ArrayList<Annotation>();
			this.getAnnotations().put(index, list);
		}
		list.add(annotation);
	}
	
	private void forward(Tree tree, int index) {
		if (!tree.get().isEmpty()) {
			Integer id = new Integer(index);
			this.getTrees().get(id).add(tree);
		}
	}

	private Comparator<Annotation> comparator;

	private void setComparator() {
		this.comparator = new AnnotationComparator();
	}
	
	private Comparator<Annotation> getComparator() {
		return this.comparator;
	}
	
	private class AnnotationComparator implements Comparator<Annotation> {

		@Override
		public int compare(Annotation anAnnotation, Annotation anotherAnnotation) {
			int diff = anAnnotation.getBegin() - anotherAnnotation.getBegin();
			if (diff == 0) {
				return anotherAnnotation.getEnd() - anAnnotation.getEnd();
			} else {
				return diff;
			}
		}
		
	}
	
}
