package uima.sandbox.catcher.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.catcher.models.ObjectFactory;
import uima.sandbox.catcher.models.Parameter;
import uima.sandbox.catcher.models.Parameters;
import uima.sandbox.catcher.models.Rules;
import uima.sandbox.catcher.resources.constraint.And;
import uima.sandbox.catcher.resources.constraint.Eq;
import uima.sandbox.catcher.resources.constraint.Equiv;
import uima.sandbox.catcher.resources.constraint.Exists;
import uima.sandbox.catcher.resources.constraint.False;
import uima.sandbox.catcher.resources.constraint.ForAll;
import uima.sandbox.catcher.resources.constraint.Geq;
import uima.sandbox.catcher.resources.constraint.Gt;
import uima.sandbox.catcher.resources.constraint.Imply;
import uima.sandbox.catcher.resources.constraint.Leq;
import uima.sandbox.catcher.resources.constraint.Lt;
import uima.sandbox.catcher.resources.constraint.Neq;
import uima.sandbox.catcher.resources.constraint.Not;
import uima.sandbox.catcher.resources.constraint.Or;
import uima.sandbox.catcher.resources.constraint.True;
import uima.sandbox.catcher.resources.element.CoveredTextFeature;
import uima.sandbox.catcher.resources.element.Div;
import uima.sandbox.catcher.resources.element.Feature;
import uima.sandbox.catcher.resources.element.Get;
import uima.sandbox.catcher.resources.element.Minus;
import uima.sandbox.catcher.resources.element.Mult;
import uima.sandbox.catcher.resources.element.Plus;
import uima.sandbox.catcher.resources.element.SizeFeature;
import uima.sandbox.catcher.resources.element.Sub;
import uima.sandbox.catcher.resources.element.TypeFeature;
import uima.sandbox.catcher.resources.element.Variable;

public class RuleSystemResource implements RuleSystem {

	private List<Rule> rules;
	
	@Override
	public List<Rule> get() {
		return this.rules;
	}	
	
	@Override
	public void clear() {
		this.rules.clear();
	}	
	
	private void compile() {
		this.rules = new ArrayList<Rule>();
		for (uima.sandbox.catcher.models.Rule rule : this.model.getRule()) {
			String id = rule.getId();
			Map<String, String> params = new HashMap<String, String>();
			Parameters parameters = rule.getParameters();
			for (Parameter parameter : parameters.getParameter()) {
				String name = parameter.getName();
				String type = parameter.getType();
				params.put(name, type);
			}
			Constraint constraint = this.compile(rule.getConstraint());
			this.rules.add(new Rule(id, params, constraint));
		}
	}
	
	private Constraint compile(uima.sandbox.catcher.models.Constraint constraint) {
		if (constraint.getEq() != null) {
			Element left = this.compile(constraint.getEq().getLeft());
			Element right = this.compile(constraint.getEq().getRight());
			return new Eq(left, right);
		} else if (constraint.getLt() != null) {
			Element left = this.compile(constraint.getLt().getLeft());
			Element right = this.compile(constraint.getLt().getRight());
			return new Lt(left, right);
		} else if (constraint.getLeq() != null) {
			Element left = this.compile(constraint.getLeq().getLeft());
			Element right = this.compile(constraint.getLeq().getRight());
			return new Leq(left, right);
		} else if (constraint.getGt() != null) {
			Element left = this.compile(constraint.getGt().getLeft());
			Element right = this.compile(constraint.getGt().getRight());
			return new Gt(left, right);
		} else if (constraint.getGeq() != null) {
			Element left = this.compile(constraint.getGeq().getLeft());
			Element right = this.compile(constraint.getGeq().getRight());
			return new Geq(left, right);
		} else if (constraint.getNeq() != null) {
			Element left = this.compile(constraint.getNeq().getLeft());
			Element right = this.compile(constraint.getNeq().getRight());
			return new Neq(left, right);
		} else if (constraint.getTrue() != null) {
			return new True();
		} else if (constraint.getFalse() != null) {
			return new False();
		} else if (constraint.getNot() != null) {
			Constraint cst = this.compile(constraint.getNot().getConstraint());
			return new Not(cst);
		} else if (constraint.getAnd() != null) {
			Constraint left = this.compile(constraint.getAnd().getLeft());
			Constraint right = this.compile(constraint.getAnd().getRight());
			return new And(left, right);
		} else if (constraint.getOr() != null) {
			Constraint left = this.compile(constraint.getOr().getLeft());
			Constraint right = this.compile(constraint.getOr().getRight());
			return new Or(left, right);
		} else if (constraint.getImply() != null) {
			Constraint left = this.compile(constraint.getImply().getLeft());
			Constraint right = this.compile(constraint.getImply().getRight());
			return new Imply(left, right);
		} else if (constraint.getEquiv() != null) {
			Constraint left = this.compile(constraint.getEquiv().getLeft());
			Constraint right = this.compile(constraint.getEquiv().getRight());
			return new Equiv(left, right);
		} else if (constraint.getForall() != null) {
			String variable = constraint.getForall().getVariable();
			String type = constraint.getForall().getType();
			Constraint cst = this.compile(constraint.getForall().getConstraint());
			return new ForAll(variable, type, cst);
		} else if (constraint.getExists() != null) {
			String variable = constraint.getExists().getVariable();
			String type = constraint.getExists().getType();
			Constraint cst = this.compile(constraint.getExists().getConstraint());
			return new Exists(variable, type, cst);
		} else {
			return null;
		}
	}
	
	private Element compile(uima.sandbox.catcher.models.Element element) {
		if (element.getVariable() != null) {
			return new Variable(element.getVariable().getName());
		} else if (element.getInteger() != null) {
			return new uima.sandbox.catcher.resources.element.Integer(element.getInteger().getValue());
		} else if (element.getString() != null) {
			return new uima.sandbox.catcher.resources.element.String(element.getString().getValue());
		} else if (element.getFeature() != null) {
			Element elem = this.compile(element.getFeature().getElement());
			String name = element.getFeature().getName();
			if (name.equals("coveredText")) {
				return new CoveredTextFeature(elem);				
			} else if (name.equals("size")) {
				return new SizeFeature(elem);
			} else if (name.equals("type")) {
				return new TypeFeature(elem);
			} else {
				return new Feature(elem, name);
			}
		} else if (element.getPlus() != null) {
			Element left = this.compile(element.getPlus().getLeft());
			Element right = this.compile(element.getPlus().getRight());
			return new Plus(left, right);
		} else if (element.getMinus() != null) {
			Element left = this.compile(element.getMinus().getLeft());
			Element right = this.compile(element.getMinus().getRight());
			return new Minus(left, right);
		} else if (element.getMult() != null) {
			Element left = this.compile(element.getMult().getLeft());
			Element right = this.compile(element.getMult().getRight());
			return new Mult(left, right);
		} else if (element.getDiv() != null) {
			Element left = this.compile(element.getDiv().getLeft());
			Element right = this.compile(element.getDiv().getRight());
			return new Div(left, right);
		} else if (element.getGet() != null) {
			Element left = this.compile(element.getGet().getLeft());
			Element right = this.compile(element.getGet().getRight());
			return new Get(left, right);
		} else if (element.getSub() != null) {
			Element elem = this.compile(element.getSub().getElement());
			String type = element.getSub().getType();
			return new Sub(type, elem);
		} else {
			return null;
		}
	}

	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		try {
			this.load(data.getInputStream());
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private Rules model;
	
	@Override
	public void load(InputStream inputStream) throws IOException {
		if (this.model == null) {
			try {
				JAXBContext context = JAXBContext.newInstance(Rules.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				StreamSource source = new StreamSource(inputStream);
				JAXBElement<Rules> root = unmarshaller.unmarshal(source, Rules.class);
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
				JAXBContext context = JAXBContext.newInstance(Rules.class);
				JAXBElement<Rules> element = factory.createRules(this.model);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
				marshaller.marshal(element, outputStream);
			} catch (JAXBException e) {
				throw new IOException(e);
			}
		}
	}
	
}
