package eu.project.ttc.tools.mimesis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class MimesisEngineListener implements ActionListener, StatusCallbackListener {

	private Mimesis mimesis;
	
	public void setMimesis(Mimesis mimesis) {
		this.mimesis = mimesis;
	}
	
	private Mimesis getMimesis() {
		return this.mimesis;
	}

	private void doQuit() {
		String message = "Do you really want to quit " + this.getMimesis().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getMimesis().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getMimesis().quit();				
		}
	}
	
	private void doHelp() {
		try {
			TermSuite parent = this.getMimesis().getParent();
			parent.getHelp().selectContextualizer();
			SwingUtilities.invokeLater(parent.getHelp());
		} catch (Exception e) {
			this.getMimesis().error(e);
		} 
	}
	
	public void doProcess() {
		this.getMimesis().getSettings().doUpdate();
		try {
			this.getMimesis().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getMimesis().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		MimesisEngine proc = new MimesisEngine();
		proc.setMimesis(this.getMimesis());
		proc.execute();
		try {
			CpeDescription desc = proc.get();
			this.setTimer();
			this.getMimesis().getToolBar().getRun().setEnabled(false);
			this.getMimesis().getToolBar().getPause().setEnabled(true);
			this.getMimesis().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getMimesis().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getMimesis().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getMimesis().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
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
			this.getMimesis().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getMimesis().getToolBar().getProgressBar().setMaximum(files);
	    	this.getMimesis().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getMimesis().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getMimesis().getToolBar().getProgressBar().setString("Resumed...");
			this.getMimesis().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getMimesis().getToolBar().getProgressBar().setString("Paused...");
			this.getMimesis().getToolBar().getRun().setText("Resume");
			this.getMimesis().getToolBar().getRun().setActionCommand("resume");
			this.getMimesis().getToolBar().getRun().setEnabled(true);
			this.getMimesis().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getMimesis().getToolBar().getRun().setEnabled(true);
		this.getMimesis().getToolBar().getPause().setEnabled(false);
		this.getMimesis().getToolBar().getStop().setEnabled(false);
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
				this.getMimesis().getToolBar().getProgressBar().setValue(value);
				this.getMimesis().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
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
				this.getMimesis().getAbout().show();
			} else if (action.equals("run")) {
				this.doProcess();
			} else if (action.equals("pause")) {
				this.doPause();
			} else if (action.equals("resume")) {
				this.doResume();
			} else if (action.equals("stop")) {
				this.doStop();
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
		UIMAFramework.getLogger().log(Level.INFO,this.getEngine().getPerformanceReport().toString());
		if (this.getMimesis().isCommandLineInterface()) {
			this.getMimesis().quit();
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
			this.getMimesis().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getMimesis().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getMimesis().warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getMimesis().getDocuments().getResultModel().addElement(result);
			} catch (Exception e) {
				this.getMimesis().error(e);
			}
		}
	}
		
}
