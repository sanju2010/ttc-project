package eu.project.ttc.models;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.uima.TreeTaggerAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import fr.univnantes.lina.uima.models.DefaultWrapperHandler;

public class ImsWrapperHandler extends DefaultWrapperHandler {

	@Override
	public void doEncode(JCas cas, Writer writer) throws IOException {
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
	}

	@Override
	public void doDecode(JCas cas, Reader reader) throws IOException {
		
	}
	
}
