package eu.project.ttc.tools.indexer;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.config.IndexerSettings;
import eu.project.ttc.tools.config.TermSuiteSettings;

import javax.swing.*;
import java.awt.*;

public class IndexerController {

private TermSuite parent;

    private IndexerSettings settings;

    public IndexerController(TermSuiteSettings tsConfig) {
        // Save the references to the settings
        this.settings = tsConfig.getIndexerSettings();

        this.setComponent();
    }

    public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}

	public IndexerSettings getSettings() {
		return this.settings;
	}

//	private JSplitPane component;
    private JScrollPane component;
	
	private void setComponent() {
        this.component=this.getSettings().getComponent();

//		this.component = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
//		JScrollPane pp = this.getSettings().getView();
//		this.component.setTopComponent(pp);
//		JSplitPane componentAdvanced;
//		componentAdvanced = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//		componentAdvanced.setTopComponent(this.getAdvancedSettings().getView());
//		componentAdvanced.setBottomComponent(this.getTBXSettings().getView());
//		componentAdvanced.setResizeWeight(0.7);
//		this.component.setBottomComponent(componentAdvanced);
//		this.component.setContinuousLayout(true);
//		this.component.setResizeWeight(0.6);
//		this.component.setOneTouchExpandable(true);
	//	this.component.setDividerSize(1);
	}

	public Component getView() {
		return this.component;
	}

		
}