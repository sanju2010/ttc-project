package eu.project.ttc.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.uima.LemmaAnnotation;
import org.apache.uima.TermAnnotation;
import org.apache.uima.TermNoteAnnotation;
import org.apache.uima.TreeTaggerAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

public class TermExtractorHandler implements WrapperHandler {

	@Override
	public void doEncode(JCas cas, OutputStream outputStream) throws IOException {
		Writer writer = new OutputStreamWriter(outputStream);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TreeTaggerAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TreeTaggerAnnotation annotation = (TreeTaggerAnnotation) iterator.next();
			String text = annotation.getCoveredText();
			text += "\t" + annotation.getTag();
			text += "\t" + annotation.getLemma();
			text += "\t" + annotation.getBegin();
			text += "\t" + annotation.getEnd();
			text += "\n";
			writer.write(text);
		}
		writer.close();
	}

	@Override
	public void doDecode(JCas cas, InputStream inputStream) throws IOException {
		Scanner scanner = new Scanner(inputStream);
		scanner.useDelimiter("\n");
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] elements = line.split("\t");
			if (elements.length == 5) {
				// String pattern = elements[0];
				// String coveredText = elements[1];
				String lemma = elements[2];
				Integer begin = Integer.valueOf(elements[3]);
				Integer end = Integer.valueOf(elements[4]);
				TermAnnotation termAnnotation = new TermAnnotation(cas,begin.intValue(),end.intValue());
				this.doAnnotate(cas, termAnnotation);
				termAnnotation.addToIndexes();
				LemmaAnnotation lemmaAnnotation = new LemmaAnnotation(cas, begin.intValue(), end.intValue());
				lemmaAnnotation.setValue(lemma);
				lemmaAnnotation.addToIndexes();
			} else {
				throw new IOException("Wrong wrapper output format at line: " + line);
			}
		}
		scanner.close();
	}
	
	private Map<String,String> notes;
	
	private void setNotes() {
		this.notes = new TreeMap<String, String>();
	}
	
	private Map<String,String> getNotes() {
		return this.notes;
	}
	
	public TermExtractorHandler() {
		this.setNotes();
	}
	
	private void setNotes(JCas cas,TermAnnotation term) {
		FSArray array = new FSArray(cas,this.getNotes().size());
		term.setNotes(array);
		int begin = term.getBegin();
		int end = term.getEnd();
		int index = 0;
		for (String note : this.getNotes().keySet()) {
			TermNoteAnnotation complexitynoteAnnotation = new TermNoteAnnotation(cas, begin, end);
			complexitynoteAnnotation.setKind(note);
			complexitynoteAnnotation.setValue(this.getNotes().get(note));
			complexitynoteAnnotation.addToIndexes();
			term.setNotes(index,complexitynoteAnnotation);
			index++;
		}
	}
	
	private void doAnnotate(JCas cas,TermAnnotation term) {
		term.setLanguage(cas.getDocumentLanguage());
		this.getNotes().clear();
		this.getNotes().put("document",this.getDocument(cas));
		this.getNotes().put("begin",Integer.toString(term.getBegin()));
		this.getNotes().put("end",Integer.toString(term.getEnd()));
		if (term.getCoveredText().split("\\s+").length > 1) {
			this.getNotes().put("complexity","multi-word");
		} else {
			this.getNotes().put("complexity","single-word");
		}
		this.setNotes(cas, term);
		term.addToIndexes();
	}
	
	private String getDocument(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation annotation = (SourceDocumentInformation) iterator.next();
			return annotation.getUri();
		} else {
			return "";
		}
	}

}
