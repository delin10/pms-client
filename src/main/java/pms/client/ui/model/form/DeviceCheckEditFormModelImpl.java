package pms.client.ui.model.form;

import java.util.LinkedList;
import java.util.Map;

import util.ui.swing.bean.KV;

public class DeviceCheckEditFormModelImpl extends AbstractCommonFormModel {

	public DeviceCheckEditFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		LinkedList<KV> avails = new LinkedList<>();
		avails.add(new KV().setField("available").setTitle("可用").setValue("1"));
		avails.add(new KV().setField("available").setTitle("不可用").setValue("0"));
		// System.out.println(list.size());
		int index = avails.indexOf(values.get("available"));
		if (index >= 0) {
			KV kv = avails.remove(index);
			avails.addFirst(kv);
		}
		this.setValue("available", avails.toArray(new KV[0]));
	}

}
