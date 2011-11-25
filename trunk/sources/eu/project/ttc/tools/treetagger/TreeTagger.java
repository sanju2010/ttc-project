package eu.project.ttc.tools.treetagger;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;
import eu.project.ttc.tools.utils.ToolBar;
import fr.free.rocheteau.jerome.dunamis.listeners.ProcessingResultListener;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

public class TreeTagger implements Runnable {

	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}
	
	private boolean cli;
	
	private void enableCommandLineInterface(boolean enabled) {
		this.cli = enabled;
	}
	
	public boolean isCommandLineInterface() {
		return this.cli;
	}
	
	public void error(Exception e) {
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
	}
	
	public void warning(String message) {
		UIMAFramework.getLogger().log(Level.WARNING,message);
	}
	
	public void message(String message) {
		UIMAFramework.getLogger().log(Level.INFO,message);
	}
	
	private Desktop desktop;
	
	private void setDesktop() {
		if (Desktop.isDesktopSupported()) {
			this.desktop = Desktop.getDesktop();
		}
	}
	
	public Desktop getDesktop() {
		return this.desktop;
	}

	private TreeTaggerSettings settings;
	
	private void setSettings() {
		this.settings = new TreeTaggerSettings(System.getProperty("user.home") + File.separator + ".treetagger.settings");
	}
	
	public TreeTaggerSettings getSettings() {
		return this.settings;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences("treetagger.properties");
		try {
			this.preferences.load();
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
		this.about.setPreferences(this.getPreferences());
	}
	
	public About getAbout() {
		return this.about;
	}
	
	public Preferences  getPreferences() {
		return this.preferences;
	}

	private ToolBar toolBar;
	
	private void setToolBar() {
		this.toolBar = new ToolBar();
	}
	
	public ToolBar getToolBar() {
		return this.toolBar;
	}
	
	private ProcessingResultViewer documents;
	
	private void setDocuments() {
		this.documents = new ProcessingResultViewer();
	}
	
	public ProcessingResultViewer getDocuments() {
		return this.documents;
	}
	
	private Component content;
	
	private void setContent() {		
		JTabbedPane inner = new JTabbedPane();
		inner.setTabPlacement(JTabbedPane.TOP);
		inner.addTab("   Settings   ",this.getSettings().getComponent());
		inner.addTab("   Documents  ",this.getDocuments().getComponent());
		JSplitPane outter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		outter.setTopComponent(this.getToolBar().getComponent());
		outter.setBottomComponent(inner);
		outter.setDividerSize(0);
		outter.setEnabled(false);
		this.content = outter;
	}
	
	private Component getContent() {
		return this.content;
	}
	
	private Dimension getDimension() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (4 * screen.width) / 5;
		int height = (4 * screen.height) / 5;
		Dimension dimension = new Dimension(width,height);
		return dimension;
	}
	
	private JFrame frame;
	
	private void setFrame() {
		this.frame = new JFrame();
		this.frame.setTitle(this.getPreferences().getTitle());
		this.frame.setPreferredSize(this.getDimension());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.getContentPane().add(this.getContent());
		this.frame.setJMenuBar(null);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
	}

	private void hide() {
		this.getFrame().setVisible(false);
	}
	
	private void show() {
		this.getFrame().setVisible(true);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public TreeTagger(boolean cli) {
		this.enableCommandLineInterface(cli);
		this.setDesktop();
		this.setSettings();
		this.setPreferences();
		this.setAbout();
		this.setToolBar();
		this.setDocuments();
		this.setContent();
		this.setFrame();
		this.enableListeners();
	}
	
	private void enableListeners() {
		TreeTaggerEngineListener acabitEngineListener = new TreeTaggerEngineListener();
		acabitEngineListener.setTreeTagger(this);
		this.getToolBar().enableListeners(acabitEngineListener);
		ProcessingResultListener processingRresultListener = new ProcessingResultListener();
		processingRresultListener.setViewer(this.getDocuments());
		this.getDocuments().enableListeners(processingRresultListener);
		WindowListener windowListener = new WindowListener();
		windowListener.setTreeTagger(this);
		this.getFrame().addWindowListener(windowListener);
	}
	
	private void process() {
		TreeTaggerEngineListener engineListener = new TreeTaggerEngineListener();
		engineListener.setTreeTagger(this);
		engineListener.doProcess();
	}
	
	public void run() {
		if (this.isCommandLineInterface()) {
			this.process();
		} else {
			this.show();
		}
	}
	
	private void save() {
		try {
			this.getSettings().doSave();
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	public void quit() {
		this.save();
		this.hide();
		this.getFrame().dispose();
		if (this.getParent() == null) {
			System.exit(0);			
		}
	}
	
	public void quit(Exception e) {
		this.save();
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		e.printStackTrace();
		this.hide();
		this.getFrame().dispose();
		if (this.getParent() == null) {
			System.exit(1);			
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }
		boolean cli = false;
		String wrong = null;
		for (String arg : args) {
			if (arg.equals("--cli")) {
				cli = true;
				break;
			} else if (arg.equals("--gui")) {
				cli = false;
				break;
			} else {
				wrong = arg;
				break;
			}
		}
		if (wrong == null) {
			TreeTagger treeTagger = new TreeTagger(cli);
			SwingUtilities.invokeLater(treeTagger);			
		} else {
			UIMAFramework.getLogger().log(Level.SEVERE,"Wrong option: " + wrong);
			UIMAFramework.getLogger().log(Level.INFO,"Options allowed: --cli | --gui");
			System.exit(1);
		}
    }
	
	private class WindowListener extends WindowAdapter {
				
		private TreeTagger treeTagger;
		
		public void setTreeTagger(TreeTagger treeTagger) {
			this.treeTagger = treeTagger;
		}
		
		private TreeTagger getTreeTagger() {
			return this.treeTagger;
		}
		
		public void windowClosing(WindowEvent event) {
			String message = "Do you really want to quit " + this.getTreeTagger().getPreferences().getTitle() + "?";
			String title = "Exit?";
			int response = JOptionPane.showConfirmDialog(this.getTreeTagger().getFrame(),message,title,JOptionPane.OK_CANCEL_OPTION);
			if (response == 0) {
				this.getTreeTagger().quit();
			} 
		 }
		
	}
	
}
