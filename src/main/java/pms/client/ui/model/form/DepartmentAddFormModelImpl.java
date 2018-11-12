package pms.client.ui.model.form;

import java.util.Map;

import javax.swing.JTable;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.CommunityRequestImpl;
import pms.client.ui.model.CommunitiesTableModelImpl;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class DepartmentAddFormModelImpl extends AbstractCommonFormModel {

	public DepartmentAddFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.setType("community_name", FieldType.TABLE_SELECTOR);
		this.setType("did", FieldType.TABLE_SELECTOR);
		this.setType("contract_id", FieldType.TABLE_SELECTOR);

		TableSelector selector_communities = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new CommunitiesTableModelImpl());
			RuntimeStore.runUI().refresh(new CommunityRequestImpl().query(), t.getModel());;
			return null;
		});

		selector_communities.comfirm(args -> {
			UIHandler.handle_table_selector("community_name", "name", args);
			return null;
		}, "0");

		this.setValue("community_name", selector_communities);
	}

}
