package fr.free.rocheteau.jerome.dunamis.models;

public interface ProcessorSettingModel {

	public void setCasPoolSize(int casPoolSize) throws Exception;
		
    public void setProcessingUnitThreadCount(int processUnitThreadCount) throws Exception;
		
    public void setDropOnCasException(boolean dropOnCasException) throws Exception;
    
    public int getCasPoolSize() throws Exception;
	
    public int getProcessingUnitThreadCount() throws Exception;
		
    public boolean getDropOnCasException() throws Exception;
	
}
