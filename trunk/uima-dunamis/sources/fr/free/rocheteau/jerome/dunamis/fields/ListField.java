package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ListField<T> {
	
	private DefaultListModel model;

	private void setModel() {
		this.model = new DefaultListModel();
	}

	protected DefaultListModel getModel() {
		return this.model;
	}

	private JList list;

	private void setList() {
		this.list = new JList(this.getModel());
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Border etchedBorder = BorderFactory.createEtchedBorder();
		this.list.setBorder(etchedBorder);
	}

	public JList getList() {
		return this.list;
	}

	private JScrollPane scroll;

	private void setScroll() {
		this.scroll = new JScrollPane(this.getList(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// TODO
		Dimension dimension = new Dimension(440,120);
		this.scroll.setPreferredSize(dimension);
	}

	private JScrollPane getScroll() {
		return this.scroll;
	}

	private Dimension getButtonDimension() {
		return new Dimension(96,32);
	}

	private JButton insert;

	private void setMoveButton() {
		this.insert = new JButton();	
		this.insert.setText("Insert");
		this.insert.setHorizontalTextPosition(SwingConstants.CENTER);
		this.insert.setHorizontalAlignment(SwingConstants.CENTER);
		this.insert.setActionCommand("move");
		this.insert.setPreferredSize(this.getButtonDimension());
	}

	private JButton getMoveButton() {
		return this.insert;
	}
	
	private JButton delete;

	private void setRemoveButton() {
		this.delete = new JButton();	
		this.delete.setText("Delete");
		this.delete.setHorizontalTextPosition(SwingConstants.CENTER);
		this.delete.setHorizontalAlignment(SwingConstants.CENTER);
		this.delete.setActionCommand("remove");
		this.delete.setPreferredSize(this.getButtonDimension());
	}

	private JButton getRemoveButton() {
		return this.delete;
	}

	private JPanel controls;
	
	private void setControls() {
		this.controls = new JPanel();
		this.controls.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.CENTER;
		this.controls.add(this.getMoveButton(),c);
		c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.CENTER;
		this.controls.add(this.getRemoveButton(),c);
	}
	
	private JPanel getControls() {
		return this.controls;
	}
	
	private TitledBorder border;
	
	private void setBorder() {
		Border border = BorderFactory.createEmptyBorder();
		int position = TitledBorder.DEFAULT_POSITION;
		int justrification = TitledBorder.DEFAULT_JUSTIFICATION;
		this.border = BorderFactory.createTitledBorder(border,"",justrification,position);		
	}
	
	public TitledBorder getBorder() {
		return this.border;
	}
	
	private JPanel component;

	private void setComponent() {
		this.component = new JPanel();
		this.component.setOpaque(false);
		this.component.add(this.getScroll());
		this.component.add(this.getControls());
		this.component.setBorder(this.getBorder());
	}

	public JPanel getComponent() {
		return this.component;
	}

	public ListField() {
		this.setModel();
		this.setList();
		this.setScroll();
		this.setMoveButton();
		this.setRemoveButton();
		this.setControls();
		this.setBorder();
		this.setComponent();
		this.enableModified(false);
	}

	public void enableListeners(ListListener<T> listener) {
		listener.setListView(this);
		this.getMoveButton().addActionListener(listener);
		this.getRemoveButton().addActionListener(listener);
	}

	public void add(T item) {
		this.getModel().addElement(item);
	}
	
	public void clear() {
		this.getModel().clear();
	}

	private boolean modified;
	
	public void enableModified(boolean enabled) {
		this.modified = enabled;
	}
	
	public boolean isModified() {
		return this.modified;
	}

}

