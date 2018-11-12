package util.comm;

/**
 * ��Ϊ������Ϣ�ù���
 * @author delin
 *status ��ʾ�����Ƿ�ɹ�,ͨ��-1��ʾʧ��
 *info ���������Ľ����Ϣ
 *data Я������
 */
public class Info {
	private int status=-1;
	private String info;
	private Object data;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void fail(String info) {
		this.info=info;
		this.status=-1;
	}
	
	public void suc(String info) {
		this.info=info;
		this.status=0;
	}
	
	public void wrapInfo(Info info) {
		this.status=info.getStatus();
		this.info=info.getInfo();
		this.data=info.getData();
	}
	
	public Info line() {
		this.info+="</br>";
		return this;
	}
	
	public Info append(String info) {
		this.info+=info;
		return this;
	}
}
