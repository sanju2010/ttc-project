package eu.project.ttc.tools;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.univnantes.lina.uima.models.TermBankResource;
import fr.univnantes.lina.uima.tools.dunamis.models.ProcessingResult;

public class Fire implements ActionListener, StatusCallbackListener {

	private TermSuite termSuite;
	
	public void setTermSuite(TermSuite termSuite) {
		this.termSuite = termSuite;
	}
	
	private TermSuite getTermSuite() {
		return this.termSuite;
	}
	
	private URI getHelp() throws URISyntaxException {
		String uri = this.getTermSuite().getPreferences().getHelp();
		return new URI(uri);
	}
	
	private void doQuit() {
		String message = "Do you really want to quit " + this.getTermSuite().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getTermSuite().quit();				
		}
	}
	
	private void doHelp() {
		try {
			if (this.getTermSuite().getDesktop().isSupported(Desktop.Action.BROWSE)) {
				this.getTermSuite().getDesktop().browse(this.getHelp());	
			}
		} catch (IOException e) {
			this.getTermSuite().error(e);
		} catch (URISyntaxException e) {
			this.getTermSuite().error(e);
		}
	}
	
	public void doProcess() {
		this.getTermSuite().getParameters().doUpdate();
		Processor proc = new Processor();
		proc.setTermSuite(this.getTermSuite());
		proc.execute();
		try {
			CpeDescription desc = proc.get();
			this.setTimer();
			this.getTermSuite().getToolBar().getRun().setEnabled(false);
			this.getTermSuite().getToolBar().getPause().setEnabled(true);
			this.getTermSuite().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
	}
	
	private void doStart() {
		int files = -1;
		Progress progress[] = this.getEngine().getProgress();
		if (progress != null) { 
			for (int i = 0; i < progress.length; i++) {
				if (progress[i].getUnit().equals(Progress.ENTITIES)) {
					files = (int) progress[i].getTotal();
					break;
				}
			}
		}
		if (files == -1) {
			this.getTermSuite().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getTermSuite().getToolBar().getProgressBar().setMaximum(files);
	    	this.getTermSuite().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getTermSuite().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getTermSuite().getToolBar().getProgressBar().setString("Resumed...");
			this.getTermSuite().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getTermSuite().getToolBar().getProgressBar().setString("Paused...");
			this.getTermSuite().getToolBar().getRun().setText("Resume");
			this.getTermSuite().getToolBar().getRun().setActionCommand("resume");
			this.getTermSuite().getToolBar().getRun().setEnabled(true);
			this.getTermSuite().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getTermSuite().getToolBar().getRun().setEnabled(true);
		this.getTermSuite().getToolBar().getPause().setEnabled(false);
		this.getTermSuite().getToolBar().getStop().setEnabled(false);
		// System.out.println(this.getEngine().getPerformanceReport().toString());
		// System.out.flush();
	}
	
	private void doTime() {
		Progress progress[] = this.getEngine().getProgress();
		if (progress != null && progress.length > 0) {
			int index = -1;
			if (index == -1) {
				for (int i = 0; i < progress.length; i++) {
					if (progress[i].getUnit().equals(Progress.ENTITIES)) {
						index = i;
						break;
					}
				}
			}
			if (index >= 0) {
				int value = (int) progress[index].getCompleted();
				this.getTermSuite().getToolBar().getProgressBar().setValue(value);
				this.getTermSuite().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
			} 
		}
	}
	
	private Timer timer;
	
	private void setTimer() {
		this.timer = new Timer(0,this);
	}
	
	public Timer getTimer() {
		return this.timer;
	}
	
	private CollectionProcessingEngine engine;
	
	private void setEngine(CpeDescription desc) throws ResourceInitializationException {
		this.engine = UIMAFramework.produceCollectionProcessingEngine(desc);
		this.engine.addStatusCallbackListener(this);
		this.engine.process();
	}
	
	private CollectionProcessingEngine getEngine() {
		return this.engine;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object instanceof JButton) {
			JButton source = (JButton) object;
			String action = source.getActionCommand();
			if (action.equals("quit")) { 
				this.doQuit();
			} else if (action.equals("help")) { 
				this.doHelp();
			} else if (action.equals("about")) {
				this.getTermSuite().getAbout().show();
			} else if (action.equals("run")) {
				this.doProcess();
			} else if (action.equals("pause")) {
				this.doPause();
			} else if (action.equals("resume")) {
				this.doResume();
			} else if (action.equals("stop")) {
				this.doStop();
			} else if (action.equals("save")) {
				try {
					this.doSave();
				} catch (Exception e) {
					this.getTermSuite().error(e);
				}
			}
		} else if (object instanceof Timer) {
			this.doTime();
		}
	}

	@Override
	public void initializationComplete() {
		this.doStart();
	}

	@Override
	public void batchProcessComplete() { }

	@Override
	public void collectionProcessComplete() {
		this.doStop();
		this.doLoad();
	}
	
	private void doLoad() {
		try {
			String path = (String) this.getTermSuite().getParameters().getHiddentMetaData().getConfigurationParameterSettings().getParameterValue("TermBankFile");
			UIMAFramework.getLogger().log(Level.CONFIG,"Loading Term Bank " + path);
			File file = new File(path);
			TermBankResource resource = new TermBankResource();
			InputStream inputStream = new FileInputStream(file);
			resource.update(inputStream);
			resource.embed(this.getTermSuite().getTerms().getModel());
			UIMAFramework.getLogger().log(Level.INFO,"Term Bank Loaded");
			this.getTermSuite().getToolBar().getSave().setEnabled(true);
			UIMAFramework.getLogger().log(Level.INFO,this.getEngine().getPerformanceReport().toString());
		} catch (Exception e) {
			this.getTermSuite().error(e);
		}
	}

	@Override
	public void paused() { }

	@Override
	public void resumed() { }

	@Override
	public void aborted() { }

	@Override
	public void entityProcessComplete(CAS cas, EntityProcessStatus status) {
		if (status.isException()) {
			String message = status.getStatusMessage();
			this.getTermSuite().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getTermSuite().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getTermSuite().warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getTermSuite().getDocuments().getResultModel().addElement(result);
			} catch (Exception e) {
				this.getTermSuite().error(e);
			}
		}
	}

	private JFileChooser chooser;
	
	private void setChooser() {
		this.chooser = new JFileChooser();
		this.chooser.setDialogTitle("File Chooser");
		this.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	this.chooser.setPreferredSize(new Dimension(750,500));
	}
	
	private JFileChooser getChooser() {
		return this.chooser;
	}
	
	public void doSave() throws Exception {
		if (this.getChooser() == null) {
			this.setChooser();
		}
		this.getChooser().setMultiSelectionEnabled(false);
		int rv = this.getChooser().showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
			File file = this.getChooser().getSelectedFile();

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
			sourceDesc.setTextContent(file.getAbsolutePath());
			fileDesc.appendChild(sourceDesc);
			Element text = document.createElement("text");
			martif.appendChild(text);
			Element body = document.createElement("body");
			text.appendChild(body);

			TreeModel tree = this.getTermSuite().getTerms().getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getRoot();
			int size = root.getChildCount();
			for (int i = 0; i < size; i++) {
				DefaultMutableTreeNode entry = (DefaultMutableTreeNode) root.getChildAt(i);
				Element termEntry = document.createElement("termEntry");
				body.appendChild(termEntry);
				Element langSet = document.createElement("langSet");
				termEntry.appendChild(langSet);
				int length = entry.getChildCount();
				for (int j = 0; j < length; j++) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) entry.getChildAt(j);
					Element ntig = document.createElement("ntig");
					langSet.appendChild(ntig);
					Element termGrp = document.createElement("termGrp");
					ntig.appendChild(termGrp);
					Element term = document.createElement("term");
					term.setTextContent((String) entry.getUserObject());
					termGrp.appendChild(term);
					for (int k = 0; k < node.getChildCount(); k++) {
						DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(k);
						String[] note = this.getNote((String) child.getUserObject());
						if (note == null) {
							this.addComponent(document, term, child);
						} else {
							this.addNote(document, langSet, term, note[0],note[1]);
						}
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		}
	}

	private String[] getNote(String string) {
		String[] result = new String[2];
		String[] strings = string.split(":");
		if (strings.length == 2) {
			result[0] = strings[0].trim();
			result[1] = strings[1].trim();
			return result;
		} else {
			return null;
		}
	}
	
	private void addLanguage(Element element,String language) {
		element.setAttribute("xml:lang", language);
	}
	
	private void addNote(Document document, Element lang, Element element,String type,String value) {
		if (lang != null && type.equals("language")) {
			this.addLanguage(lang, value);
		} 
		Element termNote = document.createElement("termNote");
		element.appendChild(termNote);
		termNote.setAttribute("type", type);
		termNote.setTextContent(value);
	}
	
	private void addComponent(Document document,Element element, DefaultMutableTreeNode root) {
		Element termCompList = document.createElement("TermCompList");
		element.appendChild(termCompList);
		termCompList.setAttribute("type", "termElement");
		int size = root.getChildCount();
		for (int i = 0; i < size; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			Element termCompGrp = document.createElement("termCompGrp");
			termCompList.appendChild(termCompGrp);
			Element termComp = document.createElement("termComp");
			termCompGrp.appendChild(termComp);
			termComp.setTextContent((String) node.getUserObject());
			for (int k = 0; k < node.getChildCount(); k++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(k);
				String[] note = this.getNote((String) child.getUserObject());
				if (note == null) {
					this.addComponent(document, termComp, child);
				} else {
					this.addNote(document, null, termComp, note[0],note[1]);
				}
			}
		}
	}
	
}
