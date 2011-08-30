package eu.project.ttc.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

public class ToolBar {

	/*
	private static URL Engine = ClassLoader.getSystemResource("fr/univnantes/lina/uima/tools/dunamis/images/Engine.png");
	private static URL Run = ClassLoader.getSystemResource("fr/univnantes/lina/uima/tools/dunamis/images/Run.png");
	private static URL Stop = ClassLoader.getSystemResource("fr/univnantes/lina/uima/tools/dunamis/images/Stop.png");
	private static URL Pause = ClassLoader.getSystemResource("fr/univnantes/lina/uima/tools/dunamis/images/Pause.png");
	*/
	
	private final Dimension dimension = new Dimension(66,33);
	private final Insets insets = new Insets(0,0,0,0);
	private final Color color = new Color(0,0,0,0);

	private JButton quit;
	
	private void setQuit() {
		this.quit = new JButton("Quit");
		this.quit.setActionCommand("quit");
		this.quit.setEnabled(true);
		this.quit.setPreferredSize(dimension);
		this.quit.setMargin(insets);
		this.quit.setBackground(color);
		this.quit.setBorderPainted(false);
	}
	
	public JButton getQuit() {
		return this.quit;
	}

	private JButton help;
	
	private void setHelp() {
		this.help = new JButton("Help");
		this.help.setActionCommand("help");
		this.help.setEnabled(true);
		this.help.setPreferredSize(dimension);
		this.help.setMargin(insets);
		this.help.setBackground(color);
		this.help.setBorderPainted(false);
	}
	
	public JButton getHelp() {
		return this.help;
	}
	
	private JButton about;
	
	private void setAbout() {
		this.about = new JButton("About");
		this.about.setActionCommand("about");
		this.about.setEnabled(true);
		this.about.setPreferredSize(dimension);
		this.about.setMargin(insets);
		this.about.setBackground(color);
		this.about.setBorderPainted(false);
	}
	
	public JButton getAbout() {
		return this.about;
	}
	
	private JButton run;
	
	private void setRun() {
		this.run = new JButton("Run");
		this.run.setActionCommand("run");
		this.run.setEnabled(true);
		this.run.setPreferredSize(dimension);
		this.run.setMargin(insets);
		this.run.setBackground(color);
		this.run.setBorderPainted(false);
	}
	
	public JButton getRun() {
		return this.run;
	}
	
	private JButton pause;
	
	private void setPause() {
		this.pause = new JButton("Pause");
		this.pause.setActionCommand("pause");
		this.pause.setEnabled(false);
		this.pause.setPreferredSize(dimension);
		this.pause.setMargin(insets);
		this.pause.setBackground(color);
		this.pause.setBorderPainted(false);
	}
	
	public JButton getPause() {
		return this.pause;
	}
	
	private JButton stop;
	
	private void setStop() {
		this.stop = new JButton("Stop");
		this.stop.setActionCommand("stop");
		this.stop.setEnabled(false);
		this.stop.setPreferredSize(dimension);
		this.stop.setMargin(insets);
		this.stop.setBackground(color);
		this.stop.setBorderPainted(false);
	}
	
	public JButton getStop() {
		return this.stop;
	}
	
	private JProgressBar progressBar;
	
	private void setProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setPreferredSize(new Dimension(600,33));
		this.progressBar.setStringPainted(true);
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
	private JToolBar component;
	
	private void setComponent() {
		this.component = new JToolBar();
		this.component.setFloatable(false);
		this.component.add(this.getRun());
		this.component.add(this.getPause());
		this.component.add(this.getStop());
		this.component.add(this.getProgressBar());
		this.component.add(this.getHelp());
		this.component.add(this.getAbout());
		this.component.add(this.getQuit());
	}
	
	public JToolBar getComponent(){
		return this.component;
	}
	
	public void enableListeners(ActionListener listener) {
		this.getQuit().addActionListener(listener);
		this.getAbout().addActionListener(listener);
		this.getHelp().addActionListener(listener);
		this.getStop().addActionListener(listener);
		this.getPause().addActionListener(listener);
		this.getRun().addActionListener(listener);
	}
	
	public ToolBar() {
		this.setQuit();
		this.setHelp();
		this.setAbout();
		this.setRun();
		this.setPause();
		this.setStop();
		this.setProgressBar();
		this.setComponent();
	}
	
}
