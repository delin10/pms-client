package pms.client.ui.model.form;

import java.util.Map;

import javax.swing.JTable;

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class DeviceAddFormModelImpl extends AbstractCommonFormModel {

	public DeviceAddFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.setType("community_name", FieldType.TABLE_SELECTOR);
		this.setType("building_id", FieldType.TABLE_SELECTOR);
		this.setType("install_man", FieldType.TABLE_SELECTOR);

		TableSelector selector_communities = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			UIHandler.handle_communities_table(Func.communities(), t);
			return null;
		});
		TableSelector selector_building = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			UIHandler.handle_buildings_table(Func.buildings(), t);
			return null;
		});
		TableSelector selector_emmployees = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			UIHandler.handle_employees_table(Func.employees(), t);
			return null;
		});

		selector_communities.comfirm(args -> {
			UIHandler.handle_table_selector("community_name", "name", args);
			return null;
		}, "0");
		
		selector_building.comfirm(args -> {
			UIHandler.handle_table_selector("building_id", "building_id", args);
			return null;
		}, "0");
		selector_emmployees.comfirm(args -> {
			UIHandler.handle_table_selector("install_man", "eid", args);
			return null;
		}, "0");

		this.setValue("community_name", selector_communities);
		this.setValue("building_id", selector_building);
		this.setValue("install_man", selector_emmployees);
	}

}
