package eu.project.ttc.tools;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

import fr.univnantes.lina.uima.models.TermBank;
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
	
	/*
	private void doRun() {
		String[] sourceCorpora = this.getTermSuite().getParameters().getSourceDirectories();
		if (sourceCorpora == null || sourceCorpora.length == 0) {
			String message = "There is no source corpus selected!\n.";
			message += "You should select some corpus by drag and droping directories into corpora.";
			String title = "Warning";
			JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.WARNING_MESSAGE);
		} else {
			String[] targetCorpora = this.getTermSuite().getParameters().getTargetDirectories();
			if (targetCorpora == null || targetCorpora.length == 0) {
				String message = "There is no target corpus selected!\n.";
				message += "You should select some corpus by drag and droping directories into corpora.";
				String title = "Warning";
				JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.WARNING_MESSAGE);
			} else {
				String source = this.getTermSuite().getParameters().getSourceLanguage();
				String target = this.getTermSuite().getParameters().getTargetLanguage();
				if (source.equals(target)) {
					String message = "The source language is the same as that of the target one!\n.";
					message += "You should select different languages in Source and Target tabs.";
					String title = "Warning";
					JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.WARNING_MESSAGE);
				} else {
					this.doProcess();
				}
			}
		}
	}
	*/

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
		System.out.println(this.getEngine().getPerformanceReport().toString());
		System.out.flush();
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
	
	private void doHook() {
		TermBank termBase = TermBankResource.getInstance();
		System.out.println("Hooking " + termBase);
		this.getTermSuite().getTerms().setModel(termBase.getTreeModel());
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
			}  else if (action.equals("stop")) {
				this.doStop();
			}
		} else if (object instanceof Timer) {
			this.doTime();
		}
	}

	@Override
	public void initializationComplete() {
		this.doStart();
		this.doHook();
	}

	@Override
	public void batchProcessComplete() { }

	@Override
	public void collectionProcessComplete() {
		this.doStop();
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

}
