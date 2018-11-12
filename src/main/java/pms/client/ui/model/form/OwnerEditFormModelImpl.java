package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;

public class OwnerEditFormModelImpl extends AbstractCommonFormModel {

	public OwnerEditFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.disable(new String[] {"building_id","community_name","room_id","contract_id","floor_id","name","owner_id"});
		this.setValue(values);
	}

}
