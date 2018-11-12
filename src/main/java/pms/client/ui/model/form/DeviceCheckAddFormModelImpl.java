package pms.client.ui.model.form;

import java.util.LinkedList;
import java.util.Map;

import util.ui.swing.bean.KV;

public class DeviceCheckAddFormModelImpl extends AbstractCommonFormModel {

	public DeviceCheckAddFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		LinkedList<KV> avails = new LinkedList<>();
		avails.add(new KV().setField("available").setTitle("可用").setValue("1"));
		avails.add(new KV().setField("available").setTitle("不可用").setValue("0"));
		this.setValue("available", avails.toArray(new KV[0]));
	}

}
