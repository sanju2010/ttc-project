package eu.project.ttc.engines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.models.CsvResource;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;

public class CsvCasConsumer extends JCasAnnotator_ImplBase {

	private CsvResource resource;
	
	private void setResource(CsvResource resource) {
		this.resource = resource;
	}
	
	private CsvResource getResource() {
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
	
	private String type;
	
	private void setType(String type) {
		this.type= type;
	}
	
	private Type getType(JCas cas) throws Exception {
		Type type = cas.getRequiredType(this.type);
		Type term = cas.getRequiredType(TermAnnotation.class.getCanonicalName());
		if (cas.getTypeSystem().subsumes(term, type)) {
			return type;
		} else {
			String message = "Type " + type.getName() + " doen't inherit from " + term.getName();
			throw new Exception(message);
		}
	}
	
	
	private void setSortBy(String sortBy) {
		this.getResource().sortBy(sortBy);
	}
		
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			CsvResource tabResource = (CsvResource) context.getResourceObject("Resource");
			this.setResource(tabResource);
			String path = (String) context.getConfigParameterValue("File");
			this.setFile(path);
			String type = (String) context.getConfigParameterValue("Type");
			this.setType(type);
			String sortBy = (String) context.getConfigParameterValue("SortBy");
			this.setSortBy(sortBy);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			Type type = this.getType(cas);
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
			FSIterator<Annotation> iterator = index.iterator();
			while (iterator.hasNext()) {
				TermAnnotation annotation = (TermAnnotation) iterator.next();
				String term = annotation.getCoveredText().toLowerCase();
				String complexity = this.getComplexity(annotation);
				String category = annotation.getCategory();
				double frequency = annotation.getFrequency();
				double specificity = annotation.getSpecificity();
				this.getResource().index(term, complexity, category, frequency, specificity);
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private String getComplexity(TermAnnotation annotation) {
		if (annotation instanceof SingleWordTermAnnotation) {
			SingleWordTermAnnotation swt = (SingleWordTermAnnotation) annotation;
			if (swt.getCompound()) {
				if (swt.getNeoclassical()) {
					return "neoclassical-compound";
				} else {
					return "compound";
				}
			} else {
				return "single-word";
			}
		} else {
			return "multi-word";
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