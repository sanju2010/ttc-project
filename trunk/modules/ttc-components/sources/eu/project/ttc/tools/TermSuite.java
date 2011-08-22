package eu.project.ttc.tools;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

public class TermSuite implements Runnable {

	public void error(Exception e) {
		this.warning(e.getMessage());
		e.printStackTrace();
	}
	
	public void warning(String message) {
		System.err.println(message);
	}
	
	public void message(String message) {
		System.out.println(message);
	}
	
	private Desktop desktop;
	
	private void setDesktop() {
		if (Desktop.isDesktopSupported()) {
			this.desktop = Desktop.getDesktop();
		} else {
			this.warning("No Desktop Integration");
		}
	}
	
	public Desktop getDesktop() {
		return this.desktop;
	}

	private Parameters parameters;
	
	private void setParameters() {
		this.parameters = new Parameters();
	}
	
	public Parameters getParameters() {
		return this.parameters;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences();
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
	
	private Corpora sourceCorpora;
	
	private void setSourceCorpora() {
		this.sourceCorpora = new Corpora();
	}
	
	public Corpora getSourceCorpora() {
		return this.sourceCorpora;
	}

	private Corpora targetCorpora;
	
	private void setTargetCorpora() {
		this.targetCorpora = new Corpora();
	}
	
	public Corpora getTargetCorpora() {
		return this.targetCorpora;
	}
	
	private Terms terms;
	
	private void setTerms() {
		this.terms = new Terms();
	}
	
	public Terms getTerms() {
		return this.terms;
	}
	
	private Component content;
	
	private void setContent() {
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab(" Source ",this.getSourceCorpora().getComponent());
		tabs.addTab(" Target ",this.getTargetCorpora().getComponent());
		JSplitPane inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		inner.setLeftComponent(tabs);
		inner.setRightComponent(this.getTerms().getComponent());
		inner.setContinuousLayout(true);
		inner.setEnabled(true);
		inner.setResizeWeight(0.25);
		JSplitPane outter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		outter.setTopComponent(this.getToolBar().getComponent());
		outter.setBottomComponent(inner);
		outter.setDividerSize(0);
		outter.setEnabled(false);
		// outter.setContinuousLayout(true);
		// outter.setResizeWeight(1.0);
		this.content = outter;
	}
	
	private Component getContent() {
		return this.content;
	}
	
	private Dimension getDimension() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (2 * screen.width) / 3;
		int height = (2 * screen.height) / 3;
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
	
	public TermSuite() {
		this.setDesktop();
		this.setParameters();
		this.setPreferences();
		this.setAbout();
		this.setToolBar();
		this.setSourceCorpora();
		this.setTargetCorpora();
		this.setTerms();
		this.setContent();
		this.setFrame();
		this.enableListeners();
	}
	
	private void enableListeners() {
		Fire fire = new Fire();
		fire.setTermSuite(this);
		this.getToolBar().enableListeners(fire);
		WindowListener windowListener = new WindowListener();
		windowListener.setTermSuite(this);
		this.getFrame().addWindowListener(windowListener);
	}
	
	public void prepare() {
		this.getParameters().setSourceLanguage(this.getSourceCorpora().getLanguage());
		this.getParameters().setTargetLanguage(this.getTargetCorpora().getLanguage());
		this.getParameters().setSourceDirectories(this.getSourceCorpora().getDirectories());
		this.getParameters().setTargetDirectories(this.getTargetCorpora().getDirectories());
	}
	
	public void process() {
		Fire fire = new Fire();
		fire.setTermSuite(this);
		fire.doProcess();
	}
	
	public void run() {
		this.show();
	}
	
	public void quit() {
		this.hide();
		this.getFrame().dispose();
		System.exit(0);
	}
	
	public void quit(Exception e) {
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		e.printStackTrace();
		this.hide();
		this.getFrame().dispose();
		System.exit(1);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }
		TermSuite termSuite = new TermSuite();
		if (args.length == 0) {
			SwingUtilities.invokeLater(termSuite);
		} else {
			for (String arg : args) {
				try {
					termSuite.getParameters().doLoad(arg);
				} catch (IOException e) { 
					termSuite.quit(e);
				}
			}
			termSuite.process();
		}
    }
	
	private class WindowListener extends WindowAdapter {
				
		private TermSuite termSuite;
		
		public void setTermSuite(TermSuite termSuite) {
			this.termSuite = termSuite;
		}
		
		private TermSuite getTermSuite() {
			return this.termSuite;
		}
		
		public void windowClosing(WindowEvent event) {
			String message = "Do you really want to quit TTC TermSuite?";
			String title = "Exit?";
			int response = JOptionPane.showConfirmDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.OK_CANCEL_OPTION);
			if (response == 0) {
				this.getTermSuite().quit();
			} 
		 }
		
	}
	
}
