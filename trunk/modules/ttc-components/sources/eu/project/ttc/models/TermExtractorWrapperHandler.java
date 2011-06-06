package eu.project.ttc.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import org.apache.uima.TermAnnotation;
import org.apache.uima.TreeTaggerAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import fr.univnantes.lina.uima.models.DefaultWrapperHandler;

public class TermExtractorWrapperHandler extends DefaultWrapperHandler {

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
			if (elements.length >= 6) {
				String pattern = elements[0];
				String coveredText = elements[1];
				String lemma = elements[2];
				Integer begin = Integer.valueOf(elements[3]);
				Integer end = Integer.valueOf(elements[4]);
				String id = elements[5];
				TermAnnotation termAnnotation = new TermAnnotation(cas,begin.intValue(),end.intValue());
				termAnnotation.setId(id);
				/*
				termAnnotation.setDocument(this.getDocument(cas));
				termAnnotation.setPartOfSpeech(pattern);
				termAnnotation.setLemma(lemma);
				*/
				if (!termAnnotation.getCoveredText().equals(coveredText)) {
					System.out.println(coveredText + " != " + termAnnotation.getCoveredText());
				}
				termAnnotation.addToIndexes();
			} else {
				throw new IOException("Wrong wrapper output format at line: " + line);
			}
		}
		scanner.close();
	}

	private String getDocument(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation annotation = (SourceDocumentInformation) iterator.next();
			return annotation.getUri();
		} else {
			return null;
		}
	}
	
}
