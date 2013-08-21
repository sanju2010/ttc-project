package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.event.ActionListener;


public interface ListListener<T> extends ActionListener {

	public void setListView(ListField<T> listView); 
	
}
