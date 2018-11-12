package util.ui.swing.event.focus;

public class FocusRecordAdapter extends FocusAdapter {
	private Object value;
	
	public static FocusRecordAdapter create() {
		return new FocusRecordAdapter();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
