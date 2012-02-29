package fr.free.rocheteau.jerome.dunamis.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.AnalysisEngine;
import fr.free.rocheteau.jerome.dunamis.models.CollectionProcessingEngine;
import fr.free.rocheteau.jerome.dunamis.models.CollectionReader;
import fr.free.rocheteau.jerome.dunamis.models.Package;;

public class Settings {
	
	private Properties properties;
	
	private void setProperties() {
		this.properties = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/dunamis.libraries");
		if (in != null) {
			try {
				this.properties.loadFromXML(in);
			} catch (Exception e) {
				Dunamis.error(e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					Dunamis.error(e);
				}	
			}
		} 
	}
	
	private Properties getProperties() {
		return this.properties;
	}
	
	private void loadProperties() throws ResourceAccessException, IOException {
		Iterator<Entry<Object,Object>> properties = this.getProperties().entrySet().iterator();
		while (properties.hasNext()) {
			Entry<Object,Object> entry = properties.next();
			if (entry.getValue() instanceof String) {
				String string = (String) entry.getValue();
				String name = string.replaceAll("\\.","/") + ".xml";
				URL url = ClassLoader.getSystemResource(name);
				this.loadComponent(url);
			}
		}
	}
	
	private DefaultTreeModel collectionReaders;
	
	public void setCollectionReaders(DefaultTreeModel model) {
		this.collectionReaders = model;
	} 
	
	private DefaultTreeModel getCollectionReaders() {
		return this.collectionReaders;
	}
	
	private DefaultTreeModel analysisEngines;
	
	public void setAnalysisEngines(DefaultTreeModel model) {
		this.analysisEngines = model;
	} 
	
	private DefaultTreeModel getAnalysisEngines() {
		return this.analysisEngines;
	}
	
	/*
	private DefaultListModel typeSystems;
	
	private void setTypeSystems() {
		this.typeSystems = new DefaultListModel();
	} 
	
	public DefaultListModel getTypeSystems() {
		return this.typeSystems;
	}
	
	private DefaultListModel casConsumers;
	
	private void setCasConsumers() {
		this.casConsumers = new DefaultListModel();
	} 
	
	public DefaultListModel getCasConsumers() {
		return this.casConsumers;
	}
	
	private DefaultListModel flowControllers;
	
	private void setFlowControllers() {
		this.flowControllers = new DefaultListModel();
	} 
	
	public DefaultListModel getFlowControllers() {
		return this.flowControllers;
	}
	*/
	
	private void addAnalysisEngine(XMLInputSource source) throws InvalidXMLException {
		XMLParser parser = UIMAFramework.getXMLParser();
		AnalysisEngineDescription ae = parser.parseAnalysisEngineDescription(source);		
		AnalysisEngine analysisEngine = new AnalysisEngine(ae,null);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(analysisEngine);
		MutableTreeNode root = (MutableTreeNode) this.getAnalysisEngines().getRoot();
		int index = this.getAnalysisEngines().getChildCount(root);
		this.getAnalysisEngines().insertNodeInto(node,root,index);
		UIMAFramework.getLogger().log(Level.CONFIG,"Adding " + analysisEngine.toString() + " to available Analysis Engines");
	}

	private void addCollectionReader(XMLInputSource source) throws InvalidXMLException	{
		XMLParser parser = UIMAFramework.getXMLParser();
		CollectionReaderDescription cr = parser.parseCollectionReaderDescription(source);
		CollectionReader collectionReader = new CollectionReader(cr,null);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(collectionReader);
		MutableTreeNode root = (MutableTreeNode) this.getCollectionReaders().getRoot();
		int index = this.getCollectionReaders().getChildCount(root);
		this.getCollectionReaders().insertNodeInto(node,root,index);
		UIMAFramework.getLogger().log(Level.CONFIG,"Adding " + collectionReader.toString() + " to available Collection Readers");
	}
	
	/*
	private void addCasConsumer(XMLInputSource source) throws InvalidXMLException {
		XMLParser parser = UIMAFramework.getXMLParser();
		CasConsumerDescription cc = parser.parseCasConsumerDescription(source);
		this.getCasConsumers().addElement(cc);
		this.getStringBuilder().append("Adding " + cc.getMetaData().getName() + " to available CAS Consumers");
	}
	
	private void addTypeSystem(XMLInputSource source) throws InvalidXMLException {
		XMLParser parser = UIMAFramework.getXMLParser();
		TypeSystemDescription ts = parser.parseTypeSystemDescription(source);
		this.getTypeSystems().addElement(ts);
		this.getStringBuilder().append("Adding " + ts.getName() + " to available Type Systems");
	}
	
	private void addFlowController(XMLInputSource source)	throws InvalidXMLException {
		XMLParser parser = UIMAFramework.getXMLParser();
		 FlowControllerDescription fc = parser.parseFlowControllerDescription(source);
		 this.getFlowControllers().addElement(fc);
		 this.getStringBuilder().append("Adding " + fc.getMetaData().getName() + " to available Flow Controllers");
	}
	*/
	
	private void splitAnalysisEngines() {
		UIMAFramework.getLogger().log(Level.CONFIG,"Spliting Analysis Engines between primitive and aggregate ones...");
		DefaultMutableTreeNode primitives = new DefaultMutableTreeNode("Primitive Analysis Engines");	
		DefaultMutableTreeNode aggregates = new DefaultMutableTreeNode("Aggregate Analysis Engines");
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getAnalysisEngines().getRoot();
		int count = root.getChildCount();
		for (int index = 0; index < count; index++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(index);
			DefaultMutableTreeNode o = (DefaultMutableTreeNode) node.clone();
			AnalysisEngine ae = (AnalysisEngine) node.getUserObject();
			if (ae.getDescription().isPrimitive()) {
				primitives.insert(o,primitives.getChildCount());
			} else {
				aggregates.insert(o,aggregates.getChildCount());
			}
		}
		this.embedAnalysisEngines(aggregates,primitives);
		this.embedAnalysisEngines(aggregates,aggregates);
		root.removeAllChildren();
		root.insert(aggregates,0);
		root.insert(primitives,1);
		this.getCollectionReaders().reload();
		this.getAnalysisEngines().reload();
	}
	
	private void embedAnalysisEngines(DefaultMutableTreeNode aggregates, DefaultMutableTreeNode primitives) {
		for (int index = 0; index < aggregates.getChildCount(); index++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) aggregates.getChildAt(index);
			AnalysisEngine aggregate = (AnalysisEngine) node.getUserObject();
			Map<String, MetaDataObject> specs = aggregate.getDescription().getDelegateAnalysisEngineSpecifiersWithImports();
			for (String key : specs.keySet()) {
				DefaultMutableTreeNode primitive = this.getPrimitiveAnalysisEngine(primitives,key);
				if (primitive != null) {
					node.insert(primitive, node.getChildCount());
				}
			}
		}
	}

	private DefaultMutableTreeNode getPrimitiveAnalysisEngine(DefaultMutableTreeNode primitives, String key) {
		for (int index = 0; index < primitives.getChildCount(); index++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) primitives.getChildAt(index);
			AnalysisEngine primitive = (AnalysisEngine) node.getUserObject();
			String name = primitive.getDescription().getMetaData().getName().replaceAll(" ","");
			if (name.equals(key)) {
				return (DefaultMutableTreeNode) node.clone();
			} 
		}
		return null;
	}

	private DefaultListModel packages;
	
	private void setPackages() {
		this.packages = new DefaultListModel();
	} 
	
	private DefaultListModel getPackages() {
		return this.packages;
	}
	
	private DefaultListModel collectionProcessingEngines;
	
	public void setCollectionProcessingEngines(DefaultListModel model) {
		this.collectionProcessingEngines = model;
	} 
	
	private DefaultListModel getCollectionProcessingEngines() {
		return this.collectionProcessingEngines;
	}
	
	private File settingFile;
	
	private void setSettingFile() {
		String home = System.getProperty("user.home");
		String name = ".dunamis.settings";
		String os = System.getProperty("os.name");
		if (os.startsWith("Windows")) {
			name = "dunamis.settings";
		}
		String path = home + File.separator + name;
		File settings = new File(path);
		this.settingFile = settings;
	}

	private File getSettingFile() {
		return this.settingFile;
	}
	
	public Settings() {
		this.setProperties();
		this.setSettingFile();
		this.setPackages();
	}
	
	public String getStatus() {
		String status = null;
		DefaultMutableTreeNode crRoot = (DefaultMutableTreeNode) this.getCollectionReaders().getRoot();
		DefaultMutableTreeNode aeRoot = (DefaultMutableTreeNode) this.getAnalysisEngines().getRoot();
		if (crRoot.getChildCount() == 0) { 
			status = "There is no Collection Reader loaded!\n";
			status +="Please load a package with your components (Edit > Packages or Ctl-Alt-P)\n";
		} else if (aeRoot.getChildAt(0).getChildCount() == 0 && 
				aeRoot.getChildAt(1).getChildCount() == 0) {
			status = "There is no Analysis Engine loaded!\n";
			status +="Please load a package with your components (Edit > Packages or Ctl-Alt-P)\n";
		} else if (this.getCollectionProcessingEngines().getSize() == 0) {
			status = "There is no Collection Processing Engine declared!\n";
			status +="Please create an new Collection Processing Engine (File > New or Ctl-N)\n";
			status +="or open an existing one (File > Open or Ctl-O)\n";
		} 
		return status;
	}
	
	private void loadClassPath() throws Exception {
		String pathSeparator = System.getProperty("path.separator");
		String classPath = System.getProperty("java.class.path");
		String[] paths = classPath.split(pathSeparator);
		for (String path : paths) {
			File file = new File(path);
			this.loadPackage(file);
			this.loadDescriptors(file);
		}
	}
	
	public void load() throws Exception {
		this.loadProperties();
		this.loadClassPath();
		this.loadPackages();
		this.loadDescriptors();
		this.splitAnalysisEngines();
		this.loadEngines();
	}
	
	private void loadDescriptors(File file) throws IOException, ResourceAccessException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					this.loadDescriptors(f);
				} else if (f.getName().endsWith(".xml")) {
					URL url = f.toURI().toURL();
					this.loadComponent(url);
				}
			}
		} else {
			JarFile jar = new JarFile(file);
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.isDirectory()) { 
					String name = entry.getName();
					if (name.endsWith(".xml")) {
						URL url = ClassLoader.getSystemResource(name);
						this.loadComponent(url);
					}
				}
			}
		}
	}

	private XMLInputSource getSource(URL url) throws IOException {
		return new XMLInputSource(url);
	}
	
	private void loadComponent(URL url) {
		try {
			if (url == null) { 
				throw new NullPointerException();
			} else {
				try { 
					this.addAnalysisEngine(this.getSource(url));
				} catch (InvalidXMLException e1) {
					try {
						this.addCollectionReader(this.getSource(url));
					} catch (InvalidXMLException e2) {
						throw new ResourceAccessException();
					}
				}
			}
		} catch (NullPointerException e) {
			// System.err.println("Null Pointer Error");
		} catch (ResourceAccessException e) {
			// System.err.println("Access Error " + url);
		} catch (IOException e) {
			// System.err.println("IO Error " + url);
		}
	}
	
	private void loadDescriptors() {
		for (int i = 0; i < this.getPackages().getSize(); i++) {
			Package pkg = (Package) this.getPackages().getElementAt(i);
			try {
				this.loadDescriptors(pkg.getFile());
			} catch (Exception e) {
				Dunamis.error(e);
			}
		}
	}

	private void loadPackage(URL url) throws Exception {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		Class<?>[] parameters = new Class[]{ URL.class };
		Method method = sysclass.getDeclaredMethod("addURL",parameters);
		method.setAccessible(true);
		method.invoke(sysloader,new Object[]{ url });
	}
	
	private void loadPackage(File file) throws Exception {
		URI uri = file.toURI(); // new URI("file:" + file.getAbsolutePath());
		URL url = uri.toURL();
		UIMAFramework.getLogger().log(Level.CONFIG,"Loading " + file.getAbsolutePath());
		this.loadPackage(url);
	}
	
	private void loadPackages() {
		for (int i = 0; i < this.getPackages().getSize(); i++) {
			Package pkg = (Package) this.getPackages().getElementAt(i);
			File file = pkg.getFile();
			try {
				this.loadPackage(file);
			} catch (Exception e) {
				Dunamis.error(e);
			}
        }
	}
	
	private void loadEngine(File file) throws Exception {
		XMLInputSource xml = new XMLInputSource(file);
		XMLParser parser = UIMAFramework.getXMLParser();
		CpeDescription desc = parser.parseCpeDescription(xml);
		CollectionProcessingEngine cpe = new CollectionProcessingEngine(desc); 
		this.getCollectionProcessingEngines().addElement(cpe);
		UIMAFramework.getLogger().log(Level.CONFIG,"Adding " + cpe.toString() + " to Collection Processing Engines");
	}
		
	private void loadEngines() throws Exception {
		if (this.getSettingFile().exists()) {
			Scanner scanner = new Scanner(this.getSettingFile());
			while (scanner.hasNextLine()) {
				String engine = scanner.nextLine();
				if (engine.startsWith("file:")) {
					File file = new File(engine.substring(5));
					try {
						this.loadEngine(file);
					} catch (Exception e) {
						Dunamis.error(e);
					}
				}
			}
			scanner.close();
		}
	}

	public void doSave() throws Exception {
		OutputStream stream = new FileOutputStream(this.getSettingFile());
		Writer writer = new OutputStreamWriter(stream);
		for (int index = 0; index < this.getCollectionProcessingEngines().getSize(); index++) {
			CollectionProcessingEngine cpe = (CollectionProcessingEngine) this.getCollectionProcessingEngines().getElementAt(index);
			String uri = cpe.getDescription().getSourceUrl().toString();
			writer.write(uri + '\n');
		}
		writer.close();
	}
	
	/*
	private void doParseSettings() throws ParserConfigurationException,	SAXException, IOException, Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(this.getSettingFile());
		Element root = document.getDocumentElement();
		root.normalize();
		if (root.getNodeName().equals(DUNAMIS)) {
			NodeList archives = root.getElementsByTagName(PACKAGES);
			this.doParseArchives(archives);
			NodeList engines = root.getElementsByTagName(ENGINES);
			this.doParseEngines(engines);
		} else {
			throw new Exception("Wrong root tag: expected 'dunamis' but got here " + root.getNodeName());
		}
	}

	private void doParseEngines(NodeList engines) {
		for (int i = 0; i < engines.getLength(); i++) {
			Element engine = (Element) engines.item(i);
			NodeList files = engine.getElementsByTagName(ENGINE);
			for (int j = 0; j < files.getLength(); j++) {
				try {
					File file = new File(files.item(j).getTextContent());
					this.getEngineFiles().addElement(file);				
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	private void doParseArchives(NodeList archives) {
		for (int i = 0; i < archives.getLength(); i++) {
			Element archive = (Element) archives.item(i);
			NodeList files = archive.getElementsByTagName(PACKAGE);
			for (int j = 0; j < files.getLength(); j++) {
				try {
					URI uri = new URI(files.item(j).getTextContent());
					File file = new File(uri.getPath());
					if (file.exists()) {
						Package pkg = new Package(file);
						this.getPackages().addElement(pkg);
					} 
				} catch (Exception e) {
					Dunamis.warning(e.getMessage());
				}
			}
		}
	}

	private void doBuildSetings() throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		Element root = document.createElement(DUNAMIS);
		Element archives = document.createElement(PACKAGES);
		Element engines = document.createElement(ENGINES);
		root.appendChild(archives);
		root.appendChild(engines);
		document.appendChild(root);
		this.doStore(document);
	}

	private void store() throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(this.getSettingFile());
		Element root = document.getDocumentElement();
		this.doCleanSettings(root);
		this.doStoreArchives(document,root);
		this.doStoreEngines(document,root);
		this.doStore(document);
	}

	private void doStore(Document document)	throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result =  new StreamResult(this.getSettingFile());
        transformer.transform(source,result);
	}

	private void doStoreEngines(Document document,Element root) {
		Element engines = document.createElement(ENGINES);
		for (int i = 0; i < this.getCollectionProcessingEngines().getSize(); i++) {
			CollectionProcessingEngine cpe = (CollectionProcessingEngine) this.getCollectionProcessingEngines().getElementAt(i);
			Element engine = document.createElement(ENGINE);
			engine.setTextContent(cpe.getDescription().getSourceUrl().getPath());
			engines.appendChild(engine);
		}
		
		root.appendChild(engines);
	}

	private void doStoreArchives(Document document,Element root) {
		Element archives = document.createElement(PACKAGES);
		for (int i = 0; i < this.getPackages().getSize(); i++) {
			Package pkg = (Package) this.getPackages().getElementAt(i);
			Element archive = document.createElement(PACKAGE);
			archive.setTextContent(pkg.getFile().toString());
			archives.appendChild(archive);
		}
		root.appendChild(archives);
	}

	private void doCleanSettings(Element root) {
		NodeList archiveNodes = root.getElementsByTagName(PACKAGES);
		for (int i = 0; i < archiveNodes.getLength(); i++) {
			Node node = archiveNodes.item(i);
			root.removeChild(node);
		}
		NodeList enginesNodes = root.getElementsByTagName(ENGINES);
		for (int i = 0; i < enginesNodes.getLength(); i++) {
			Node node = enginesNodes.item(i);
			root.removeChild(node);
		}
	}

	*/
}
