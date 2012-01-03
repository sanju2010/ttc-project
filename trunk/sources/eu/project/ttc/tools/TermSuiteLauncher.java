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

public class TermSuiteLauncher {
	
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
		this.acabit = new JButton("Termer");
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
	
	private JButton katastasis;
	
	private void setKatastasis() {
		this.katastasis = new JButton("Contextualizer");
		this.katastasis.setActionCommand("katastasis");
		this.katastasis.setEnabled(true);
		this.katastasis.setPreferredSize(dimension);
		this.katastasis.setMargin(insets);
		this.katastasis.setBackground(color);
		this.katastasis.setBorderPainted(false);
	}
	
	public JButton getKatastasis() {
		return this.katastasis;
	}
	
	private JButton mimesis;
	
	private void setMimesis() {
		this.mimesis = new JButton("Relater");
		this.mimesis.setActionCommand("mimesis");
		this.mimesis.setEnabled(true);
		this.mimesis.setPreferredSize(dimension);
		this.mimesis.setMargin(insets);
		this.mimesis.setBackground(color);
		this.mimesis.setBorderPainted(false);
	}
	
	public JButton getMimesis() {
		return this.mimesis;
	}
	
	private JButton ziggurat;
	
	private void setZiggurat() {
		this.ziggurat = new JButton("Aligner");
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
	
	private JButton tagger;
	
	private void setTagger() {
		this.tagger = new JButton("Tagger");
		this.tagger.setActionCommand("tagger");
		this.tagger.setEnabled(true);
		this.tagger.setPreferredSize(dimension);
		this.tagger.setMargin(insets);
		this.tagger.setBackground(color);
		this.tagger.setBorderPainted(false);
	}
	
	public JButton getTagger() {
		return this.tagger;
	}
	
	private JButton converter;
	
	private void setConverter() {
		this.converter = new JButton("Converter");
		this.converter.setActionCommand("converter");
		this.converter.setEnabled(true);
		this.converter.setPreferredSize(dimension);
		this.converter.setMargin(insets);
		this.converter.setBackground(color);
		this.converter.setBorderPainted(false);
	}
	
	public JButton getConverter() {
		return this.converter;
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
		this.component.add(this.getTagger(),c);
		c.gridx = 0; c.gridy = 4;
		this.component.add(this.getAcabit(),c);
		c.gridx = 0; c.gridy = 5;
		this.component.add(this.getKatastasis(),c);
		c.gridx = 0; c.gridy = 6;
		this.component.add(this.getMimesis(),c);
		c.gridx = 0; c.gridy = 7;
		this.component.add(this.getZiggurat(),c);
		c.gridx = 0; c.gridy = 8;
		this.component.add(new JSeparator(JSeparator.VERTICAL),c);
		c.gridx = 0; c.gridy = 9;		
		this.component.add(this.getConverter(),c);
		c.gridx = 0; c.gridy = 10;		
		this.component.add(new JSeparator(JSeparator.VERTICAL),c);
		c.gridx = 0; c.gridy = 11;		
		this.component.add(this.getQuit(),c);
	}
	
	public JToolBar getComponent(){
		return this.component;
	}
	
	public void enableListeners(ActionListener listener) {
		this.getQuit().addActionListener(listener);
		this.getAbout().addActionListener(listener);
		this.getHelp().addActionListener(listener);
		this.getTagger().addActionListener(listener);
		this.getAcabit().addActionListener(listener);
		this.getKatastasis().addActionListener(listener);
		this.getMimesis().addActionListener(listener);
		this.getZiggurat().addActionListener(listener);
		this.getConverter().addActionListener(listener);
	}
	
	public TermSuiteLauncher() {
		this.setQuit();
		this.setHelp();
		this.setAbout();
		this.setAcabit();
		this.setKatastasis();
		this.setMimesis();
		this.setZiggurat();
		this.setTagger();
		this.setConverter();
		this.setComponent();
	}
	
}
