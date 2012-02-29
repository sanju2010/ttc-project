package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFileChooser;

public class FieldFactory {

	public static Field getComponent(String type,String name,boolean multi,Object value,String description) {
		Field field = null;
		if (multi) {
			if (type.equals("Boolean")) {
				field = new WeirdField();
			} else if (type.equals("Integer")) {
				field = new WeirdField();
			} else if (type.equals("Float")) {
				field = new WeirdField();
			} else if (type.equals("String")) {
				if (name.endsWith("Files") || name.endsWith("Descriptors") || name.endsWith("Directories")) {
					field = new FileListField();
				} else {
					field = new StringListField();
				}
			}
		} else {
			if (type.equals("Boolean")) {
				field = new BooleanField();
			} else if (type.equals("Integer")) {
				field = new IntegerField();
			} else if (type.equals("Float")) {
				field = new FloatField();
			} else if (type.equals("String")) {
				String[] values = FieldFactory.getValues(description);
				if (values == null) {
					if (name.endsWith("File") || name.endsWith("Descriptor") || name.endsWith("Directory")) {
						field = new FileField();
					} else {
						field = new StringField();
					}					
				} else {
					if (name.endsWith("File") || name.endsWith("Descriptor") || name.endsWith("Directory")) {
						field = new FileField();
					} else if (name.endsWith("Language")) {
						field = new LanguageItemField(values);
					} else {
						field = new StringItemField(values);
					}
				}
			}
		}
		field.setName(name);
		field.setValue(value);
		return field;
	}

	public static String getTitle(String name) {
		StringBuffer title = new StringBuffer();
		boolean done = false;
		for (char ch : name.toCharArray()) {
			if (!done) {
				title.append(Character.toUpperCase(ch));
				done = true;
			} else if (Character.isUpperCase(ch)) {
				title.append(" " + ch);
			} else {
				title.append(ch);
			}
		}
		return title.toString();
	}

	private static String[] getValues(String description) {
		if (description == null) {
			return null;
		} else {
			String[] lines = description.split("\n");
			for (String line : lines) {
				if (line.startsWith("values:")) {
					String[] items = line.substring(7).split("\\s*\\|\\s*");
					String[] values = new String[items.length];
					for (int i = 0; i < items.length; i++) {
						values[i] = items[i].trim();
					}
					return items;
				}
			}
			return null;
		}
	}
	
	private static Dimension getDimension() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (2 * screen.width) / 3;
		int height = (2 * screen.height) / 3;
		Dimension dimension = new Dimension(width,height);
		return dimension;
	}
	
	private static JFileChooser chooser;
	
	static {
		FieldFactory.chooser = new JFileChooser();
		FieldFactory.chooser.setDialogTitle("File Chooser");
		// TODO
		FieldFactory.chooser.setPreferredSize(FieldFactory.getDimension());
		FieldFactory.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
	}
	
	public static JFileChooser getChooser() {
		return FieldFactory.chooser;
	}
	
}
