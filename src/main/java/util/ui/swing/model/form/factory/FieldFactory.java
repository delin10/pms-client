package util.ui.swing.model.form.factory;

import java.awt.Container;

import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.form.field.FormField;
import util.ui.swing.model.form.field.impl.StandardComboField;
import util.ui.swing.model.form.field.impl.StandardDefaultField;
import util.ui.swing.model.form.field.impl.StandardTableSelectorField;

public class FieldFactory {
	public static FormField create(Container container, FieldType type) {
		if (FieldType.COMBOBOX.equals(type)) {
			return new StandardComboField(container);
		} else if (FieldType.TABLE_SELECTOR.equals(type)) {
			return new StandardTableSelectorField(container);
		}else {
			return new StandardDefaultField(container);
		}
	}
}
