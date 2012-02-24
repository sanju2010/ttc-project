package eu.project.ttc.tools.converter;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteListener;
import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.TermSuiteTool;
import eu.project.ttc.tools.converter.tbx.TbxXmiConverter;
import eu.project.ttc.tools.converter.tbx.TbxXmiConverterEngine;
import eu.project.ttc.tools.converter.tbx.XmiTbxConverter;
import eu.project.ttc.tools.converter.tbx.XmiTbxConverterEngine;
import eu.project.ttc.tools.converter.tsv.TsvXmiConverter;
import eu.project.ttc.tools.converter.tsv.TsvXmiConverterEngine;
import eu.project.ttc.tools.converter.tsv.XmiTsvConverter;
import eu.project.ttc.tools.converter.tsv.XmiTsvConverterEngine;

public class Converter implements TermSuiteTool {

	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
		this.xmiTsvConverter.setParent(this.getParent());
		this.tsvXmiConverter.setParent(this.getParent());
		this.xmiTbxConverter.setParent(this.getParent());
		this.tbxXmiConverter.setParent(this.getParent());
	}
	
	public TermSuite getParent() {
		return this.parent;
	}	
	
	private XmiTsvConverter xmiTsvConverter;
	
	private void setXmiTsvConverter() {
		this.xmiTsvConverter = new XmiTsvConverter();
	}
	
	private XmiTsvConverter getXmiTsvConverter() {
		return this.xmiTsvConverter;
	}
	
	private TsvXmiConverter tsvXmiConverter;
	
	private void setTsvXmiConverter() {
		this.tsvXmiConverter = new TsvXmiConverter();
	}
	
	private TsvXmiConverter getTsvXmiConverter() {
		return this.tsvXmiConverter;
	}
	
	private XmiTbxConverter xmiTbxConverter;
	
	private void setXmiTbxConverter() {
		this.xmiTbxConverter = new XmiTbxConverter();
	}
	
	private XmiTbxConverter getXmiTbxConverter() {
		return this.xmiTbxConverter;
	}
	
	private TbxXmiConverter tbxXmiConverter;
	
	private void setTbxXmiConverter() {
		this.tbxXmiConverter = new TbxXmiConverter();
	}
	
	private TbxXmiConverter getTbxXmiConverter() {
		return this.tbxXmiConverter;
	}
	
	private JTabbedPane content;
	
	private void setContent() {
		this.content = new JTabbedPane();
		this.content.setTabPlacement(JTabbedPane.RIGHT);
		this.content.addTab(" XMI -> TSV ",this.getXmiTsvConverter().getComponent());
		this.content.addTab(" TSV -> XMI ",this.getTsvXmiConverter().getComponent());
		this.content.addTab(" XMI -> TBX ",this.getXmiTbxConverter().getComponent());
		this.content.addTab(" TBX -> XMI ",this.getTbxXmiConverter().getComponent());
		Listener listener = new Listener();
		listener.setConverter(this);
		this.content.addChangeListener(listener);
	}
	
	private JTabbedPane getContent() {
		return this.content;
	}
	
	public boolean isXmiTsvSelected() {
		return this.getContent().getSelectedIndex() == 0;
	}
	
	public boolean isTsvXmiSelected() {
		return this.getContent().getSelectedIndex() == 1;
	}
	
	public boolean isXmiTbxSelected() {
		return this.getContent().getSelectedIndex() == 2;
	}
	
	public boolean isTbxXmiSelected() {
		return this.getContent().getSelectedIndex() == 3;
	}

	@Override
	public JTabbedPane getComponent() {
		return this.content;
	}	
	
	public Converter() {
		this.setXmiTsvConverter();
		this.setTsvXmiConverter();
		this.setXmiTbxConverter();
		this.setTbxXmiConverter();
		this.setContent();
	}
	
	TermSuiteListener listener;
	
	public void enableListsners(TermSuiteListener listener) {
		this.listener = listener;
	}
	
	private void enableListsners() {
		if (this.isXmiTsvSelected()) {
			listener.setTermSuiteTool(this.getXmiTsvConverter());
			XmiTsvConverterEngine engine = new XmiTsvConverterEngine();
			engine.setTermSuiteTool(this.getXmiTsvConverter());
			listener.setTermSuiteEngine(engine);
		} else if (this.isTsvXmiSelected()) {
			listener.setTermSuiteTool(this.getTsvXmiConverter());
			TsvXmiConverterEngine engine = new TsvXmiConverterEngine();
			engine.setTermSuiteTool(this.getTsvXmiConverter());
			listener.setTermSuiteEngine(engine);
		}  else if (this.isXmiTbxSelected()) {
			listener.setTermSuiteTool(this.getXmiTbxConverter());
			XmiTbxConverterEngine engine = new XmiTbxConverterEngine();
			engine.setTermSuiteTool(this.getXmiTbxConverter());
			listener.setTermSuiteEngine(engine);
		}  else if (this.isTbxXmiSelected()) {
			listener.setTermSuiteTool(this.getTbxXmiConverter());
			TbxXmiConverterEngine engine = new TbxXmiConverterEngine();
			engine.setTermSuiteTool(this.getTbxXmiConverter());
			listener.setTermSuiteEngine(engine);
		}
	}
	
	public void doSave() {
		try {
			this.getXmiTsvConverter().getSettings().doSave();
			this.getTsvXmiConverter().getSettings().doSave();
			this.getXmiTbxConverter().getSettings().doSave();
			this.getTbxXmiConverter().getSettings().doSave();
		} catch (Exception e) {
			this.getParent().error(e);
		}
	}

	@Override
	public TermSuiteSettings getSettings() {
		if (this.isXmiTsvSelected()) {
			return this.getXmiTsvConverter().getSettings();
		} else if (this.isTsvXmiSelected()) {
			return this.getTsvXmiConverter().getSettings();
		} else if (this.isXmiTbxSelected()) {
			return this.getXmiTbxConverter().getSettings();
		} else if (this.isTbxXmiSelected()) {
			return this.getTbxXmiConverter().getSettings();
		} else {
			return null;
		}
	}

	private class Listener implements ChangeListener {

		private Converter converter;
		
		public void setConverter(Converter converter) {
			this.converter = converter;
		}
		
		@Override
		public void stateChanged(ChangeEvent arg0) {
			this.converter.enableListsners();
		}
		
	}
	
}
