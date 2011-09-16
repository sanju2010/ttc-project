package eu.project.ttc.models;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class WrapperConfiguration implements SharedResourceObject {
	
	private static String PreProcess = "pre-process";
		
	private static String Process = "process";
			
	private static String PostProcess = "post-process";
	
	private static String HandlerClassName = "handler-class-name";
	
	private static String Language = "\\$LANGUAGE";
	
	private static String Input = "\\$INPUT";
	
	private static String Output = "\\$OUTPUT";

	private Properties properties;
	
	private void setProperties() {
		this.properties = new Properties();
	}
	
	private Properties getProperties() {
		return this.properties;
	}
	
	private WrapperHandler handler;
	
	private void setHandler() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String handlerClassName = this.getProperties().getProperty(HandlerClassName);
		if (handlerClassName == null) {
			this.setHandler(new DefaultWrapperHandler());
		} else {
			Class<?> handlerClass = Class.forName(handlerClassName);
			Object instance = handlerClass.newInstance();
			if (instance instanceof WrapperHandler) {
				this.setHandler((WrapperHandler) instance);
			} else {
				throw new InstantiationException("The class " + handlerClassName + " doesn't inherit from"  + WrapperHandler.class.getCanonicalName());
			}
		}
	}
	
	private void setHandler(WrapperHandler handler) {
		this.handler = handler;
	}

	public WrapperHandler getHandler() {
		return handler;
	}
	
	private String preProcess;
	
	private void setPreProcess() {
		this.preProcess = this.getProperties().getProperty(PreProcess);
	}
	
	public String getPreProcess() {
		return this.preProcess;
	}

	private String process;
	
	private void setProcess() {
		this.process = this.getProperties().getProperty(Process);
	}
	
	public String getProcess(String language,String input,String output) {
		if (this.process == null) { 
			return this.process;
		} else {
			String process = this.process;
			process = process.replaceAll(Language,language);
			process = process.replaceAll(Input,input);
			process = process.replaceAll(Output,output);
			return process;
		}
	}
	
	private String postProcess;
	
	private void setPostProcess() {
		this.postProcess = this.getProperties().getProperty(PostProcess);
	}
	
	public String getPostProcess() {
		return this.postProcess;
	}
	
	public WrapperConfiguration() {
		this.setProperties();
	}

	public void doLoad(String path) throws Exception {
		InputStream inputStream = new FileInputStream(path);
		this.doLoad(inputStream);
	}

	public void doLoad(InputStream inputStream) throws Exception {
		this.getProperties().load(inputStream);
		this.setPreProcess();
		this.setProcess();
		this.setPostProcess();
		this.setHandler();
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		try {
			this.doLoad(data.getInputStream());
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	
}
