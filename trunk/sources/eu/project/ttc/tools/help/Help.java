package eu.project.ttc.tools.help;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Help implements Runnable {

	private Locale locale;
	
	private void setLocale() {
		Locale locale = Locale.getDefault();
		if (locale == null) {
			locale = Locale.ENGLISH;
		} else {
			this.locale = locale;
		}
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	private Component getHelp(String name, String locale) {
		try { 
			HelpTab help = new HelpTab(name, locale);
			return help.getComponent();
		} catch (IOException e) {
			try {
				HelpTab help = new HelpTab(name, Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
				return help.getComponent();	
			} catch (IOException ee) {
				ee.printStackTrace();
				return null;
			}
		}
	}
	
	private JTabbedPane content;
	
	private void setContent() {		
		this.content = new JTabbedPane();
		this.content.setTabPlacement(JTabbedPane.TOP);
		String locale = this.getLocale().getDisplayLanguage(Locale.ENGLISH);
		this.content.addTab("  TreeTegger  ",this.getHelp("TreeTagger", locale));
		this.content.addTab("    Termer    ",this.getHelp("Termer", locale));
		this.content.addTab("Contextualizer",this.getHelp("Contextualizer", locale));
		this.content.addTab("   Aligner    ",this.getHelp("Aligner", locale));
	}
	
	private JTabbedPane getContent() {
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
		this.frame.setTitle("Term Suite Help");
		this.frame.setPreferredSize(this.getDimension());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.getContentPane().add(this.getContent());
		this.frame.setJMenuBar(null);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.addWindowListener(new WindowListener());
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
	
	public Help() {
		this.setLocale();
		this.setContent();
		this.setFrame();
	}
		
	public void run() {
		this.show();
	}
		
	public void quit() {
		this.hide();
		this.getFrame().dispose();
	}
	
	private class WindowListener extends WindowAdapter {
		
		public void windowClosing(WindowEvent event) {
			quit();
		 }
		
	}

	public void selectTreeTagger() {
		this.getContent().setSelectedIndex(0);
	}
	
	public void selectTermer() {
		this.getContent().setSelectedIndex(1);
	}
	
	public void selectContextualizer() {
		this.getContent().setSelectedIndex(2);
	}
	
	public void selectAligner() {
		this.getContent().setSelectedIndex(3);
	}
	
}
