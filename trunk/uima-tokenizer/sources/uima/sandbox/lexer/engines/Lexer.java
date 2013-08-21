package uima.sandbox.lexer.engines;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import uima.sandbox.lexer.models.Tree;
import uima.sandbox.lexer.models.Unit;
import uima.sandbox.lexer.resources.SegmentBank;

public class Lexer extends JCasAnnotator_ImplBase {
	
	private SegmentBank bank;
	
	private void setBank(SegmentBank bank) {
		this.bank = bank;
	}
	
	private SegmentBank getBank() {
		return this.bank;
	}
	
	private String type;
	
	private void setType(String type) {
		this.type = type;
	}
	
	private String getType() {
		return this.type;
	}
	
	private Type getType(JCas cas) {
		return cas.getTypeSystem().getType(this.getType());
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getBank() == null) {
				SegmentBank bank = (SegmentBank) context.getResourceObject("Bank");
				this.setBank(bank);
				String path = (String) context.getConfigParameterValue("File");
				if (path != null) {
					InputStream inputStream = new FileInputStream(path);
					this.getContext().getLogger().log(Level.INFO,"Loading " + path);
					this.getBank().load(inputStream);
				}				
			}
			if (this.getType() == null) {
				String type = (String) context.getConfigParameterValue("Type");
				this.setType(type);				
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		this.tokenize(cas);
		Tree<Character> prefixes = this.getBank().get("initial");
		Tree<Character> suffixes = this.getBank().get("final");
		if (prefixes != null && suffixes != null) {
			this.split(cas, prefixes, suffixes);
		}
		Tree<Character> compound = this.getBank().get("compound");
		if (compound != null) {
			this.merge(cas, compound);
			this.clean(cas);			
		}
	}
	
	/**
	 * remove word annotations covered by compound word ones.
	 * 
	 * @param cas the common analysis structure
	 */
	private void clean(JCas cas) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		Type type = this.getType(cas);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			Annotation annotation = iterator.next();
			FSIterator<Annotation> subiterator = index.subiterator(annotation);
			while (subiterator.hasNext()) {
				Annotation a = subiterator.next();
				annotations.add(a);
			}
		}
		for (Annotation a : annotations) {
			a.removeFromIndexes();
		}
	}
	
	/**
	 * create word annotations over the document text
	 * 
	 * @param cas the common analysis structure
	 */
	private void tokenize(JCas cas) {
		Type type = this.getType(cas);
		String text = cas.getDocumentText();
		int begin = 0;
		int length = text.length();
		for (int index = begin; index < length; index++) {
			if (this.hasChanged(text,index)) {
				if (!this.areSpaces(text,begin,index)) {
					AnnotationFS annotation = this.annotate(cas,type,begin,index);
					cas.getCas().addFsToIndexes(annotation);
				}
				begin = index;
			} else {
				if (index == length - 1) {
					if (!this.areSpaces(text,begin,index)) {
						AnnotationFS annotation = this.annotate(cas,type,begin,index);
						cas.getCas().addFsToIndexes(annotation);
					}
				}
			}
		}
	}

	private boolean areSpaces(String text, int begin, int end) {
		boolean space = true;
		for (int index = begin; index < end; index++) {
			char current = text.charAt(index);
			space = space && Character.isWhitespace(current);			
		}
		return space;
	}

	protected boolean hasChanged(String text,int index) {
		if (index == 0) {
			return false;
		} else {
			char previous = text.charAt(index - 1);
			char current = text.charAt(index);
			if (Character.isWhitespace(previous) && !Character.isWhitespace(current)) {
				return true;
			} else if (!Character.isWhitespace(previous) && Character.isWhitespace(current)) {
				return true;
			} else {
				return false;
			} 
		}
	}

	private void merge(JCas cas, Tree<Character> root) {
		String text = cas.getDocumentText();
		Type type = this.getType(cas);
		Map<Tree<Character>,Unit> currents = new HashMap<Tree<Character>,Unit>();
		currents.put(root,new Unit(type,0));
		int length = text.length();
		for (int index = 0; index < length; index++) {
			char ch = Character.toLowerCase(text.charAt(index));
			Character character = new Character(ch);
			this.filter(cas,currents,character,index);
			if (currents.isEmpty()) {
				currents.put(root,new Unit(type,index + 1));
			} else {
				boolean done = false;
				for (Tree<Character> current : currents.keySet()) {
					if (current.leaf()) {
						Unit word = currents.get(current);
						word.update(index + 1);
						done = true;
					}
				}
				if (done) {
					currents.put(root,new Unit(type,index + 1));
				}
			}
		}
	}
	
	private void filter(JCas cas,Map<Tree<Character>,Unit> currents, Character character,int index) {
		Map<Tree<Character>,Unit> nexts = new HashMap<Tree<Character>,Unit>();
		for (Tree<Character> current : currents.keySet()) {
			Tree<Character> next = current.get(character);
			Unit word = currents.get(current);
			if (next == null) {
				word.fire(cas);
			} else {
				nexts.put(next,word);
			}
		}
		currents.clear();
		currents.putAll(nexts);
	}
	
	private void split(JCas cas, Tree<Character> prefixes, Tree<Character> suffixes) {
		List<AnnotationFS> annotations = new ArrayList<AnnotationFS>();
		Type type = this.getType(cas);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			Annotation annotation = iterator.next();
			this.prefixes(cas, type, annotation, prefixes, annotations);
			this.suffixes(cas, type, annotation, suffixes, annotations);
			
		}
		for (AnnotationFS annotation :annotations) {
			cas.getCas().addFsToIndexes(annotation);
		}
	}

	private void prefixes(JCas cas, Type type, Annotation annotation, Tree<Character> prefixes, List<AnnotationFS> annotations) {
		AnnotationFS a = this.prefixes(cas,type,annotation.getBegin(),annotation.getEnd(),annotation.getBegin(),prefixes);
		if (a != null) {
			if (this.fill(cas, type, annotation, a)) {
				annotations.add(a);
				this.prefixes(cas, type, annotation, prefixes, annotations);
			}
		}
	}

	private void suffixes(JCas cas, Type type, Annotation annotation, Tree<Character> suffixes, List<AnnotationFS> annotations) {
		AnnotationFS b = this.suffixes(cas,type,annotation.getBegin(),annotation.getEnd(),annotation.getEnd(),suffixes);
		if (b != null) {
			if (this.fill(cas, type, annotation, b)) {
				annotations.add(b);
				this.suffixes(cas, type, annotation, suffixes, annotations);
			}
		}
	}

	private AnnotationFS prefixes(JCas cas,Type type,int begin,int end,int index,Tree<Character> current) {
		if (index < end) {
			char ch = cas.getDocumentText().charAt(index);
			Character c = Character.toLowerCase(ch);
			Tree<Character> tree = current.get(c);
			if (tree == null) {
				if (current.leaf()) { 
					return this.annotate(cas,type,begin,index);
				} else {
					return null;
				}
			} else {
				return this.prefixes(cas,type,begin,end,index + 1,tree);
			}
		} else {
			return null;
		}
	}
	
	private AnnotationFS suffixes(JCas cas,Type type,int begin,int end,int index,Tree<Character> current) {
		if (index > begin) {
			char ch = cas.getDocumentText().charAt(index - 1);
			Character c = Character.toLowerCase(ch);
			Tree<Character> tree = current.get(c);
			if (tree == null) {
				if (current.leaf()) {
					return this.annotate(cas,type,index,end);
				} else {
					return null;
				}
			} else {
				return this.suffixes(cas,type,begin,end,index - 1,tree);
			}
		} else {
			return null;
		}
	}
	
	private boolean fill(JCas cas,Type type,Annotation coveringAnnotation,AnnotationFS coveredAnnotation) {
		if (coveringAnnotation.getBegin() < coveredAnnotation.getBegin()) {
			coveringAnnotation.setEnd(coveredAnnotation.getBegin());
			return true;
		} else if (coveredAnnotation.getEnd() < coveringAnnotation.getEnd()) {
			coveringAnnotation.setBegin(coveredAnnotation.getEnd());
			return true;
		} else {
			return false;
		}
	}
	
	private AnnotationFS annotate(JCas cas,Type type,int begin,int end) {
		return cas.getCas().createAnnotation(type, begin, end);
	}
	
}
