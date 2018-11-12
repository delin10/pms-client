package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;

public class ContractEditFormModelImpl extends AbstractCommonFormModel {

	public ContractEditFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		this.disable(new String[] {"contract_id"});
		super.setValue(values);
	}

}
