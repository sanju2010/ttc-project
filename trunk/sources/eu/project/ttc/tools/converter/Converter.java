package eu.project.ttc.tools.converter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.converter.flx.FlxConverter;
import eu.project.ttc.tools.converter.flx.FlxConverterEngine;
import eu.project.ttc.tools.converter.tsv.TsvConverter;
import eu.project.ttc.tools.converter.tsv.TsvConverterEngine;
import eu.project.ttc.tools.converter.zig.ZigConverter;
import eu.project.ttc.tools.converter.zig.ZigConverterEngine;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;
import eu.project.ttc.tools.utils.ToolBar;

public class Converter implements Runnable {

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
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences("converter.properties");
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
	
	private Dimension getDimension() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (4 * screen.width) / 5;
		int height = (2 * screen.height) / 5;
		Dimension dimension = new Dimension(width,height);
		return dimension;
	}
	
	private TsvConverter tsvConverter;
	
	private void setTsvConverter() {
		this.tsvConverter = new TsvConverter();
		this.tsvConverter.setParent(this);
	}
	
	private TsvConverter getTsvConverter() {
		return this.tsvConverter;
	}
	
	private FlxConverter flxConverter;
	
	private void setFlxConverter() {
		this.flxConverter = new FlxConverter();
		this.flxConverter.setParent(this);
	}
	
	private FlxConverter getFlxConverter() {
		return this.flxConverter;
	}
	
	private ZigConverter zigConverter;
	
	private void setZigConverter() {
		this.zigConverter = new ZigConverter();
		this.zigConverter.setParent(this);
	}
	
	private ZigConverter getZigConverter() {
		return this.zigConverter;
	}
	
	private JTabbedPane content;
	
	private void setContent() {
		this.content = new JTabbedPane();
		this.content.setTabPlacement(JTabbedPane.TOP);
		this.content.addTab("Tagger: XMI -> TSV        ",this.getTsvConverter().getComponent());
		this.content.addTab("Tagger: XMI -> FLX        ",this.getFlxConverter().getComponent());
		this.content.addTab("Contextualizer: XMI -> ZIG",this.getZigConverter().getComponent());
		Listener listener = new Listener();
		listener.setConverter(this);
		this.content.addChangeListener(listener);
	}
	
	private JTabbedPane getContent() {
		return this.content;
	}
	
	public boolean isTsvSelected() {
		return this.getContent().getSelectedIndex() == 0;
	}
	
	public boolean isFlxSelected() {
		return this.getContent().getSelectedIndex() == 1;
	}
	
	public boolean isZigSelected() {
		return this.getContent().getSelectedIndex() == 2;
	}
	
	private JSplitPane component;
	
	private void setComponent() {
		this.component = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.component.setTopComponent(this.getToolBar().getComponent());
		this.component.setBottomComponent(this.getContent());
		this.component.setDividerSize(0);
		this.component.setEnabled(false);
	}
	
	private JSplitPane getComponent() {
		return this.component;
	}
	
	private JFrame frame;
	
	private void setFrame() {
		this.frame = new JFrame();
		this.frame.setTitle(this.getPreferences().getTitle());
		this.frame.setPreferredSize(this.getDimension());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.getContentPane().add(this.getComponent());
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
	
	public Converter(boolean cli) {
		this.enableCommandLineInterface(cli);
		this.setPreferences();
		this.setAbout();
		this.setToolBar();
		this.setTsvConverter();
		this.setFlxConverter();
		this.setZigConverter();
		this.setContent();
		this.setComponent();
		this.setFrame();
		this.setListener();
	}
	
	private ConverterEngineListener listener;
	
	private void setListener() {
		this.listener = new ConverterEngineListener();
		this.enableListsners();
		this.getToolBar().enableListeners(this.listener);
		WindowListener windowListener = new WindowListener();
		windowListener.setConverter(this);
		this.getFrame().addWindowListener(windowListener);
	}
	
	public void enableListsners() {
		if (this.isTsvSelected()) {
			this.listener.setConverterTool(this.getTsvConverter());
			TsvConverterEngine engine = new TsvConverterEngine();
			engine.setConverterTool(this.getTsvConverter());
			this.listener.setConverterEngine(engine);
		} else if (this.isFlxSelected()) {
			this.listener.setConverterTool(this.getFlxConverter());
			FlxConverterEngine engine = new FlxConverterEngine();
			engine.setConverterTool(this.getFlxConverter());
			this.listener.setConverterEngine(engine);
		} else if (this.isZigSelected()) {
			this.listener.setConverterTool(this.getZigConverter());
			ZigConverterEngine engine = new ZigConverterEngine();
			engine.setConverterTool(this.getZigConverter());
			this.listener.setConverterEngine(engine);
		}
	}
	
	private void process() {
		/*
		ConverterEngineListener engineListener = new ConverterEngineListener();
		engineListener.setConverter(this);
		engineListener.doProcess();
		*/
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
			this.getTsvConverter().getSettings().doSave();
			this.getFlxConverter().getSettings().doSave();
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
			Converter treeTagger = new Converter(cli);
			SwingUtilities.invokeLater(treeTagger);			
		} else {
			UIMAFramework.getLogger().log(Level.SEVERE,"Wrong option: " + wrong);
			UIMAFramework.getLogger().log(Level.INFO,"Options allowed: --cli | --gui");
			System.exit(1);
		}
    }
	
	private class WindowListener extends WindowAdapter {
				
		private Converter converter;
		
		public void setConverter(Converter converter) {
			this.converter = converter;
		}
		
		private Converter getConverter() {
			return this.converter;
		}
		
		public void windowClosing(WindowEvent event) {
			String message = "Do you really want to quit " + this.getConverter().getPreferences().getTitle() + "?";
			String title = "Exit?";
			int response = JOptionPane.showConfirmDialog(this.getConverter().getFrame(),message,title,JOptionPane.OK_CANCEL_OPTION);
			if (response == 0) {
				this.getConverter().quit();
			} 
		 }
		
	}
	
	private class Listener implements ChangeListener {

		private Converter converter;
		
		public void setConverter(Converter converter) {
			this.converter = converter;
		}
		
		@Override
		public void stateChanged(ChangeEvent event) {
			this.converter.enableListsners();
		}
		
	}
	
}
