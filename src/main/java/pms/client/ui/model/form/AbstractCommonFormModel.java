package pms.client.ui.model.form;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.form.model.impl.AbstractFormModel;

public abstract class AbstractCommonFormModel extends AbstractFormModel{
	private Map<String, KV> kvs=new HashMap<>();
	private Map<String, Boolean> disables=new HashMap<>();
	private Map<String, FieldType> mapper=new HashMap<>();
	public AbstractCommonFormModel(KV[]kvs) {
		// TODO Auto-generated constructor stub
		Arrays.stream(kvs).forEach(kv->this.kvs.put(kv.getField(), kv.copy()));
	}
	@Override
	public String getLabel(String field) {
		// TODO Auto-generated method stub
		return kvs.get(field).getTitle();
	}

	@Override
	public Object getValue(String field) {
		// TODO Auto-generated method stub
		return kvs.get(field).getValue();
	}

	@Override
	public Dimension inputSize(String field) {
		// TODO Auto-generated method stub
		return new Dimension(250, 30);
	}

	@Override
	public Dimension labelSize(String field) {
		// TODO Auto-generated method stub
		int width=kvs.values().stream().map(kv->kv.getTitle().length()*15).max((a,b)->a-b).get();
		return new Dimension(width, 30);
	}

	@Override
	public String[] fields() {
		// TODO Auto-generated method stub
		return kvs.keySet().toArray(new String[0]);
	}
	
	@Override
	public boolean disable(String field) {
		Boolean disable=disables.get(field);
		return disable==null?false:disable;
	}
	
	public void disable(String[]fields) {
		Arrays.stream(fields).forEach(field->disables.put(field, true));
	}

	public AbstractCommonFormModel setAttr(String field,String value) {
		KV kv=kvs.get(field);
		if (kv!=null) {
			kv.setValue(value);
		}
		return this;
	}
	@Override
	public Dimension formSize() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public FieldType type(String field) {
		// TODO Auto-generated method stub
		FieldType type=mapper.get(field);;
		return type==null?FieldType.DEFAULT_TEXT_FIELD:type;
	}
	
	@Override
	public void setType(String field,FieldType type) {
		// TODO Auto-generated method stub
		mapper.put(field, type);
	}
	@Override
	public void setValue(String field, Object value) {
		// TODO Auto-generated method stub
		KV kv=kvs.get(field);
		if (kv!=null) {
			kv.setValue(value);
		}
	}
	
	public void setValue(Map<String, Object> value) {
		value.entrySet().stream().forEach(entry->{
			setValue(entry.getKey(),entry.getValue());
		});
	}
	
	public abstract void init(Map<String, Object> values);
}
