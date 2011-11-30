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
import org.apache.uima.util.Level;

import eu.project.ttc.models.FlxResource;
import eu.project.ttc.types.WordAnnotation;

public class FlxCasConsumer extends JCasAnnotator_ImplBase {

	private FlxResource resource;
	
	private void setResource(FlxResource resource) {
		this.resource = resource;
	}
	
	private FlxResource getResource() {
		return this.resource;
	}
	
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
			FlxResource tabResource = (FlxResource) context.getResourceObject("Resource");
			this.setResource(tabResource);
			String path = (String) context.getConfigParameterValue("FileOrDirectory");
			this.setFile(path);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(WordAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			WordAnnotation annotation = (WordAnnotation) iterator.next();
			String word = annotation.getCoveredText().toLowerCase();
			String tag = annotation.getCategory();
			String lemma = annotation.getLemma();
			this.getResource().index(word, tag, lemma);
		}
	}

	public void collectionProcessComplete() {
		try {
			OutputStream stream = new FileOutputStream(this.getFile());
			this.getResource().release(stream);
			stream.close();
		} catch (IOException e) {
			this.getContext().getLogger().log(Level.SEVERE,e.getMessage());
		}
	}
	
}