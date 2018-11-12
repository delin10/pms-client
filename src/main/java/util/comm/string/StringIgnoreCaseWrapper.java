package util.comm.string;

public class StringIgnoreCaseWrapper {
	private String value;
	
	public static StringIgnoreCaseWrapper create() {
		return new StringIgnoreCaseWrapper();
	}
	
	public StringIgnoreCaseWrapper parse(String value) {
		this.value=value;
		return this;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value.toLowerCase().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return obj==value;
		}else {
			return value.equalsIgnoreCase(obj.toString());
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}
}
