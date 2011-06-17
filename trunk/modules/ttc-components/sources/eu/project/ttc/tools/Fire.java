package eu.project.ttc.tools;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

import eu.project.ttc.tools.Corpora.Corpus;
import eu.project.ttc.tools.Languages.Language;

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
		String message = "Do you really want to quit TTC TermSuite?";
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
	
	private void doRun() {
		List<Corpus> directories = this.getTermSuite().getCorpora().getSelection();
		if (directories.isEmpty()) {
			String message = "There is no corpus selected!\n.";
			message += "You should select some corpus by drag and droping directories into corpora.";
			String title = "Warning";
			JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.WARNING_MESSAGE);
		} else {
			List<Language> languages = this.getTermSuite().getLanguges().getSelection();
			if (languages.isEmpty()) {
				String message = "There is no language selected!\n.";
				message += "You should select some languages by clicking on items of languages.";
				String title = "Warning";
				JOptionPane.showMessageDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.WARNING_MESSAGE);
			} else {
				Processor proc = new Processor(directories,languages);
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
				this.doRun();
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
		// TODO Auto-generated method stub
		this.doStart();
	}

	@Override
	public void batchProcessComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collectionProcessComplete() {
		// TODO Auto-generated method stub
		this.doStop();
	}

	@Override
	public void paused() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aborted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {
		// TODO Auto-generated method stub
		
	}

}
