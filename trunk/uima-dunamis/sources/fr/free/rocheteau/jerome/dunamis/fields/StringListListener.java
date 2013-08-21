package fr.free.rocheteau.jerome.dunamis.fields;

import javax.swing.JOptionPane;

public class StringListListener extends AbstractListListener<String> {
	
	@Override
	public void doMove() {
		String message = "Please, enter a value: ";
		String value = (String) JOptionPane.showInputDialog(this.getList(),message,"Value",JOptionPane.PLAIN_MESSAGE,null,null,"");
		if (value != null && !value.isEmpty()) {
		    this.getModel().addElement(value);
		}
	}
	
}
