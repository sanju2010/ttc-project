package eu.project.ttc.models;

public interface Tree<T> {

	public Tree<T> get(T item);
	
	public boolean leaf();
	
}
