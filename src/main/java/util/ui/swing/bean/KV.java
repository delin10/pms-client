package util.ui.swing.bean;

import java.util.HashMap;
import java.util.Map;

public class KV {
	private String field;
	private String title;
	private Object value;
	private Map<String, Object> attrs = new HashMap<>();

	public KV() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public KV setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getField() {
		return field;
	}

	public KV setField(String field) {
		this.field = field;
		return this;
	}

	public String toString() {
		return title;
	}

	public Object getValue() {
		return value;
	}

	public KV setValue(Object value) {
		this.value = value;
		return this;
	}

	public KV setAttr(String name, Object value) {
		attrs.put(name, value);
		return this;
	}

	public Object getAttr(String name) {
		return attrs.get(name);
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public KV copy() {
		// System.out.println("copy:"+field+";size:"+attrs.size());
		KV copy = new KV().setTitle(title).setField(field).setValue(value);
		copy.attrs.putAll(attrs);
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof String) {
			return field.equals(obj.toString());
		} else {
			return field.equals(((KV) obj).field);

		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return field.hashCode();
	}
}
