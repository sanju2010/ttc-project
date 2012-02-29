package fr.free.rocheteau.jerome.dunamis.fields;

import java.io.File;

public class FileListField extends ListField<File> implements Field {

	private String name;
	
	@Override
	public void setName(String name) {
		this.name = name;
		String title = FieldFactory.getTitle(name);
		this.getBorder().setTitle(title);
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setValue(Object value) {
		if (value instanceof Object[]) {
			Object[] values = (Object[]) value;
			for (Object val : values) {
				String item = val.toString();
				File file = new File(item);
				this.getModel().addElement(file);
			}
		} 
	}

	@Override
	public String[] getValue() {
		int size = this.getModel().size();
		String[] value = new String[size];
		for (int index = 0; index < size; index++) {
			File file = (File) this.getModel().getElementAt(index);
			value[index] = file.getAbsolutePath();
		}
		return value;
	}
		
	private void enableListeners() {
		FileListListener listener = new FileListListener();
		super.enableListeners(listener);
	}
		
	public FileListField() {
		super();
		this.enableListeners();
	}

}
