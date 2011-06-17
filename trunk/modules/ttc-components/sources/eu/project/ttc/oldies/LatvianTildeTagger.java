package eu.project.ttc.oldies;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.apache.uima.TokenAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.tilde.tagger.TaggerService;
import com.tilde.tagger.TaggerServiceSoap;

import fr.univnantes.lina.uima.engines.Annotator;

public class LatvianTildeTagger extends Annotator {

	private TaggerService service;
	
	private void setService() {
		this.service = new TaggerService();
	}
	
	private TaggerService getService() {
		return this.service;
	}
	
	private TaggerServiceSoap port;
	
	private void setPort() {
		this.port = this.getService().getTaggerServiceSoap();
		
	}
	
	private TaggerServiceSoap getPort() {
		return this.port;
	}
	
	@Override
	public void doInitialize() throws ResourceInitializationException {
		this.setService();
		this.setPort();
	}

	@Override
	public void doProcess(JCas cas) throws AnalysisEngineProcessException {
		String input = this.doEncode(cas);
		String output = this.getPort().posTagger(input,"treetagger","lv");
		this.doDecode(cas,output);
	}

	private void doDecode(JCas cas, String output) {
		ByteArrayInputStream stream = new ByteArrayInputStream(output.getBytes());
		Scanner scanner = new Scanner(stream);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] info = line.split("\t+");
			if (info.length == 3) {
				String word = info[0];
				String tag = info[1];
				String lemma = info[2];
				this.doAnnotate(cas,word,tag,lemma);
			} else {
				// TODO
			}
		}
	}

	private void doAnnotate(JCas cas, String word, String tag, String lemma) {
		// TODO Auto-generated method stub
	}

	private String doEncode(JCas cas) {
		StringBuffer buffer = new StringBuffer(1048576);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TokenAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			Annotation annotation = iterator.next();
			String token = annotation.getCoveredText();
			buffer.append(token + '\n');
		}
		return buffer.toString();
	}

	@Override
	public void doFinalize() { }

}
