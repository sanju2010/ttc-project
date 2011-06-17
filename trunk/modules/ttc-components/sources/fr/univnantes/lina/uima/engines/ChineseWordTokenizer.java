package fr.univnantes.lina.uima.engines;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cn.ChineseTokenizer;
import org.apache.lucene.util.Attribute;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class ChineseWordTokenizer extends Annotator {
	
	private Tokenizer tokenizer;
	
	private void setTokenizer(InputStream inputStream) throws IOException {
		Reader reader = new InputStreamReader(inputStream);
		if (this.tokenizer == null) {
			this.tokenizer = new ChineseTokenizer(reader);
			this.tokenizer.reset();
		} else {
			this.tokenizer.reset(reader);
		}
	}
	
	private Tokenizer getTokenizer() {
		return this.tokenizer;
	}
	
	@Override
	public void doInitialize() throws ResourceInitializationException { 
	}

	@Override
	public void doFinalize() { }

	@Override
	public void doProcess(JCas cas) throws AnalysisEngineProcessException {
		try {
			String text = cas.getDocumentText();
			InputStream inputStream = new ByteArrayInputStream(text.getBytes());
			this.setTokenizer(inputStream);
		
			while (this.getTokenizer().incrementToken()) {
				this.setOffsets();
				this.setCategory();
				Iterator<Class<? extends Attribute>> iterator = this.getTokenizer().getAttributeClassesIterator();
				while (iterator.hasNext()) {
					Class<? extends Attribute> cl = iterator.next();
					String name = cl.getCanonicalName();
					Attribute attr = this.getTokenizer().getAttribute(cl);
					if (name.endsWith("OffsetAttribute")) {
						this.setOffsets(attr);
					} else if (name.endsWith("TypeAttribute")) {
						this.setCategory(attr);	
					}
				}
				if (this.isDone()) {
					this.doAnnotate(cas);
				}
			}
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private Integer begin;
	
	private int getBegin() {
		return this.begin.intValue();
	}
	
	private Integer end; 
	
	private int getEnd() {
		return this.end.intValue();
	}
	
	private void setOffsets() {
		this.begin = null;
		this.end = null;
	}
	
	private void setOffsets(Attribute attribute) {
		if (attribute != null) {
			try {
				String[] offsets = attribute.toString().split(",");
				if (offsets.length == 2) {
					String startOffset = offsets[0];
					if (startOffset.startsWith("startOffset=")) {
						String begin = startOffset.substring(12);
						this.begin = Integer.valueOf(begin);
					}
					String endOffset = offsets[1];
					if (endOffset.startsWith("endOffset=")) {
						String end = endOffset.substring(10);
						this.end = Integer.valueOf(end);
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				this.setOffsets();
			}
		}
	}
	
	private String category;
	
	private void setCategory() {
		this.category = null;
	}
	
	private void setCategory(Attribute attribute) {
		if (attribute != null) {
			this.category = attribute.toString();
		}
	}
	
	private String getCategory() {
		return this.category;
	}
	
	private boolean isDone() {
		return (this.begin != null) && (this.end != null);
	}

	private void doAnnotate(JCas cas) {
		TokenAnnotation annotation = new TokenAnnotation(cas);
		annotation.setBegin(this.getBegin());
		annotation.setEnd(this.getEnd());
		annotation.setCategory(this.getCategory());
		annotation.addToIndexes();
	}
	
}
