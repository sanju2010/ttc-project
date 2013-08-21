package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.util.Progress;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;
import fr.free.rocheteau.jerome.dunamis.utils.Report;
import fr.free.rocheteau.jerome.dunamis.utils.ToolBar;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

public class EngineListener implements ActionListener, StatusCallbackListener {

	private Dunamis dunamis;
	
	public void setDunamis(Dunamis dunamis) {
		this.dunamis = dunamis;
	}
		
	private ToolBar getToolBar() {
		return this.dunamis.getToolBar();
	}
	
	private Report getReport() {
		return this.dunamis.getReport();
	}
	
	private ProcessingResultViewer getResultViewer() {
		return this.dunamis.getResultViewer();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JButton) {
			String action = event.getActionCommand();
			if (action.equals("build")) {
				this.doBuild();
			} else if (action.equals("run")) {
				this.doRun();
			} else if (action.equals("resume")) {
				this.doResume();
			} else if (action.equals("pause")) {
				this.doPause();
			} else if (action.equals("stop")) {
				this.doStop();
			} 
		} else if (event.getSource() instanceof Timer) {
			try {
				Progress progress[] = this.getToolBar().getEngine().getProgress();
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
						this.getToolBar().getProgressBar().setValue(value);
						this.getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
					} 
				}
			} catch (Exception e) {
				Dunamis.error(e);
			}
		}
	}
	
	private void doBuild() {
		this.getToolBar().getProgressBar().setString("");
		this.getToolBar().getRun().setText("Run");
		this.getToolBar().getRun().setActionCommand("run");
		this.getToolBar().getProgressBar().setValue(0);
		this.getToolBar().getBuild().setEnabled(false);
		this.getToolBar().getRun().setEnabled(true);
		SwingUtilities.invokeLater(this.getToolBar());
	}

	private void doStart() {
		int files = -1;
		Progress progress[] = this.getToolBar().getEngine().getProgress();
		if (progress != null) { 
			for (int i = 0; i < progress.length; i++) {
				if (progress[i].getUnit().equals(Progress.ENTITIES)) {
					files = (int) progress[i].getTotal();
					break;
				}
			}
		}
		if (files == -1) {
			this.getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getToolBar().getProgressBar().setMaximum(files);
	    	this.getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getToolBar().getProgressBar().setValue(0);
		// this.getToolBar().getCollectionProcessingEngine().getProcessingResults().clear();
		this.getToolBar().getTimer().start();
	}
	
	private void doRun() {
		this.getReport().clear();
		this.getToolBar().getRun().setEnabled(false);
		this.getToolBar().getPause().setEnabled(true);
		this.getToolBar().getStop().setEnabled(true);
		try { 
			this.getToolBar().getEngine().process();
		} catch (Exception e) {
			this.doStop();
			Dunamis.error(e);
		}		
	}

	private void doResume() {
		if (this.getToolBar().getEngine().isPaused()) {
			this.getToolBar().getPause().setEnabled(true);
			this.getToolBar().getTimer().restart();
			this.getToolBar().getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getToolBar().getEngine().isProcessing()) {
			this.getToolBar().getRun().setText("Resume");
			this.getToolBar().getRun().setActionCommand("resume");
			this.getToolBar().getRun().setEnabled(true);
			this.getToolBar().getPause().setEnabled(false);
			this.getToolBar().getEngine().pause();
			this.getToolBar().getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getToolBar().getEngine().isProcessing() || this.getToolBar().getEngine().isPaused()) {
			this.getToolBar().getEngine().stop();
		}
		this.getToolBar().getTimer().stop();
		this.getToolBar().getBuild().setEnabled(true);
		this.getToolBar().getRun().setEnabled(false);
		this.getToolBar().getPause().setEnabled(false);
		this.getToolBar().getStop().setEnabled(false);
		this.getReport().update(this.getToolBar().getEngine().getPerformanceReport());
	}
	
	@Override
	public void entityProcessComplete(CAS cas,EntityProcessStatus status) {
		if (status.isException()) {
			String message = status.getStatusMessage();
			Dunamis.warning(message);
			for (Exception e : status.getExceptions()) {
				Dunamis.error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			Dunamis.warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getResultViewer().getResultModel().addElement(result);
			} catch (Exception e) {
				Dunamis.error(e);
			}
		}
	}

	@Override
	public void aborted() {
	}

	@Override
	public void batchProcessComplete() {
	}

	@Override
	public void collectionProcessComplete() {
		this.doStop();
	}

	@Override
	public void initializationComplete() {
		this.doStart();
	}

	@Override
	public void paused() {
	}

	@Override
	public void resumed() {
	}
	
}
