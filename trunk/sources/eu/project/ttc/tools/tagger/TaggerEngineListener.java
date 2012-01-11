package eu.project.ttc.tools.tagger;

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

import eu.project.ttc.tools.tagger.Tagger;
import eu.project.ttc.tools.tagger.TaggerTool;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;

public class TaggerEngineListener implements ActionListener, StatusCallbackListener {

	private TaggerTool taggerTool;
	
	public void setTaggerTool(TaggerTool tool) {
		this.taggerTool = tool;
	}
	
	private TaggerTool getTaggerTool() {
		return this.taggerTool;
	}
	
	private TaggerEngine taggerEngine;
	
	public void setTaggerEngine(TaggerEngine taggerEngine) {
		this.taggerEngine = taggerEngine;
	}
	
	private TaggerEngine getTaggerEngine() {
		return this.taggerEngine;
	}
	
	private void doQuit() {
		String message = "Do you really want to quit " + this.getTaggerTool().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getTaggerTool().getParent().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getTaggerTool().getParent().quit();				
		}
	}
	
	private void doHelp() {
		try {
			Tagger parent = this.getTaggerTool().getParent();
			// if (parent.isTreeTaggerSelected()) {
				parent.getParent().getHelp().selectTreeTagger();
			/* } else if (parent.isTildeTaggerSelected()) {
				parent.getParent().getHelp().selectTildeTagger();
			} else if (parent.isRFTaggerSelected()) {
				parent.getParent().getHelp().selectRFTagger();
			} else if (parent.isFreeLingSelected()) {
				parent.getParent().getHelp().selectFreeLing();
			} else {
				parent.getParent().getHelp().selectTermSuite();
			} */
			SwingUtilities.invokeLater(parent.getParent().getHelp());				
		} catch (Exception e) {
			this.getTaggerTool().error(e);
		}
	}
		
	public void doProcess() {
		this.getTaggerTool().getSettings().doUpdate();
		try {
			this.getTaggerTool().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getTaggerTool().getParent().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		try {
			this.getTaggerEngine().execute();
			CpeDescription desc = this.getTaggerEngine().get();
			this.setTimer();
			this.getTaggerTool().getParent().getToolBar().getRun().setEnabled(false);
			this.getTaggerTool().getParent().getToolBar().getPause().setEnabled(true);
			this.getTaggerTool().getParent().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getTaggerTool().getParent().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getTaggerTool().getParent().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getTaggerTool().getParent().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
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
			this.getTaggerTool().getParent().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getTaggerTool().getParent().getToolBar().getProgressBar().setMaximum(files);
	    	this.getTaggerTool().getParent().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getTaggerTool().getParent().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getTaggerTool().getParent().getToolBar().getProgressBar().setString("Resumed...");
			this.getTaggerTool().getParent().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getTaggerTool().getParent().getToolBar().getProgressBar().setString("Paused...");
			this.getTaggerTool().getParent().getToolBar().getRun().setText("Resume");
			this.getTaggerTool().getParent().getToolBar().getRun().setActionCommand("resume");
			this.getTaggerTool().getParent().getToolBar().getRun().setEnabled(true);
			this.getTaggerTool().getParent().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getTaggerTool().getParent().getToolBar().getRun().setEnabled(true);
		this.getTaggerTool().getParent().getToolBar().getPause().setEnabled(false);
		this.getTaggerTool().getParent().getToolBar().getStop().setEnabled(false);
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
				this.getTaggerTool().getParent().getToolBar().getProgressBar().setValue(value);
				this.getTaggerTool().getParent().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
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
				this.getTaggerTool().getAbout().show();
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
		this.doLoad();
		if (this.getTaggerTool().getParent().isCommandLineInterface()) {
			this.getTaggerTool().getParent().quit();;
		}
	}
	
	private void doLoad() {
		try {
			UIMAFramework.getLogger().log(Level.INFO,"Collection Process Complete");
			UIMAFramework.getLogger().log(Level.INFO,this.getEngine().getPerformanceReport().toString());
		} catch (Exception e) {
			this.getTaggerTool().error(e);
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
			this.getTaggerTool().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getTaggerTool().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getTaggerTool().warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getTaggerTool().getParent().getDocuments().getResultModel().addElement(result);
			} catch (Exception e) {
				this.getTaggerTool().error(e);
			}
		}
	}
	
}
