package util.comm.lambda;


public class AntiFinalVar {
	private Object value;
	
	public static AntiFinalVar create() {
		return new AntiFinalVar();
	}
	
	public AntiFinalVar parse(Object value) {
		this.value=value;
		return this;
	}
	
	public String getString() {
		return value.toString();
	}
	
	public int getInt() {
		return (int)value;
	}
	
	public long getLong() {
		return (long)value;
	}
	
	public boolean getBoolean() {
		return (boolean)value;
	}
	
	public Object getObject() {
		return value;
	}
}
