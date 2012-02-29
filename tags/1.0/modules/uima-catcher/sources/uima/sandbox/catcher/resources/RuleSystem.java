package uima.sandbox.catcher.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.uima.resource.SharedResourceObject;


public interface RuleSystem extends SharedResourceObject {

	public void clear();

	public List<Rule> get();
	
	public void load(InputStream inputStream) throws IOException;

	public void store(OutputStream outputStream) throws IOException;
	
}
