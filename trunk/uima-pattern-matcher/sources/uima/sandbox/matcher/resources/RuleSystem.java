package uima.sandbox.matcher.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.uima.resource.SharedResourceObject;

import uima.sandbox.matcher.models.trees.Tree;

public interface RuleSystem extends SharedResourceObject {

	public Tree get();

	public void clear();

	public void load(InputStream inputStream) throws IOException;
	
	public void store(OutputStream outputStream) throws IOException;
	
}
