package uima.sandbox.filter.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.apache.uima.resource.SharedResourceObject;

public interface Filter extends SharedResourceObject {

	public Set<String> get();

	public void load(InputStream inputStream) throws IOException;
	
	public void store(OutputStream outputStream) throws IOException;

}
