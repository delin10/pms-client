package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;

public class CarAddFormModelImpl extends AbstractCommonFormModel {
	public CarAddFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		disable(new String[] { "item_id" ,"community_name","building_id","room_id"});
	}
}
