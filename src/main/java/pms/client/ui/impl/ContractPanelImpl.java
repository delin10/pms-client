package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.ContractRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.ContractTableModelImpl;
import pms.client.ui.model.form.BuildingAddFormModelImpl;
import pms.client.ui.model.form.BuildingEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class ContractPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static ContractRequestImpl req = new ContractRequestImpl();
	private static String[] keys= {"contract_id"};

	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}

	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "合同列表";
	}

	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}

	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增合同";
	}

	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑合同信息";
	}

	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new BuildingAddFormModelImpl(model.columns());
	}

	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new BuildingEditFormModelImpl(model.columns());
	}

	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}

	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new ContractTableModelImpl();
	}
}