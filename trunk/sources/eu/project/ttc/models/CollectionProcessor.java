package eu.project.ttc.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import fr.univnantes.lina.uima.engines.Annotator;

/**
 */
public class CollectionProcessor extends Annotator {	
	
	private String commandLine;

	private void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}
	
	private String getCommandLine() {
		return this.commandLine;
	}

	private File inputFile;
	
	private void setInputFile() throws Exception {
		this.inputFile = File.createTempFile("input-",".txt");
		this.inputFile.deleteOnExit();
		this.getLogger().log(Level.INFO,"Creating " + this.inputFile);
	}
	
	public File getInputFile() {
		return this.inputFile;
	}
	
	private File outputFile;
	
	private void setOutputFile() throws Exception {
		this.outputFile = File.createTempFile("output-",".xml");
		this.outputFile.deleteOnExit();
		this.getLogger().log(Level.INFO,"Creating " + this.outputFile);
	}
	
	public File getOutputFile() {
		return this.outputFile;
	}
	
	private Writer writer;
	
	private void setWriter() throws FileNotFoundException {
		this.writer = new OutputStreamWriter(new FileOutputStream(this.getInputFile()));
	}
	
	public Writer getWriter() {
		return this.writer;
	}
			
	@Override
	public void doInitialize() throws ResourceInitializationException {
		try {
			String commandLine = (String) this.getParameter("CommandLine");
			this.setCommandLine(commandLine);
			this.setInputFile();
			this.setOutputFile();
			this.setWriter();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void doProcess(JCas cas) throws AnalysisEngineProcessException {
		try {
			String text = this.getText(cas);
			this.getWriter().write(text);
		} catch (Exception e) { 
			throw new AnalysisEngineProcessException(e);
		}
	}

	@Override
	public void doFinalize() {
		try {
			this.getWriter().close();
			this.getLogger().log(Level.INFO,this.getCommandLine());
			Process proc = Runtime.getRuntime().exec(this.getCommandLine());
			if (proc.waitFor() != 0) {
				this.getLogger().log(Level.SEVERE,"Failure while processing '" + this.getCommandLine() + "'") ;
			}
			proc.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private String getText(JCas cas) throws AnalysisEngineProcessException {	
		try {
			return cas.getDocumentText();			
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
