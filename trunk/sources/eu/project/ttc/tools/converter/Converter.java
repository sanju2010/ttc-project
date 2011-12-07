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
import eu.project.ttc.tools.converter.tbx.TbxXmiConverter;
import eu.project.ttc.tools.converter.tbx.TbxXmiConverterEngine;
import eu.project.ttc.tools.converter.tbx.XmiTbxConverter;
import eu.project.ttc.tools.converter.tbx.XmiTbxConverterEngine;
import eu.project.ttc.tools.converter.tsv.TsvXmiConverter;
import eu.project.ttc.tools.converter.tsv.TsvXmiConverterEngine;
import eu.project.ttc.tools.converter.tsv.XmiTsvConverter;
import eu.project.ttc.tools.converter.tsv.XmiTsvConverterEngine;
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
	
	private XmiTsvConverter xmiTsvConverter;
	
	private void setXmiTsvConverter() {
		this.xmiTsvConverter = new XmiTsvConverter();
		this.xmiTsvConverter.setParent(this);
	}
	
	private XmiTsvConverter getXmiTsvConverter() {
		return this.xmiTsvConverter;
	}
	
	private TsvXmiConverter tsvXmiConverter;
	
	private void setTsvXmiConverter() {
		this.tsvXmiConverter = new TsvXmiConverter();
		this.tsvXmiConverter.setParent(this);
	}
	
	private TsvXmiConverter getTsvXmiConverter() {
		return this.tsvXmiConverter;
	}
	
	private XmiTbxConverter xmiTbxConverter;
	
	private void setXmiTbxConverter() {
		this.xmiTbxConverter = new XmiTbxConverter();
		this.xmiTbxConverter.setParent(this);
	}
	
	private XmiTbxConverter getXmiTbxConverter() {
		return this.xmiTbxConverter;
	}
	
	private TbxXmiConverter tbxXmiConverter;
	
	private void setTbxXmiConverter() {
		this.tbxXmiConverter = new TbxXmiConverter();
		this.tbxXmiConverter.setParent(this);
	}
	
	private TbxXmiConverter getTbxXmiConverter() {
		return this.tbxXmiConverter;
	}
	
	private JTabbedPane content;
	
	private void setContent() {
		this.content = new JTabbedPane();
		this.content.setTabPlacement(JTabbedPane.TOP);
		this.content.addTab(" XMI -> TSV ",this.getXmiTsvConverter().getComponent());
		this.content.addTab(" TSV -> XMI ",this.getTsvXmiConverter().getComponent());
		this.content.addTab(" XMI -> TBX ",this.getXmiTbxConverter().getComponent());
		this.content.addTab(" TBX -> XMI ",this.getTbxXmiConverter().getComponent());
		Listener listener = new Listener();
		listener.setConverter(this);
		this.content.addChangeListener(listener);
	}
	
	private JTabbedPane getContent() {
		return this.content;
	}
	
	public boolean isXmiTsvSelected() {
		return this.getContent().getSelectedIndex() == 0;
	}
	
	public boolean isTsvXmiSelected() {
		return this.getContent().getSelectedIndex() == 1;
	}
	
	public boolean isXmiTbxSelected() {
		return this.getContent().getSelectedIndex() == 2;
	}
	
	public boolean isTbxXmiSelected() {
		return this.getContent().getSelectedIndex() == 3;
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
		this.setXmiTsvConverter();
		this.setTsvXmiConverter();
		this.setXmiTbxConverter();
		this.setTbxXmiConverter();
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
		if (this.isXmiTsvSelected()) {
			this.listener.setConverterTool(this.getXmiTsvConverter());
			XmiTsvConverterEngine engine = new XmiTsvConverterEngine();
			engine.setConverterTool(this.getXmiTsvConverter());
			this.listener.setConverterEngine(engine);
		} else if (this.isTsvXmiSelected()) {
			this.listener.setConverterTool(this.getTsvXmiConverter());
			TsvXmiConverterEngine engine = new TsvXmiConverterEngine();
			engine.setConverterTool(this.getTsvXmiConverter());
			this.listener.setConverterEngine(engine);
		} else if (this.isXmiTbxSelected()) {
			this.listener.setConverterTool(this.getXmiTbxConverter());
			XmiTbxConverterEngine engine = new XmiTbxConverterEngine();
			engine.setConverterTool(this.getXmiTbxConverter());
			this.listener.setConverterEngine(engine);
		}  else if (this.isTbxXmiSelected()) {
			this.listener.setConverterTool(this.getTbxXmiConverter());
			TbxXmiConverterEngine engine = new TbxXmiConverterEngine();
			engine.setConverterTool(this.getTbxXmiConverter());
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
			this.getXmiTsvConverter().getSettings().doSave();
			this.getTsvXmiConverter().getSettings().doSave();
			this.getXmiTbxConverter().getSettings().doSave();
			this.getTbxXmiConverter().getSettings().doSave();
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
