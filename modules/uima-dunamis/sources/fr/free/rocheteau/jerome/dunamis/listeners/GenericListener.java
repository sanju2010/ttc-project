package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.free.rocheteau.jerome.dunamis.viewers.GenericViewer;

public class GenericListener<T> implements ActionListener {

	private GenericViewer<T> viewer;
	
	public GenericListener(GenericViewer<T> viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		if (action.equals("yes")) {
			this.viewer.doApply();
		} else {
			this.viewer.doCancel();
		}
	}

}
