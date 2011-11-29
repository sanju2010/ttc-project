package eu.project.ttc.engines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.xml.sax.SAXParseException;

public class XmiCasConsumer extends JCasAnnotator_ImplBase {
		
	private File directory;
	
	private void setDirectory(String path) throws IOException {
		File directory = new File(path);
		if (directory.exists()) {
			if (directory.isDirectory()) {
				this.directory = directory;
			} else {
				throw new IOException("This path '" + path + "' isn't a directory");
			}
		} else {
			throw new FileNotFoundException(path);
		}
	}
	
	private File getDirectory() {
		return this.directory;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			String directory = (String) context.getConfigParameterValue("Directory");
			this.setDirectory(directory);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private String retrieve(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation annotation = (SourceDocumentInformation) iterator.next();
			File file = new File(annotation.getUri());
			String name = file.getName();
			int i = name.lastIndexOf('.');
			return name.substring(0, i) + ".xmi";
		} else {
			return null;
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException { 
		try {
			String name = this.retrieve(cas);
			if (name == null) { 
				this.getContext().getLogger().log(Level.WARNING,"Skiping CAS Serialization");
			} else {
				File file = new File(this.getDirectory(), name);
				OutputStream stream = new FileOutputStream(file);
				try {
					XmiCasSerializer.serialize(cas.getCas(), cas.getTypeSystem(), stream);
					this.getContext().getLogger().log(Level.CONFIG,"Serializing " + file.getAbsolutePath());
				} catch (SAXParseException e) {
					this.getContext().getLogger().log(Level.WARNING,"Failure while serializing " + file + "\nCaused by " + e.getClass().getCanonicalName() + ": " + e.getMessage());
				} finally {
					stream.close();
				}
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
