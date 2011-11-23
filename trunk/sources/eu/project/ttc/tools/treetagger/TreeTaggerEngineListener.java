package eu.project.ttc.tools.treetagger;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;

import eu.project.ttc.tools.TermSuite;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;

public class TreeTaggerEngineListener implements ActionListener, StatusCallbackListener {

	private TreeTagger treeTagger;
	
	public void setTreeTagger(TreeTagger treeTagger) {
		this.treeTagger = treeTagger;
	}
	
	private TreeTagger getTreeTagger() {
		return this.treeTagger;
	}
	
	private URI getHelp() throws URISyntaxException {
		String uri = this.getTreeTagger().getPreferences().getHelp();
		return new URI(uri);
	}
	
	private void doQuit() {
		String message = "Do you really want to quit " + this.getTreeTagger().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getTreeTagger().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getTreeTagger().quit();				
		}
	}
	
	private void doHelp() {
		try {
			if (this.getTreeTagger().getDesktop() != null && this.getTreeTagger().getDesktop().isSupported(Desktop.Action.BROWSE)) {
				this.getTreeTagger().getDesktop().browse(this.getHelp());	
			} else {
				Runnable parent = this.getTreeTagger().getParent();
				if (parent instanceof TermSuite) {
					TermSuite termSuite = (TermSuite) parent;
					termSuite.getHelp().selectTreeTagger();
					SwingUtilities.invokeLater(termSuite.getHelp());
				}
			}
		} catch (IOException e) {
			this.getTreeTagger().error(e);
		} catch (URISyntaxException e) {
			this.getTreeTagger().error(e);
		}
	}
	
	public void doProcess() {
		this.getTreeTagger().getSettings().doUpdate();
		try {
			this.getTreeTagger().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getTreeTagger().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		TreeTaggerEngine proc = new TreeTaggerEngine();
		proc.setTreeTagger(this.getTreeTagger());
		proc.execute();
		try {
			CpeDescription desc = proc.get();
			this.setTimer();
			this.getTreeTagger().getToolBar().getRun().setEnabled(false);
			this.getTreeTagger().getToolBar().getPause().setEnabled(true);
			this.getTreeTagger().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getTreeTagger().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getTreeTagger().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getTreeTagger().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
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
			this.getTreeTagger().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getTreeTagger().getToolBar().getProgressBar().setMaximum(files);
	    	this.getTreeTagger().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getTreeTagger().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getTreeTagger().getToolBar().getProgressBar().setString("Resumed...");
			this.getTreeTagger().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getTreeTagger().getToolBar().getProgressBar().setString("Paused...");
			this.getTreeTagger().getToolBar().getRun().setText("Resume");
			this.getTreeTagger().getToolBar().getRun().setActionCommand("resume");
			this.getTreeTagger().getToolBar().getRun().setEnabled(true);
			this.getTreeTagger().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getTreeTagger().getToolBar().getRun().setEnabled(true);
		this.getTreeTagger().getToolBar().getPause().setEnabled(false);
		this.getTreeTagger().getToolBar().getStop().setEnabled(false);
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
				this.getTreeTagger().getToolBar().getProgressBar().setValue(value);
				this.getTreeTagger().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
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
				this.getTreeTagger().getAbout().show();
			} else if (action.equals("run")) {
				this.doProcess();
			} else if (action.equals("pause")) {
				this.doPause();
			} else if (action.equals("resume")) {
				this.doResume();
			} else if (action.equals("stop")) {
				this.doStop();
			} /* else if (action.equals("save")) {
				try {
					this.doSave();
				} catch (Exception e) {
					e.printStackTrace();
					// this.getTermSuite().error(e);
				}
			} */
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
		if (this.getTreeTagger().isCommandLineInterface()) {
			this.getTreeTagger().quit();;
		}
	}
	
	private void doLoad() {
		try {
			// String path = (String) this.getTermSuite().getParameters().getHiddenMetaData().getConfigurationParameterSettings().getParameterValue("TermBankFile");
			// UIMAFramework.getLogger().log(Level.CONFIG,"Loading Term Bank " + path);
			// File file = new File(path);
			// TermBankResource resource = new TermBankResource();
			// InputStream inputStream = new FileInputStream(file);
			// resource.update(inputStream);
			// resource.embed(this.getTermSuite().getTerms().getModel());
			// 
			// TODO
			UIMAFramework.getLogger().log(Level.INFO,"Collection Process Complete");
			// FIXME this.getTermMate().getTerms().doLoad(DefaultIndex.getInstance());
			// this.getTermSuite().getToolBar().getSave().setEnabled(true);
			UIMAFramework.getLogger().log(Level.INFO,this.getEngine().getPerformanceReport().toString());
		} catch (Exception e) {
			this.getTreeTagger().error(e);
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
			this.getTreeTagger().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getTreeTagger().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getTreeTagger().warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getTreeTagger().getDocuments().getResultModel().addElement(result);
			} catch (Exception e) {
				this.getTreeTagger().error(e);
			}
		}
	}

	/*
	
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
				DefaultMutableTreeNode aTerminology = (DefaultMutableTreeNode) root.getChildAt(i);
				int length = aTerminology.getChildCount();
				for (int j = 0; j < length; j++) {
					DefaultMutableTreeNode aTerm = (DefaultMutableTreeNode) aTerminology.getChildAt(j);
					Element termEntry = document.createElement("termEntry");
					body.appendChild(termEntry);
					Element langSet = document.createElement("langSet");
					termEntry.appendChild(langSet);
					Element tig = document.createElement("tig");
					langSet.appendChild(tig);
					Element term = document.createElement("term");
					/*
					Term trm = (Term) aTerm.getUserObject();
					term.setTextContent(trm.toString().trim());
					tig.appendChild(term);
					this.addNote(document, langSet, tig, "language", trm.language());
					this.addNote(document, langSet, tig, "complexity", trm.complexity().trim());
					this.addNote(document, langSet, tig, "category", trm.category().trim());
					this.addNote(document, langSet, tig, "lemma", trm.lemma().trim());
					this.addNote(document, langSet, tig, "frequency", trm.size());
					Set<String> occurrences = new HashSet<String>();
					for (int k = 0; k < aTerm.getChildCount(); k++) {
						DefaultMutableTreeNode child = (DefaultMutableTreeNode) aTerm.getChildAt(k);
						Object object = child.getUserObject();
						if (object instanceof TermOccurrence) {
							TermOccurrence anOccurrence = (TermOccurrence) object;
							occurrences.add(anOccurrence.text().trim());
						}
					}
					for (String occurrence : occurrences) {
						this.addNote(document, langSet, tig, "occurrence", occurrence);
						this.addNote(document, langSet, tig, "context", "... " + occurrence + " ...");
					}
					this.addNote(document, langSet, tig, "variant", "...");
										/ * /
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		}
	}
	
	private void addLanguage(Element element,String language) {
		element.setAttribute("xml:lang", language);
	}
	
	private void addNote(Document document, Element lang, Element element,String type,Object value) {
		if (type.equals("language")) {
			this.addLanguage(lang, value.toString());
		} else {
			Element termNote = document.createElement("termNote");
			element.appendChild(termNote);
			termNote.setAttribute("type", type);
			termNote.setTextContent(value.toString());
		}
	}
	*/
	/*
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
				
			}
		}
	}
	*/
	
}
