package util.ui.swing.event.input;

import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

public class InputMethodAdapter implements InputMethodListener{
	private InputMethodFunc text_changed_function;
	private InputMethodFunc pos_changed_function;
	public static InputMethodAdapter create() {
		return new InputMethodAdapter();
	}
	
	public InputMethodAdapter setTextChangedCallback(InputMethodFunc func) {
		text_changed_function=func;
		return this;
	}
	
	public InputMethodAdapter setPosChangedCallback(InputMethodFunc func) {
		pos_changed_function=func;
		return this;
	}


	@Override
	public void inputMethodTextChanged(InputMethodEvent event) {
		// TODO Auto-generated method stub
		if (text_changed_function!=null) {
			text_changed_function.handle(event);
		}
	}

	@Override
	public void caretPositionChanged(InputMethodEvent event) {
		// TODO Auto-generated method stub
		if (pos_changed_function!=null) {
			pos_changed_function.handle(event);
		}
	}

}
