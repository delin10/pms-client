package util.ui.swing.model.form;

import javax.swing.JScrollPane;

public abstract class Form extends JScrollPane{
	
	private static final long serialVersionUID = 1940580445629291447L;
	public abstract Form onSubmit(FormFunc e);
	public abstract Form onCanel(FormFunc e);
}
