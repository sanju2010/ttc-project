package eu.project.ttc.engines;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.project.ttc.types.TermAnnotation;

public class TbxCasConsumer extends JCasAnnotator_ImplBase {
	
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
			String path = (String) context.getConfigParameterValue("File");
			this.setFile(path);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			Element martif = document.createElement("martif");
			martif.setAttribute("type", "TBX");
			document.appendChild(martif);
			Element header = document.createElement("martifHeader");
			martif.appendChild(header);
			Element fileDesc = document.createElement("fileDesc");
			header.appendChild(fileDesc);
			Element sourceDesc = document.createElement("sourceDesc");
			sourceDesc.setTextContent(this.getFile().getAbsolutePath());
			fileDesc.appendChild(sourceDesc);
			Element text = document.createElement("text");
			martif.appendChild(text);
			Element body = document.createElement("body");
			text.appendChild(body);
			this.process(cas, document, body);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(this.getFile());
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
		
	}

	private void process(JCas cas, Document document, Element body) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			
			Element termEntry = document.createElement("termEntry");
			body.appendChild(termEntry);
			Element langSet = document.createElement("langSet");
			langSet.setAttribute("xml:lang", cas.getDocumentLanguage());
			termEntry.appendChild(langSet);
			Element tig = document.createElement("tig");
			langSet.appendChild(tig);
			Element term = document.createElement("term");
			term.setTextContent(annotation.getCoveredText());
			tig.appendChild(term);
			this.addNote(document, langSet, tig, "complexity", annotation.getComplexity());
			this.addNote(document, langSet, tig, "category", annotation.getCategory());
			this.addNote(document, langSet, tig, "specificity", annotation.getSpecificity());
			this.addNote(document, langSet, tig, "frequency", annotation.getFrequency());
		}
	}
	
	private void addNote(Document document, Element lang, Element element,String type,Object value) {
		Element termNote = document.createElement("termNote");
		element.appendChild(termNote);
		termNote.setAttribute("type", type);
		termNote.setTextContent(value.toString());
	}
	
}