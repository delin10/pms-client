package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;

public class CommunityEditFormModelImpl extends AbstractCommonFormModel {
	public CommunityEditFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		setValue(values);
	}

}
