package pms.client.ui.model.form;

import java.util.LinkedList;
import java.util.Map;

import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;

public class RoleEditFormModelImpl extends AbstractCommonFormModel {

	public RoleEditFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		setValue(values);
		this.setType("available", FieldType.COMBOBOX);
		LinkedList<KV> avails = new LinkedList<>();
		KV avail=new KV().setField("available_avail").setTitle("可用").setValue("1");
		KV not_avail=new KV().setField("available_not_avail").setTitle("不可用").setValue("0");
		avail.setAttr("_default_", values.get("available").equals("0")?not_avail:avail);
		avails.add(avail);
		avails.add(not_avail);
		System.out.println(values);
		setValue("available", avails.toArray(new KV[0]));
	}

}
