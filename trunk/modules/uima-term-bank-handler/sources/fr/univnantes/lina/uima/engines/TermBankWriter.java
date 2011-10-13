package fr.univnantes.lina.uima.engines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.models.TermBank;

public class TermBankWriter extends JCasAnnotator_ImplBase {

	private File file;
	
	private void setFile(File file) {
		this.file = file;
	}
	
	private void setFile(String name) {
		File file = new File(name);
		this.setFile(file);
	}
	
	private File getFile() {
		return this.file;
	}
	
	private TermBank termBank;
	
	private void setTermBank(TermBank termBank) {
		this.termBank = termBank;
	}
	
	private TermBank getTermBank() {
		return this.termBank;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			TermBank termBank = (TermBank) context.getResourceObject("TermBank");
			this.setTermBank(termBank);
			String name = (String) context.getConfigParameterValue("TermBankFile");
			this.setFile(name);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			OutputStream outputStream = new FileOutputStream(this.getFile());
			this.getTermBank().release(outputStream);
			outputStream.close();
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}

	}
	
}
