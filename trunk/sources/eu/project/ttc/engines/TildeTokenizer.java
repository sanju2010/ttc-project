package eu.project.ttc.engines;

import java.util.Scanner;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.WordAnnotation;

public class TildeTokenizer extends JCasAnnotator_ImplBase {

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			String text = cas.getDocumentText();
			Scanner scanner = new Scanner(text);
			String delimiter = System.getProperty("line.separator");
			scanner.useDelimiter(delimiter);
			int begin = 0;
			int end = 0;
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNext()) {
				end = begin;
				String line = scanner.next();
				String[] items = line.split("\t");
				if (items.length == 4) {
					String word = items[0];
					// String tag = items[1];
					String lemma = items[2];
					String tag = items[3];
					WordAnnotation annotation = new WordAnnotation(cas, begin, end + word.length());
					annotation.setTag(tag);
					annotation.setLemma(lemma);
					annotation.addToIndexes();
					
				} 
				builder.append(line);
				builder.append(delimiter);
				begin = builder.length();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

}
