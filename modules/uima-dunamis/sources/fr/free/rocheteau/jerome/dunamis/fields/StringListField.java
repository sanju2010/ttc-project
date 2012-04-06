package fr.free.rocheteau.jerome.dunamis.fields;

public class StringListField extends ListField<String> implements Field {

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

	private String[] value;
	
	@Override
	public void setValue(Object value) {
		if (value instanceof String[]) {
			this.value = (String[]) value;
			for (String item : this.value) {
				this.getModel().addElement(item);
			}
		} else {
			this.value = null;
		}
	}

	@Override
	public String[] getValue() {
		int size = this.getModel().size();
		String[] value = new String[size];
		for (int index = 0; index < size; index++) {
			value[index] = (String) this.getModel().getElementAt(index);
		}
		return value;
	}
		
	private void enableListeners() {
		StringListListener listener = new StringListListener();
		super.enableListeners(listener);
	}
		
	public StringListField() {
		super();
		this.enableListeners();
	}
	public void setListener(Field f1, Field f2, Field f3)
	{
		
	}
}
