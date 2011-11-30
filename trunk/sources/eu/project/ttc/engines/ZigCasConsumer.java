package eu.project.ttc.engines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.VectorAnnotation;

public class ZigCasConsumer extends JCasAnnotator_ImplBase {
	
	private File file;
	
	private void setFile(String path) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) {
				this.file = file;	
			} else {
				throw new IOException("Not a file: " + path);
			}
		} else {
			this.file = file;
		}
	}
	
	private File getFile() {
		return this.file;
	}
	
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			String path = (String) context.getConfigParameterValue("FileOrDirectory");
			this.setFile(path);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			OutputStream stream = new FileOutputStream(this.getFile());
			try {
				StringBuilder builder = new StringBuilder();
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(VectorAnnotation.type);
				FSIterator<Annotation> iterator = index.iterator();
				while (iterator.hasNext()) {
					VectorAnnotation annotation = (VectorAnnotation) iterator.next();
					String term = annotation.getItem();
					Integer occurrences = annotation.getFrequency();
					String context = annotation.getCoveredText();
					builder.append(term);
					builder.append('#');
					builder.append(occurrences);
					builder.append(':');
					builder.append(context);
					builder.append('\n');
				}
				stream.write(builder.toString().getBytes());
				stream.flush();
			} finally {
				stream.close();	
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
		
	}
	
}