package fr.free.rocheteau.jerome.dunamis;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import fr.free.rocheteau.jerome.dunamis.listeners.CollectionProcessingEngineListener;
import fr.free.rocheteau.jerome.dunamis.listeners.EngineListener;
import fr.free.rocheteau.jerome.dunamis.listeners.MenuListener;
import fr.free.rocheteau.jerome.dunamis.listeners.FrameListener;
import fr.free.rocheteau.jerome.dunamis.listeners.ProcessingResultListener;
import fr.free.rocheteau.jerome.dunamis.menu.Menu;
import fr.free.rocheteau.jerome.dunamis.utils.About;
import fr.free.rocheteau.jerome.dunamis.utils.Preferences;
import fr.free.rocheteau.jerome.dunamis.utils.Report;
import fr.free.rocheteau.jerome.dunamis.utils.Settings;
import fr.free.rocheteau.jerome.dunamis.utils.ToolBar;
import fr.free.rocheteau.jerome.dunamis.viewers.CollectionProcessingEngineChooser;
import fr.free.rocheteau.jerome.dunamis.viewers.CollectionProcessingEngineViewer;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

public class Dunamis implements Runnable {

	public static void error(Exception e) {
		Dunamis.warning(e.getMessage());
		e.printStackTrace();
	}
	
	public static void warning(String message) {
		System.err.println(message);
	}
	
	public static void message(String message) {
		System.out.println(message);
	}
	
	private Desktop desktop;
	
	private void setDesktop() {
		if (Desktop.isDesktopSupported()) {
			this.desktop = Desktop.getDesktop();
		} else {
			Dunamis.warning("No Desktop Integration");
		}
	}
	
	public Desktop getDesktop() {
		return this.desktop;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences();
		try {
			this.preferences.load();
		} catch (Exception e) {
			Dunamis.error(e);
		}
		UIMAFramework.getLogger().log(Level.CONFIG,this.preferences.getTitle() + " " + this.preferences.getVersion());
	}
	
	public Preferences  getPreferences() {
		return this.preferences;
	}
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
		this.about.setPreferences(this.getPreferences());
	}
	
	public About getAbout() {
		return this.about;
	}
	
	private Settings settings;
	
	private void setSettings() {
		this.settings = new Settings();
	}
	
	public Settings getSettings() {
		return this.settings;
	}

	public void load() throws Exception {
		// this.getSettings().setPackages(this.getPackageViewer().getModel());
		this.getSettings().setCollectionProcessingEngines(this.getEngineChooser().getModel());
		this.getSettings().setCollectionReaders(this.getEngineViewer().getAvailableCollectonReaders());
		this.getSettings().setAnalysisEngines(this.getEngineViewer().getAvailableAnalysisEngines());
		this.getSettings().load();
	}
	
	private Menu menu;
	
	private void setMenu() {
		this.menu = new Menu();
	}
	
	private Menu getMenu() {
		return this.menu;
	}
	
	/*
	private Console console;
	
	private void setConsole() {
		this.console = new Console();
	}
	
	public Console getConsole() {
		return this.console;
	}
	
	private PackageViewer packageViewer;
	
	private void setPackageViewer() {
		this.packageViewer = new PackageViewer();
	}
	
	public PackageViewer getPackageViewer() {
		return this.packageViewer;
	}
	*/
	
	private CollectionProcessingEngineChooser engineChooser;
	
	private void setEngineChooser() {
		this.engineChooser = new CollectionProcessingEngineChooser();
	}

	public CollectionProcessingEngineChooser getEngineChooser() {
		return this.engineChooser;
	}
		
	private CollectionProcessingEngineViewer engineViewer;
	
	private void setEngineViewer() {
		this.engineViewer = new CollectionProcessingEngineViewer();
	}
		
	public CollectionProcessingEngineViewer getEngineViewer() {
		return this.engineViewer;
	}
	
	private ToolBar toolBar;
	
	private void setToolBar() {
		this.toolBar = new ToolBar();
	}
	
	public ToolBar getToolBar() {
		return this.toolBar;
	}
	/*
	private TypeSystemViewer typeSystemViewer;
	
	private void setTypeSystemViewer() {
		this.typeSystemViewer = new TypeSystemViewer();
	}
	
	public TypeSystemViewer getTypeSystemViewer() {
		return this.typeSystemViewer;
	}
	*/
	private Report report;
	
	private void setReport() {
		this.report = new Report();
	}
	
	public Report getReport() {
		return this.report;
	}
	/*
	private JTabbedPane auxiliaryInformations;
	
	private void setAuxiliaryInformations() {
		this.auxiliaryInformations = new JTabbedPane();
		this.auxiliaryInformations.addTab("  Console  ",this.getConsole().getComponent());
		this.auxiliaryInformations.addTab("Type System",this.getTypeSystemViewer().getComponent());
		this.auxiliaryInformations.addTab("  Report   ",this.getReport().getComponent());
		this.auxiliaryInformations.addTab(" Packages  ",this.getPackageViewer().getComponent());
	}
	
	private JTabbedPane getAuxiliaryInformations() {
		return this.auxiliaryInformations;
	}
	*/
	
	private ProcessingResultViewer resultViewer;
	
	private void setResultViewer() {
		this.resultViewer = new ProcessingResultViewer();
	}
	
	public ProcessingResultViewer getResultViewer() {
		return this.resultViewer;
	}
	
	private Component content;
	
	private void setContent() {
		JSplitPane inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		inner.setLeftComponent(this.getEngineChooser().getComponent());
		inner.setRightComponent(this.getEngineViewer().getComponent());
		inner.setContinuousLayout(true);
		inner.setResizeWeight(0.2);
		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.RIGHT);
		tabs.addTab("Edit",inner);
		tabs.addTab("View",this.getResultViewer().getComponent());
		
		JSplitPane outter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		outter.setTopComponent(this.getToolBar().getComponent());
		outter.setBottomComponent(tabs);
		outter.setDividerSize(0);
		outter.setEnabled(false);
		outter.setContinuousLayout(true);
		outter.setResizeWeight(0.0);

		this.content = outter;
	}
	
	private Component getContent() {
		return this.content;
	}
	
	private void enableListeners() {
		/*
		PackageListener packageListener = new PackageListener();
		packageListener.setDunamis(this);
		this.getPackageViewer().enableListeners(packageListener);
		*/
		this.getEngineViewer().enableListeners(this,this.getEngineChooser().getList());
		CollectionProcessingEngineListener cpeListener = new CollectionProcessingEngineListener();
		cpeListener.setDunamis(this);
		this.getEngineChooser().enableListeners(cpeListener);
		MenuListener menuListener = new MenuListener();
		menuListener.setDunamis(this);
		this.getMenu().enableListeners(menuListener);
		EngineListener engineListener = new EngineListener();
		engineListener.setDunamis(this);
		this.getToolBar().enableListeners(engineListener);
		ProcessingResultListener resultListener = new ProcessingResultListener();
		resultListener.setViewer(this.getResultViewer());
		this.getResultViewer().enableListeners(resultListener);
		FrameListener frameListener = new FrameListener();
		frameListener.setDunamis(this);
		this.getFrame().addWindowListener(frameListener);
	}

	private JFrame frame;
	
	private void setFrame() {
		this.frame = new JFrame();
		this.frame.setTitle(this.getPreferences().getTitle());
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.setPreferredSize(new Dimension((4 * screen.width) / 5, (4 * screen.height) / 5));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().add(this.getContent());
		this.frame.setJMenuBar(this.getMenu().getComponent());
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public Dunamis() {
		// this.setConsole();
		this.setDesktop();
		this.setPreferences();
		this.setAbout();
		this.setMenu();
		// this.setPackageViewer();
		this.setToolBar();
		// this.setTypeSystemViewer();
		this.setReport();
		// this.setAuxiliaryInformations();
		this.setEngineChooser();
		this.setEngineViewer();
		this.setResultViewer();
		this.setContent();
		this.setFrame();
		this.setSettings();
		this.enableListeners();
	}
	
	public void quit() {
		try {
			this.getSettings().doSave();
		} catch (Exception e) {
			Dunamis.error(e);
		}
		this.getFrame().setVisible(false);
		this.getFrame().dispose();
		System.exit(0);
	}
		
	public void run() { 
		try {
			this.load();
			this.check();
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}

	private void check() {
		String status = this.getSettings().getStatus();
		if (status == null) {
			this.getEngineViewer().doEnable(true);
		} else {
			JOptionPane.showMessageDialog(this.getFrame(),status,"Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	// private static URL Banner = ClassLoader.getSystemResource("fr.free.rocheteau.jerome/dunamis/images/Banner.png");
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		try {
			Dunamis dunamis = new Dunamis();
			SwingUtilities.invokeLater(dunamis);
		} catch (Exception e) {
		}
    }
	
}