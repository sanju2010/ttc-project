package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.Component;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.ProcessorSettingModel;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessorSettingViewer;

public class ProcessorSettingListener implements ChangeListener {

	ProcessorSettingViewer processorSettingViewer;
	
	private void setViewer(ProcessorSettingViewer processorSettingViewer) {
		this.processorSettingViewer = processorSettingViewer;
	}
	
	private ProcessorSettingViewer getViewer() {
		return this.processorSettingViewer;
	}
	
	public ProcessorSettingListener(ProcessorSettingViewer processorSettingViewer) {
		this.setViewer(processorSettingViewer);
	}	
	
	@Override
	public void stateChanged(ChangeEvent event) {
		ProcessorSettingModel model = this.getViewer().getModel();
		Component component  = (Component) event.getSource();
		if (component.getName().equals(this.getViewer().getCasPoolSizeField().getName())) {
			int casPoolSize = this.getViewer().getCasPoolSize();
			try {
				if (model != null) {
					model.setCasPoolSize(casPoolSize);	
				}
			} catch (Exception e) {
				Dunamis.error(e);
			}
			int processingUnitThreadCount = this.getViewer().getProcessingUnitThreadCount();
			if (casPoolSize < processingUnitThreadCount) {
				this.getViewer().setProcessUnitThreadCount(casPoolSize);
				try {
					if (model != null) {
						model.setProcessingUnitThreadCount(casPoolSize);
					}
				} catch (Exception e) {
					Dunamis.error(e);
				}
			}
		} else if (component.getName().equals(this.getViewer().getProcessUnitThreadField().getName())) {			
			int processingUnitThreadCount = this.getViewer().getProcessingUnitThreadCount();
			try {
				if (model != null) {
					model.setProcessingUnitThreadCount(processingUnitThreadCount);
				}
			} catch (Exception e) {
				Dunamis.error(e);
			}
			int casPoolSize = this.getViewer().getCasPoolSize();
			if (casPoolSize < processingUnitThreadCount) {
				this.getViewer().setCasPoolSize(processingUnitThreadCount);
				try {
					if (model != null) {
						model.setCasPoolSize(processingUnitThreadCount);
					}
				} catch (Exception e) {
					Dunamis.error(e);
				}
			}			
		}  
	}

}
