package eu.project.ttc.tools;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.help.Help;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;

public class TermSuite implements Runnable {

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
		} else {
			this.warning("No Desktop Integration");
		}
	}
	
	public Desktop getDesktop() {
		return this.desktop;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences("term-suite.properties");
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
	
	private Help help;
	
	private void setHelp() {
		this.help = new Help();
	}
	
	public Help getHelp() {
		return this.help;
	}
	
	public Preferences  getPreferences() {
		return this.preferences;
	}

	private TermSuiteToolBar toolBar;
	
	private void setToolBar() {
		this.toolBar = new TermSuiteToolBar();
	}
	
	public TermSuiteToolBar getToolBar() {
		return this.toolBar;
	}	
	
	private Point getPoint() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frame = this.frame.getSize();
		Point point = new Point();
		point.setLocation(screen.width / 10, (screen.height - frame.height) / 2);
		return point;
	}

	
	private JFrame frame;
	
	private void setFrame() {
		this.frame = new JFrame();
		this.frame.setTitle(this.getPreferences().getTitle());
		// this.frame.setPreferredSize(this.getDimension());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.getContentPane().add(this.getToolBar().getComponent());
		this.frame.setJMenuBar(null);
		this.frame.pack();
		this.frame.setLocation(this.getPoint()); // RelativeTo(null);
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
	
	public TermSuite(boolean cli) {
		this.enableCommandLineInterface(cli);
		this.setDesktop();
		this.setHelp();
		this.setPreferences();
		this.setAbout();
		this.setToolBar();
		this.setFrame();
		this.enableListeners();
	}
	
	private void enableListeners() {
		TermSuiteListener listener = new TermSuiteListener();
		listener.setTermSuite(this);
		this.getToolBar().enableListeners(listener);
		WindowListener windowListener = new WindowListener();
		windowListener.setTermSuite(this);
		this.getFrame().addWindowListener(windowListener);
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
			TermSuite termSuite = new TermSuite(cli);
			SwingUtilities.invokeLater(termSuite);			
		} else {
			UIMAFramework.getLogger().log(Level.SEVERE,"Wrong option: " + wrong);
			UIMAFramework.getLogger().log(Level.INFO,"Options allowed: --cli | --gui");
			System.exit(1);
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
			String message = "Do you really want to quit " + this.getTermSuite().getPreferences().getTitle() + "?";
			String title = "Exit?";
			int response = JOptionPane.showConfirmDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.OK_CANCEL_OPTION);
			if (response == 0) {
				this.getTermSuite().quit();
			} 
		 }
		
	}
	
}
