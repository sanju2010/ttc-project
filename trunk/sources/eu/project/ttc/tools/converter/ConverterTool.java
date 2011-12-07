package eu.project.ttc.tools.converter;

import java.awt.Component;

public interface ConverterTool {

	public void setParent(Converter parent);

	public Converter getParent();

	public void error(Exception e);

	public void warning(String message);

	public void message(String message);

	public ConverterSettings getSettings();

	public Component getComponent();

}