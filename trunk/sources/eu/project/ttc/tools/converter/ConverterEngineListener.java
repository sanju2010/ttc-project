package eu.project.ttc.tools.converter;

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

public class ConverterEngineListener implements ActionListener, StatusCallbackListener {

	private Converter converter;
	
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	private Converter getConverter() {
		return this.converter;
	}
	
	private URI getHelp() throws URISyntaxException {
		String uri = this.getConverter().getPreferences().getHelp();
		return new URI(uri);
	}
	
	private void doQuit() {
		String message = "Do you really want to quit " + this.getConverter().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getConverter().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getConverter().quit();				
		}
	}
	
	private void doHelp() {
		try {
			if (this.getConverter().getDesktop() != null && this.getConverter().getDesktop().isSupported(Desktop.Action.BROWSE)) {
				this.getConverter().getDesktop().browse(this.getHelp());	
			} else {
				Runnable parent = this.getConverter().getParent();
				if (parent instanceof TermSuite) {
					TermSuite termSuite = (TermSuite) parent;
					termSuite.getHelp().selectTreeTagger();
					SwingUtilities.invokeLater(termSuite.getHelp());
				}
			}
		} catch (IOException e) {
			this.getConverter().error(e);
		} catch (URISyntaxException e) {
			this.getConverter().error(e);
		}
	}
	
	public void doProcess() {
		this.getConverter().getSettings().doUpdate();
		try {
			this.getConverter().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getConverter().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		ConverterEngine proc = new ConverterEngine();
		proc.setConverter(this.getConverter());
		proc.execute();
		try {
			CpeDescription desc = proc.get();
			this.setTimer();
			this.getConverter().getToolBar().getRun().setEnabled(false);
			this.getConverter().getToolBar().getPause().setEnabled(true);
			this.getConverter().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getConverter().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getConverter().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getConverter().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
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
			this.getConverter().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getConverter().getToolBar().getProgressBar().setMaximum(files);
	    	this.getConverter().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getConverter().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getConverter().getToolBar().getProgressBar().setString("Resumed...");
			this.getConverter().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getConverter().getToolBar().getProgressBar().setString("Paused...");
			this.getConverter().getToolBar().getRun().setText("Resume");
			this.getConverter().getToolBar().getRun().setActionCommand("resume");
			this.getConverter().getToolBar().getRun().setEnabled(true);
			this.getConverter().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getConverter().getToolBar().getRun().setEnabled(true);
		this.getConverter().getToolBar().getPause().setEnabled(false);
		this.getConverter().getToolBar().getStop().setEnabled(false);
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
				this.getConverter().getToolBar().getProgressBar().setValue(value);
				this.getConverter().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
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
				this.getConverter().getAbout().show();
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
		if (this.getConverter().isCommandLineInterface()) {
			this.getConverter().quit();;
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
			this.getConverter().error(e);
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
			this.getConverter().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getConverter().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getConverter().warning(message);
		} 
	}
	
}
