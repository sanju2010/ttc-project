package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;

abstract public class AbstractListListener<T> implements ListListener<T> {
	
	private ListField<T> listView;
	
	public void setListView(ListField<T> listView) {
		this.listView = listView;
	}
	
	protected DefaultListModel getModel() {
		return this.listView.getModel();
	}
	
	protected JList getList() {
		return this.listView.getList();
	}
	
	abstract void doMove();
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals("move")) {
			this.doMove();
			this.listView.enableModified(true);
		} else if (command.equals("remove")) {
			int index = this.getList().getSelectedIndex();
			if (index != -1) {
				this.getModel().removeElementAt(index);
				this.listView.enableModified(true);
			}
		}
	}

}
