package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.ChargeItemRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.ChargeItemTableModelImpl;
import pms.client.ui.model.form.ChargeItemAddFormModelImpl;
import pms.client.ui.model.form.ChargeItemEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class ChargeItemPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static ChargeItemRequestImpl req=new ChargeItemRequestImpl();
	private static String[] keys= {"item_id"};

	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}


	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "收费项目列表";
	}


	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}


	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增收费项目";
	}


	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑收费项目信息";
	}


	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new ChargeItemAddFormModelImpl(model.columns());
	}


	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new ChargeItemEditFormModelImpl(model.columns());
	}


	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}


	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new ChargeItemTableModelImpl();
	}

}
