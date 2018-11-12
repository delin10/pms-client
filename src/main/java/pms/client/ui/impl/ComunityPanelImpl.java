package pms.client.ui.impl;

import javax.swing.table.TableModel;

import pms.client.data.RuntimeStore;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.handler.request.impl.CommunityRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.CommunitiesTableModelImpl;
import pms.client.ui.model.form.CommunityAddFormModelImpl;
import pms.client.ui.model.form.CommunityEditFormModelImpl;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public class ComunityPanelImpl extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static CommunityRequestImpl req=new CommunityRequestImpl();
	private static String[] keys= {"name"};

	@Override
	public SwitchPanel refresh() {
		// TODO Auto-generated method stub
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		update();
		return this;
	}


	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "小区列表";
	}


	@Override
	protected ADQUPatternRequestImpl getRequest() {
		// TODO Auto-generated method stub
		return req;
	}


	@Override
	protected String getAddFormTitle() {
		// TODO Auto-generated method stub
		return "新增小区";
	}


	@Override
	protected String getEditFormTitle() {
		// TODO Auto-generated method stub
		return "编辑小区信息";
	}


	@Override
	protected FormModel getAddFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new CommunityAddFormModelImpl(model.columns());
	}


	@Override
	protected FormModel getEditFormModel(JSONTableModel model) {
		// TODO Auto-generated method stub
		return new CommunityEditFormModelImpl(model.columns());
	}


	@Override
	protected String[] getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}


	@Override
	protected TableModel getJSONTableModel() {
		// TODO Auto-generated method stub
		return new CommunitiesTableModelImpl();
	}

}
