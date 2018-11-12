package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.ChargeFormRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.ChargeFormTableModelImpl;
import pms.client.ui.model.form.ChargeFormAddFormModelImpl;
import pms.client.ui.model.form.ChargeFormEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class ChargeFormPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static ChargeFormRequestImpl req=new ChargeFormRequestImpl();
	private static String[] keys= {"form_id"};

	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}


	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "收款记录";
	}


	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}


	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增收款单信息";
	}


	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑收款单信息";
	}


	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new ChargeFormAddFormModelImpl(model.columns());
	}


	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new ChargeFormEditFormModelImpl(model.columns());
	}


	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}


	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new ChargeFormTableModelImpl();
	}

}
