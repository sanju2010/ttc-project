package eu.project.ttc.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class TermSuiteToolBar {
	
	// private final Dimension dimension = new Dimension(66,33);
	private final Dimension dimension = new Dimension(198,66);
	private final Insets insets = new Insets(10,5,10,5);
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

	private JButton acabit;
	
	private void setAcabit() {
		this.acabit = new JButton("Acabit");
		this.acabit.setActionCommand("acabit");
		this.acabit.setEnabled(true);
		this.acabit.setPreferredSize(dimension);
		this.acabit.setMargin(insets);
		this.acabit.setBackground(color);
		this.acabit.setBorderPainted(false);
	}
	
	public JButton getAcabit() {
		return this.acabit;
	}
	
	private JButton ziggurat;
	
	private void setZiggurat() {
		this.ziggurat = new JButton("Ziggurat");
		this.ziggurat.setActionCommand("ziggurat");
		this.ziggurat.setEnabled(true);
		this.ziggurat.setPreferredSize(dimension);
		this.ziggurat.setMargin(insets);
		this.ziggurat.setBackground(color);
		this.ziggurat.setBorderPainted(false);
	}
	
	public JButton getZiggurat() {
		return this.ziggurat;
	}
	
	private JButton treetagger;
	
	private void setTreeTagger() {
		this.treetagger = new JButton("TreeTagger");
		this.treetagger.setActionCommand("treetagger");
		this.treetagger.setEnabled(true);
		this.treetagger.setPreferredSize(dimension);
		this.treetagger.setMargin(insets);
		this.treetagger.setBackground(color);
		this.treetagger.setBorderPainted(false);
	}
	
	public JButton getTreeTagger() {
		return this.treetagger;
	}
	
	private JToolBar component;
	
	private void setComponent() {
		this.component = new JToolBar();
		this.component.setLayout(new GridBagLayout());
		GridBagConstraints c =  new GridBagConstraints();
		this.component.setFloatable(false);
		c.gridx = 0; c.gridy = 0;
		this.component.add(this.getAbout(),c);
		c.gridx = 0; c.gridy = 1;
		this.component.add(this.getHelp(),c);
		c.gridx = 0; c.gridy = 2;
		this.component.add(new JSeparator(JSeparator.VERTICAL),c);
		c.gridx = 0; c.gridy = 3;
		this.component.add(this.getTreeTagger(),c);
		c.gridx = 0; c.gridy = 4;
		this.component.add(this.getAcabit(),c);
		c.gridx = 0; c.gridy = 5;
		this.component.add(this.getZiggurat(),c);
		c.gridx = 0; c.gridy = 6;
		this.component.add(new JSeparator(JSeparator.VERTICAL),c);
		c.gridx = 0; c.gridy = 7;		
		this.component.add(this.getQuit(),c);
	}
	
	public JToolBar getComponent(){
		return this.component;
	}
	
	public void enableListeners(ActionListener listener) {
		this.getQuit().addActionListener(listener);
		this.getAbout().addActionListener(listener);
		this.getHelp().addActionListener(listener);
		this.getTreeTagger().addActionListener(listener);
		this.getAcabit().addActionListener(listener);
		this.getZiggurat().addActionListener(listener);
	}
	
	public TermSuiteToolBar() {
		this.setQuit();
		this.setHelp();
		this.setAbout();
		this.setAcabit();
		this.setZiggurat();
		this.setTreeTagger();
		this.setComponent();
	}
	
}
