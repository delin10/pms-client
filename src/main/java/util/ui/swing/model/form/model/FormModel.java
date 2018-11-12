package util.ui.swing.model.form.model;

import java.awt.Dimension;

import util.ui.swing.model.form.FieldType;

public interface FormModel {
	public String getLabel(String field);
	public Object getValue(String field);
	public boolean hidden(String field);
	public boolean disable(String field);
	public void disable(String[]field);
	public Dimension inputSize(String field);
	public Dimension labelSize(String field);
	public Dimension formSize();
	public String[] fields();
	public FieldType type(String field);
	public void setValue(String field,Object value);
	public void setType(String field,FieldType type);
}
