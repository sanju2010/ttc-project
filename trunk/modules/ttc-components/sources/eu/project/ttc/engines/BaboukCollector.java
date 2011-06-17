package eu.project.ttc.engines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.DublinCoreDocumentAnnotation;
import org.apache.uima.DublinCorePropertyAnnotation;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import fr.univnantes.lina.uima.engines.Collector;
import fr.univnantes.lina.uima.models.DublinCore;
import fr.univnantes.lina.uima.models.DublinCoreProperty;

public class BaboukCollector extends Collector {

	private Map<String,String> languages;
	
	private void setLanguages() {
		this.languages = new HashMap<String, String>();

		this.languages.put("en","en");
		this.languages.put("english","en");
		this.languages.put("English","en");
		
		this.languages.put("fr","fr");
		this.languages.put("french","fr");
		this.languages.put("French","fr");
		this.languages.put("français","fr");
		this.languages.put("Français","fr");
		
		this.languages.put("de","de");
		this.languages.put("deutsch","de");
		this.languages.put("Deutsch","de");
		this.languages.put("german","de");
		this.languages.put("German","de");
		
		this.languages.put("es","es");
		this.languages.put("español","es");
		this.languages.put("Español","es");
		this.languages.put("spanish","es");
		this.languages.put("Spanish","es");
		
		this.languages.put("ru","ru");
		this.languages.put("русский","ru");
		this.languages.put("Русский","ru");
		this.languages.put("russian","ru");
		this.languages.put("Russian","ru");

		this.languages.put("lv","lv");
		this.languages.put("latviešu","lv");
		this.languages.put("Latviešu","lv");
		this.languages.put("latvian","lv");
		this.languages.put("Latvian","lv");

		this.languages.put("zh","zh");
		this.languages.put("chinese","zh");
		this.languages.put("Chinese","zh");
		this.languages.put("中文","zh");
		this.languages.put("漢語","zh");
		
	}
	
	private Map<String,String> getLanguages() {
		return this.languages;
	}
	
	@Override
	public void doInitialize() throws ResourceInitializationException {
		this.doCollect();
		this.setLanguages();
		this.setMap();
	}

	@Override
	public void doProcess(JCas cas) throws Exception {
        try {
        	File file = (File) this.getDocument();
        	this.getLogger().log(Level.INFO,"Processing " + file.getAbsolutePath());
        	this.addDublinCore(file);
        	File document = this.getCrawledDocument(file);
        	String text = this.getCrawledDocumentText(document);
        	String language = this.getCrawledDocumentLanguage(file);
        	cas.setDocumentText(this.doClean(text));
        	cas.setDocumentLanguage(language);
        	this.doAnnotate(cas,file,text);
        } catch (Exception e) { 
        	throw new CollectionException(e); 
        } 
	}

	@Override
	public void doFinalize() {
	}

	private void addDublinCore(File file) throws Exception { 
		DublinCore dc = new DublinCore(file);
		dc.load();
		this.map.put(file,dc);
	}

	
	private String getCrawledDocumentText(File file) throws Exception {
		StringBuffer text = new StringBuffer(1048576);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		for (String line = buffer.readLine(); line != null; line = buffer.readLine()) {
			text.append(line);
			text.append('\n');
		}
		buffer.close();
		return text.toString();
	}
	
	private String getCrawledDocumentLanguage(File file) throws Exception {
		DublinCore dc = this.map.get(file);
		if (dc == null) {
			throw new NullPointerException();
		} else {
			DublinCoreProperty property = dc.getProperty("dc:language");
			String language = property.getValue();
			return this.getLanguages().get(language);
		}
	}
	
	private File getCrawledDocument(File file) throws NullPointerException, FileNotFoundException {
		File parent = file.getParentFile();
		String name = this.getCrawledDocument(file.getName());
		if (name == null) {
			throw new NullPointerException(file.getName());
		}
		File crawled = new File(parent,name);
		if (!crawled.exists()) {
			throw new FileNotFoundException(crawled.getAbsolutePath());
		}
		return crawled;
	}
	
	private String getCrawledDocument(String name) {
		if (name.endsWith(".xml")) {
			int len = name.length() - 4;
			return name.substring(0,len) + ".txt";
		} else {
			return null;
		}
	}
	
	public void doAnnotate(JCas cas,File file,String text) throws Exception {
		SourceDocumentInformation info = new SourceDocumentInformation(cas);
		info.setBegin(0);
		info.setEnd(text.length());
		info.setDocumentSize(text.length());
		info.setUri(file.toURI().toString());
		info.addToIndexes();
		DublinCore dc = this.map.get(file);
		if (dc == null) {
			String msg = "No Dublin Core found for " + file;
			throw new Exception(msg);
		} else {
			int length = dc.getProperties().getSize();
			FSArray array = new FSArray(cas,length);
			DublinCoreDocumentAnnotation dublinCore = new DublinCoreDocumentAnnotation(cas);
			dublinCore.setBegin(0);
			dublinCore.setEnd(text.length());
			dublinCore.setMetadata(array);
			for (int index = 0; index < length; index++) {
				Object element = dc.getProperties().get(index);
				if (element instanceof DublinCoreProperty) {
					DublinCoreProperty prop = (DublinCoreProperty) element;
					DublinCorePropertyAnnotation property = new DublinCorePropertyAnnotation(cas);
					property.setName(prop.getName());
					property.setValue(prop.getValue());
					property.setScheme(prop.getScheme());
					property.setLang(prop.getLanguage());
					property.addToIndexes();
					dublinCore.setMetadata(index,property);
					if (prop.getName().equals("dc:language")) {
						String language = prop.getValue();
						String lang = this.getLanguages().get(language);
						dublinCore.setLanguage(lang);
					}
				}
			}
			dublinCore.addToIndexes();
		}
	}
	
	private Map<File,DublinCore> map;
	
	private void setMap() {
		this.map = new TreeMap<File,DublinCore>();
	}
	
	private FileFilter filter = new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				return file.getName().endsWith(".xml");
			}
		}
		
	};
	
	private void doCollect(File directory) {
		if (directory.exists()) {
			if (directory.isDirectory()) {				
				List<File> files = new ArrayList<File>();
				files.addAll(Arrays.asList(directory.listFiles(this.filter)));
				Collections.sort(files);
				for (File file : files) {
					if (file.isDirectory()) {
						this.doCollect(file);
					} else {
						this.addDocument(file);
					}
				}
			}
		}
	}
	
	private void doCollect(String path) {
		File directory = new File(path);
		this.doCollect(directory);
	}
	
	private String doClean(String l) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < l.length(); i++) {
			char c = l.charAt(i);
			if (c == '’') {
				sb.append("'");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public void doCollect() throws ResourceInitializationException {
		String directory = ((String) this.getParameter("Directory"));
		if (directory == null || directory.isEmpty()) {
			String msg = "The parameter 'Directory' must be set.";
			Exception e = new Exception(msg);
			throw new ResourceInitializationException(e);
		}
		this.doCollect(directory);
	}
	
}
