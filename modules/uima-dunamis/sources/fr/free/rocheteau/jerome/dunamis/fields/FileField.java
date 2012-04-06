package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class FileField implements Field {

	private String name;
	
	@Override
	public  void setName(String name) {
		this.name = name;
		this.getBorder().setTitle(FieldFactory.getTitle(name));
	}
	
	@Override
	public String getName() {
		return this.name;
	}
		
	@Override
	public void setValue(Object value) {
		this.getText().setText(value == null ? null : value.toString());
	}
	
	@Override
	public String getValue() {
		String value = this.text.getText();
		if (value == null) {
			return null;
		} else if (value.isEmpty()) {
			return null;
		} else {
			return value;
		}
	}

	public FileField() {
		this.setBorder();
		this.setBrowse();
		this.setText();
		this.setComponent();
		this.enableListsners();
	}
	
	private Dimension getButtonDimension() {
		return new Dimension(96,32);
	}
	
	private JButton browse;

	private void setBrowse() {
		this.browse = new JButton();	
		this.browse.setText("Browse");
		this.browse.setHorizontalTextPosition(SwingConstants.CENTER);
		this.browse.setHorizontalAlignment(SwingConstants.CENTER);
		this.browse.setActionCommand("browse");
		this.browse.setPreferredSize(this.getButtonDimension());
	}

	private JButton getBrowse() {
		return this.browse;
	}

	private TitledBorder border;
	
	private void setBorder() {
		Border border = BorderFactory.createEmptyBorder();
		int position = TitledBorder.DEFAULT_POSITION;
		int justrification = TitledBorder.DEFAULT_JUSTIFICATION;
		this.border = BorderFactory.createTitledBorder(border,"",justrification,position);		
	}
	
	private TitledBorder getBorder() {
		return this.border;
	}

	private JTextField text;
	
	private void setText() {
		this.text = new JTextField(33);
		
	}

	public JTextField getText() {
		return this.text;
	}
	
	private JPanel component;
	
	private void setComponent() {
		this.component = new JPanel();
		this.component.setOpaque(false);
		this.component.setBorder(this.getBorder());
		this.component.add(this.getText());
		this.component.add(this.getBrowse());
	}

	@Override
	public JPanel getComponent() {
		return this.component;
	}
	
	private void enableListsners() {
		Listener listener = new Listener();
		listener.setFileField(this);
		this.getBrowse().addActionListener(listener);
	}
	
	private class Listener implements ActionListener {

		private FileField fileField;
		
		public void setFileField(FileField fileField) {
			this.fileField = fileField;
		}
		
		private FileField getFileField() {
			return this.fileField;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			String path = this.getFileField().getText().getText();
			if (path != null) {
				File current = new File(path);
				if (current.exists()) {
					FieldFactory.getChooser().setCurrentDirectory(current.getParentFile());
				}
			}
			String command = event.getActionCommand();
			if (command.equals("browse")) {
				int rv = FieldFactory.getChooser().showOpenDialog(/*this.getFileField().getComponent()*/null);
				if (rv == JFileChooser.APPROVE_OPTION) {
					File file = FieldFactory.getChooser().getSelectedFile();
					this.getFileField().setValue(file);
				}
			}
		}
		
	}

	@Override
	public boolean isModified() {
		return true;
	}
	public void setListener(Field f1, Field f2, Field f3)
	{
		
	}
}
