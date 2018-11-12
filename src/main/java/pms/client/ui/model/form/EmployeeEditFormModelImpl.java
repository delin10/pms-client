package pms.client.ui.model.form;

import java.util.Map;

import util.ui.swing.bean.KV;

public class EmployeeEditFormModelImpl extends AbstractCommonFormModel {

	public EmployeeEditFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.disable(new String[] {"eid","did"});
		setValue(values);
	}

}
