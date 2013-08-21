package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import fr.free.rocheteau.jerome.dunamis.listeners.GenericListener;

public abstract class GenericViewer<T> {
	
	protected abstract JComponent getComponent();
	
	protected abstract Dimension getComponentDimension();
	
	private JScrollPane scroll;
	
	private void setScroll() {
		this.scroll = new JScrollPane(this.getComponent());
		Dimension size = this.getComponentDimension();
		Dimension dimension = new Dimension(size.width + 5,size.height + 5); 
		this.scroll.setPreferredSize(dimension);
		this.scroll.setMinimumSize(new Dimension(416,312));
	}
	
	private JScrollPane getScroll() {
		return this.scroll;
	}
	
	private JButton yes;
	
	private void setYesButton(boolean dual) {
		String name = dual ? "Apply" : "All Right" ;
		this.yes = new JButton(name);
		this.yes.setActionCommand("yes");
		this.yes.setPreferredSize(new Dimension(100,33));
		this.yes.setMargin(new Insets(0,0,0,0));
		this.yes.setBackground(new Color(0,0,0,0));
		this.yes.setBorderPainted(false);
	}
	
	
	public JButton getYesButton() {
		return this.yes;
	}
	
	private JButton no;
	
	private void setNoButton(boolean dual) {
		if (dual) {
			this.no = new JButton("Cancel");
			this.no.setActionCommand("no");
			this.no.setPreferredSize(new Dimension(100,33));
			this.no.setMargin(new Insets(0,0,0,0));
			this.no.setBackground(new Color(0,0,0,0));
			this.no.setBorderPainted(false);
		}
	}
	
	public JButton getNoButton() {
		return this.no;
	}
	
	private JPanel buttons;
	
	private void setButtons(boolean dual) {
		this.buttons = new JPanel();
		this.buttons.add(this.getYesButton());
		if (dual) this.buttons.add(this.getNoButton());
	}
	
	private JPanel getButtons() {
		return this.buttons;
	}
	
	private JSplitPane content;
	
	private void setContent() {
		this.content = new JSplitPane();
		this.content.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.content.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		this.content.setLeftComponent(this.getScroll());
		this.content.setRightComponent(this.getButtons());
		this.content.setEnabled(false);
		this.content.setResizeWeight(1.0);
	}
	
	private JSplitPane getContent() {
		return this.content;
	}
	
	private JFrame frame;
	
	private void setFrame(String title) {
		this.frame = new JFrame(title);
		this.frame.getContentPane().add(this.getContent());
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
	}
	
	protected JFrame getFrame() {
		return this.frame;
	}

	private void enableListeners(boolean dual) {
		ActionListener listener = new GenericListener<T>(this);
		this.getYesButton().addActionListener(listener);
		if (dual) this.getNoButton().addActionListener(listener);
	}
	
	public void hide() {
		this.getFrame().setVisible(false);
		this.getFrame().dispose();
	}
	
	public abstract void doApply();
	
	public abstract void doCancel();
	
	public abstract T getValue();
	
	public void show() {
		this.getFrame().setVisible(true);
	}

	protected void build(String title) {
		this.build(title,true);
	}
	
	protected void build(String title,boolean dual) {
		this.setScroll();
		this.setYesButton(dual);
		this.setNoButton(dual);
		this.setButtons(dual);
		this.setContent();
		this.setFrame(title);
		this.enableListeners(dual);
	}
	
}
