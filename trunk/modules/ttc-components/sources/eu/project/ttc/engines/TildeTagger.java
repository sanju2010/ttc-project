package eu.project.ttc.engines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.uima.TreeTaggerAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.engines.Annotator;

public class TildeTagger extends Annotator {

	private static final String METHOD = "treetagger";

	private static final String LANG = "lv";
	
	private File tagger;
	
	private void setTagger(String tagger) throws IOException {
		if (tagger == null) {
			throw new IOException("The file isn't defined.");
		} else {
			File f = new File(tagger);
			if (f.exists()) {
				if (f.isFile()) {
					if (f.canExecute()) {
						this.tagger = f;
					} else {
						throw new IOException("Tilde's tagger " + tagger + " isn't executable.");
					}
				} else {
					throw new IOException("Tilde's tagger " + tagger + " isn't a file.");
				}
			} else {
				throw new IOException("Tilde's tagger " + tagger + " doesn't exist.");
			}
		}
	}
	
	private File getTagger() {
		return this.tagger;
	}
	
	private String method;

	private void setMethod() {
		this.method = METHOD;
	}
	
	private void setMethod(String method) {
		this.method = method;
	}
	
	private String getMethod() {
		return this.method;
	}

	private String language;

	private void setLanguage() {
		this.language = LANG;
	}
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
 	
	private String username;
	
	private void setUsername(String username) {
		this.username = username;
	}
	
	private String getUsername() {
		return this.username;
	}
	
	private String password;
	
	private void setPassword(String password) {
		this.password = password;
	}
	
	private String getPassword() {
		return this.password;
	}
	
	/*
	private Process process;
	
	private void setProcess() throws IOException {
		List<String> commandLine = new ArrayList<String>();
		commandLine.add(this.getTagger().getAbsolutePath());
		commandLine.add(this.getMethod());
		commandLine.add(this.getLanguage());
		commandLine.add(this.getUsername());
		commandLine.add(this.getPassword());
		ProcessBuilder builder = new ProcessBuilder(commandLine);
		this.process = builder.start();
	}
	
	private Process getProcess() {
		return this.process;
	}
	*/
	
	private String commandLine;
	
	private void setCommandLine() {
		this.commandLine = this.getTagger().getAbsolutePath();
		this.commandLine += " " + this.getMethod();
		this.commandLine += " " + this.getLanguage();
		this.commandLine += " " + this.getUsername();
		this.commandLine += " " + this.getPassword();
	}
	
	private String getCommandLine(File file) {
		return this.commandLine + " " + file.getAbsolutePath();
	}
	
	@Override
	public void doInitialize() throws ResourceInitializationException {
		try {
			String tagger = (String) this.getParameter("TaggerFile");
			this.setTagger(tagger);
			// String method = (String) this.getParameter("Method");
			// this.setMethod(method);
			this.setMethod();
			// String language = (String) this.getParameter("Language");
			// this.setLanguage(language);
			this.setLanguage();
			String username = (String) this.getParameter("Username");
			this.setUsername(username);
			String password = (String) this.getParameter("Password");
			this.setPassword(password);
			this.setCommandLine();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void doProcess(JCas cas) throws AnalysisEngineProcessException {
		Process proc = null;
		try {
			File file = this.getFile(cas.getDocumentText().getBytes());
			String commandLine = this.getCommandLine(file);
			proc = Runtime.getRuntime().exec(commandLine);
    		
    		if (proc.waitFor() == 0) {
    			this.doProcess(cas,proc.getInputStream());
    		} else {
    			this.doError(proc.getErrorStream());
    		}
    		
		} catch (Exception e) { 
			throw new AnalysisEngineProcessException(e);
		} finally {
			proc.destroy();
		}
	}

	private void doError(InputStream inputStream) throws IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader buffer = new BufferedReader(reader);
		for (String line = buffer.readLine(); line != null; line = buffer.readLine()) {
			System.err.println(line);
		}
		buffer.close();
	}

	private File getFile(byte[] bytes) throws IOException, FileNotFoundException {
		File file = File.createTempFile("tilde-tagger-uima-input-",".txt");
		file.deleteOnExit();
		OutputStream os = new FileOutputStream(file);
		os.write(bytes);
		os.flush();
		os.close();
		return file;
	}

	private void doProcess(JCas cas,InputStream inputStream) throws IOException {
		int start = 0;
		String text = cas.getDocumentText();
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader buffer = new BufferedReader(reader);
		for (String line = buffer.readLine(); line != null; line = buffer.readLine()) {
			String[] info = line.split("\t+");
			if (info.length >= 3) {
				
				String word = info[0];
				String tag = info[1];
				String lemma = info[2];
				// String multext = info[3];
				
				while (!text.substring(start,start + word.length()).equals(word)) {
					start++;
				}
				
				TreeTaggerAnnotation tt = new TreeTaggerAnnotation(cas);

				tt.setBegin(start);
				start += word.length();
				tt.setEnd(start);
				
				tt.setTag(tag);
				tt.setLemma(lemma);
				tt.addToIndexes();
			} 
		}    			
		buffer.close();
	}
		
	@Override
	public void doFinalize() { }

}


