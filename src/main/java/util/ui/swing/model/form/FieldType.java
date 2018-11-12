package util.ui.swing.model.form;

public enum FieldType {
	DEFAULT_TEXT_FIELD("text_field"), RADIO_BUTTON_GROUP("radio_button_group"), COMBOBOX("combo_box"),TABLE_SELECTOR("table_selector");
	private String type;

	private FieldType(String type) {
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String toString() {
		return type;
	}
}
