package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class BooleanFieldListener implements ActionListener{

	StringItemField f1;
	FloatField f2;
	IntegerField f3;
	public BooleanFieldListener(Field f1, Field f2, Field f3) {
		// TODO Auto-generated constructor stub
		this.f1=(StringItemField)f1;
		this.f2=(FloatField)f2;
		this.f3=(IntegerField)f3;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JCheckBox cb= (JCheckBox) arg0.getSource();
		for (Component c : f1.getComponent().getComponents()) {
			c.setEnabled(cb.isSelected());
		}
		
		for (Component c : f2.getComponent().getComponents()) {
			c.setEnabled(cb.isSelected());
		}
		
		for (Component c : f3.getComponent().getComponents()) {
			c.setEnabled(cb.isSelected());
		}
	}
	

}
