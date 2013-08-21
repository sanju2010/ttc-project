package uima.sandbox.matcher.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.matcher.constraints.AndConstraint;
import uima.sandbox.matcher.constraints.EqConstraint;
import uima.sandbox.matcher.constraints.NeqConstraint;
import uima.sandbox.matcher.constraints.OrConstraint;
import uima.sandbox.matcher.constraints.TypeConstraint;
import uima.sandbox.matcher.models.Alternative;
import uima.sandbox.matcher.models.And;
import uima.sandbox.matcher.models.Annotation;
import uima.sandbox.matcher.models.Constraint;
import uima.sandbox.matcher.models.Eq;
import uima.sandbox.matcher.models.Loop;
import uima.sandbox.matcher.models.Neq;
import uima.sandbox.matcher.models.ObjectFactory;
import uima.sandbox.matcher.models.Option;
import uima.sandbox.matcher.models.Or;
import uima.sandbox.matcher.models.Pattern;
import uima.sandbox.matcher.models.Patterns;
import uima.sandbox.matcher.models.Sequence;
import uima.sandbox.matcher.models.trees.ConstraintTree;

public class RuleSystemResource implements RuleSystem {

	private ConstraintTree tree;
	
	private void compile() {
		this.tree = new ConstraintTree();
		Set<ConstraintTree> trees = new HashSet<ConstraintTree>();
		for (Pattern pattern : this.model.getPattern()) {
			ConstraintTree tree = new ConstraintTree();
			tree.enableTerminal(false);
			tree.setId(pattern.getId());
			tree.setConstraint(null);
			this.compile(tree, pattern.getId(), pattern);
			trees.add(tree);
		}
		this.tree.fill(trees,true);
	}

	private void compile(ConstraintTree root, String id, Pattern pattern) {
		if (pattern.getAnnotation() != null) {
			this.compile(root, id, pattern.getAnnotation());
		} else if (pattern.getSequence() != null) {
			Sequence sequence = pattern.getSequence();
			this.compile(root, id, sequence);
		} else if (pattern.getAlternative() != null) {
			Alternative alternative = pattern.getAlternative();
			this.compile(root, id, alternative);
		} else if (pattern.getOption() != null) {
			Option option = pattern.getOption();
			this.compile(root, id, option);
		} else if (pattern.getLoop() != null) {
			Loop loop = pattern.getLoop();
			this.compile(root, id, loop);
		}
	}

	private void compile(ConstraintTree root, String id, Loop loop) {
		if (loop.isMandatory()) {
			ConstraintTree tree = new ConstraintTree();
			tree.enableTerminal(true);
			tree.setId(id);
			tree.enableOptional(false);
			tree.setConstraint(null);
			this.compile(tree, id, loop.getPattern());
			root.fill(tree,true);
		} 
		ConstraintTree tree = new ConstraintTree();
		tree.enableTerminal(true);
		tree.setId(id);
		tree.enableOptional(true);
		tree.setConstraint(null);
		this.compile(tree, id, loop.getPattern());
		// tree.fill(tree,false);
		root.fill(tree,false);
	}

	private void compile(ConstraintTree root, String id, Option option) {
		ConstraintTree tree = new ConstraintTree();
		tree.enableTerminal(false);
		tree.setId(id);
		tree.enableOptional(true);
		tree.setConstraint(null);
		this.compile(tree, id, option.getPattern());
		root.fill(tree,false);
	}

	private void compile(ConstraintTree root, String id, Alternative alternative) {
		Set<ConstraintTree> trees = new HashSet<ConstraintTree>();
		for (Pattern p : alternative.getPatterns().getPattern()) {
			ConstraintTree tree = new ConstraintTree();
			tree.enableTerminal(false);
			tree.setId(id);
			tree.setConstraint(null);
			this.compile(tree, id, p);
			trees.add(tree);
		}
		root.fill(trees,true);
	}

	private void compile(ConstraintTree root, String id, Sequence sequence) {
		root.setId(id);			
		for (Pattern p : sequence.getPatterns().getPattern()) {
			this.compile(root, id, p);
		}
	}
	
	private void compile(ConstraintTree root, String id, Annotation annotation) {
		String type = annotation.getType();
		TypeConstraint t = new TypeConstraint();
		t.setName(type);
		uima.sandbox.matcher.constraints.Constraint c = this.get(annotation.getConstraint(), type);
		AndConstraint constraint = new AndConstraint();
		constraint.getConstraints().add(t);
		constraint.getConstraints().add(c);
		this.compile(root, id, constraint);
	}
	
	private uima.sandbox.matcher.constraints.Constraint get(Constraint constraint, String type) { 
		if (constraint.getEq() != null) {
			return this.get(constraint.getEq(), type);
		} else if (constraint.getNeq() != null) {
			return this.get(constraint.getNeq(), type);
		} else if (constraint.getAnd() != null) {
			return this.get(constraint.getAnd(), type);
		} else if (constraint.getOr() != null) {
			return this.get(constraint.getOr(), type);
		} else {
			return null;
		}
	}
	
	private uima.sandbox.matcher.constraints.Constraint get(Or or, String type) { 
		OrConstraint constraint = new OrConstraint();
		for (Constraint c : or.getConstraints().getConstraint()) {
			constraint.getConstraints().add(this.get(c, type));
		}
		return constraint;
	}
	
	private uima.sandbox.matcher.constraints.Constraint get(And and, String type) { 
		AndConstraint constraint = new AndConstraint();
		for (Constraint c : and.getConstraints().getConstraint()) {
			constraint.getConstraints().add(this.get(c, type));
		}
		return constraint;
	}
	
	private uima.sandbox.matcher.constraints.Constraint get(Neq neq, String type) { 
		String name = neq.getKey();
		String value = neq.getValue(); 
		NeqConstraint constraint = new NeqConstraint(name, value);
		constraint.setType(type);
		return constraint;
	}
	
	private uima.sandbox.matcher.constraints.Constraint get(Eq eq, String type) { 
		String name = eq.getKey();
		String value = eq.getValue(); 
		EqConstraint constraint = new EqConstraint(name, value);
		constraint.setType(type);
		return constraint;
	}
	
	private void compile(ConstraintTree root, String id, uima.sandbox.matcher.constraints.Constraint constraint) {
		if (root.getConstraint() == null) {
			root.setConstraint(constraint);
			root.enableTerminal(true);
			root.setId(id);
		} else {
			ConstraintTree tree = new ConstraintTree();
			tree.enableTerminal(true);
			tree.setId(id);
			tree.setConstraint(constraint);
			root.fill(tree,true);
		}
	}

	@Override
	public synchronized ConstraintTree get() {
		return this.tree;
	}
	
	@Override
	public void clear() {
		this.get().get().clear();
	}

	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		try {
			this.load(data.getInputStream());
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		} 
	}

	private Patterns model;
	
	@Override
	public void load(InputStream inputStream) throws IOException {
		if (this.model == null) {
			try {
				JAXBContext context = JAXBContext.newInstance(Patterns.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				StreamSource source = new StreamSource(inputStream);
				JAXBElement<Patterns> root = unmarshaller.unmarshal(source, Patterns.class);
				this.model = root.getValue();
				this.compile();
			} catch (JAXBException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void store(OutputStream outputStream) throws IOException {
		if (this.model != null) {
			try {
				ObjectFactory factory = new ObjectFactory();
				JAXBContext context = JAXBContext.newInstance(Patterns.class);
				JAXBElement<Patterns> element = factory.createPatterns(this.model);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
				marshaller.marshal(element, outputStream);
			} catch (JAXBException e) {
				throw new IOException(e);
			}
		}
	}	
	
}
