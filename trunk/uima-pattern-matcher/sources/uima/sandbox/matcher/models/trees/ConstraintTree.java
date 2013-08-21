package uima.sandbox.matcher.models.trees;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.jcas.JCas;

import uima.sandbox.matcher.constraints.Constraint;

public class ConstraintTree implements Tree {
	
	private Constraint constraint;
	
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}
	
	public Constraint getConstraint() {
		return this.constraint;
	}
	
	@Override
	public FSMatchConstraint getConstraint(JCas cas) {
		return this.getConstraint().getConstraint(cas);
	}

	private String id;
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String id() {
		return this.id;
	}
	
	private boolean terminal;
	
	public void enableTerminal(boolean enabled) {
		this.terminal = enabled;
	}
	
	@Override
	public boolean terminal() {
		return this.terminal;
	}
	
	private boolean optional;
	
	public void enableOptional(boolean enabled) {
		this.optional = enabled;
	}
	
	@Override
	public boolean optional() {
		return this.optional;
	}
	
	private Set<ConstraintTree> children;
	
	private void setChildren() {
		this.children = new HashSet<ConstraintTree>();
	}
	
	@Override
	public Set<ConstraintTree> get() {
		return this.children;
	}
		
	public ConstraintTree() {
		this.enableTerminal(false);
		this.enableOptional(false);
		this.setChildren();
	}

	public void fill(ConstraintTree tree,boolean override) { 
		Set<ConstraintTree> trees = new HashSet<ConstraintTree>();
		trees.add(tree);
		this.fill(trees,override);	
	}
	
	public void fill(Set<ConstraintTree> trees,boolean override) {
		if (this.get().isEmpty()) {
			this.get().addAll(trees);
			if (override) {
				this.enableTerminal(false);
			}
		} else {
			if (this.terminal()) {
				this.enableTerminal(false);
			}
			for (ConstraintTree child : this.get()) {
				child.fill(trees,override);
			}
		}
	}
	
}
