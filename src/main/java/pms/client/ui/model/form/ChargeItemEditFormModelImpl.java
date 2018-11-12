package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;

public class ChargeItemEditFormModelImpl extends AbstractCommonFormModel {
	public ChargeItemEditFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		this.disable(new String[] { "item_id" });
		this.setValue(values);
	}
}
