package eu.project.ttc.tools.ziggurat;

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

public class ZigguratEngineListener implements ActionListener, StatusCallbackListener {

	private Ziggurat termSuite;
	
	public void setZiggurat(Ziggurat ziggurat) {
		this.termSuite = ziggurat;
	}
	
	private Ziggurat getZiggurat() {
		return this.termSuite;
	}
	
	private URI getHelp() throws URISyntaxException {
		String uri = this.getZiggurat().getPreferences().getHelp();
		return new URI(uri);
	}
	
	private void doQuit() {
		String message = "Do you really want to quit " + this.getZiggurat().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getZiggurat().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getZiggurat().quit();				
		}
	}
	
	private void doHelp() {
		try {
			if (this.getZiggurat().getDesktop() != null && this.getZiggurat().getDesktop().isSupported(Desktop.Action.BROWSE)) {
				this.getZiggurat().getDesktop().browse(this.getHelp());	
			} else {
				Runnable parent = this.getZiggurat().getParent();
				if (parent instanceof TermSuite) {
					TermSuite termSuite = (TermSuite) parent;
					termSuite.getHelp().selectAligner();
					SwingUtilities.invokeLater(termSuite.getHelp());
				}
			}
		} catch (IOException e) {
			this.getZiggurat().error(e);
		} catch (URISyntaxException e) {
			this.getZiggurat().error(e);
		}
	}
	
	public void doProcess() {
		this.getZiggurat().getSettings().doUpdate();
		try {
			this.getZiggurat().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getZiggurat().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		ZigguratEngine proc = new ZigguratEngine();
		proc.setZiggurat(this.getZiggurat());
		proc.execute();
		try {
			CpeDescription desc = proc.get();
			this.setTimer();
			this.getZiggurat().getToolBar().getRun().setEnabled(false);
			this.getZiggurat().getToolBar().getPause().setEnabled(true);
			this.getZiggurat().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getZiggurat().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getZiggurat().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getZiggurat().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
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
			this.getZiggurat().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getZiggurat().getToolBar().getProgressBar().setMaximum(files);
	    	this.getZiggurat().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getZiggurat().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getZiggurat().getToolBar().getProgressBar().setString("Resumed...");
			this.getZiggurat().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getZiggurat().getToolBar().getProgressBar().setString("Paused...");
			this.getZiggurat().getToolBar().getRun().setText("Resume");
			this.getZiggurat().getToolBar().getRun().setActionCommand("resume");
			this.getZiggurat().getToolBar().getRun().setEnabled(true);
			this.getZiggurat().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getZiggurat().getToolBar().getRun().setEnabled(true);
		this.getZiggurat().getToolBar().getPause().setEnabled(false);
		this.getZiggurat().getToolBar().getStop().setEnabled(false);
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
				this.getZiggurat().getToolBar().getProgressBar().setValue(value);
				this.getZiggurat().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
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
				this.getZiggurat().getAbout().show();
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
		UIMAFramework.getLogger().log(Level.INFO,"Collection Process Complete");
		UIMAFramework.getLogger().log(Level.INFO,this.getEngine().getPerformanceReport().toString());
		if (this.getZiggurat().isCommandLineInterface()) {
			this.getZiggurat().quit();
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
			this.getZiggurat().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getZiggurat().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getZiggurat().warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getZiggurat().getDocuments().getResultModel().addElement(result);
				this.getZiggurat().getTranslations().doLoad(cas.getJCas());
			} catch (Exception e) {
				this.getZiggurat().error(e);
			}
		}
	}
	
}
