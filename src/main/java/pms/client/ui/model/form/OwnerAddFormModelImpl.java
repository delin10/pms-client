package pms.client.ui.model.form;

import java.util.Map;

import javax.swing.JTable;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.BuildingRequestImpl;
import pms.client.handler.request.impl.CommunityRequestImpl;
import pms.client.handler.request.impl.RoomRequestImpl;
import pms.client.ui.model.BuildingTableModelImpl;
import pms.client.ui.model.CommunitiesTableModelImpl;
import pms.client.ui.model.RoomTableModelImpl;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class OwnerAddFormModelImpl extends AbstractCommonFormModel {

	public OwnerAddFormModelImpl(KV[] kvs) {
		super(kvs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Map<String, Object> values) {
		// TODO Auto-generated method stub
		this.setType("community_name", FieldType.TABLE_SELECTOR);
		this.setType("building_id", FieldType.TABLE_SELECTOR);
		this.setType("room_id", FieldType.TABLE_SELECTOR);
		TableSelector selector_communities= UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new CommunitiesTableModelImpl());
			RuntimeStore.runUI().handle_table(new CommunityRequestImpl().query(), t, false);
			return null;
		});
		TableSelector selector_building = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new BuildingTableModelImpl());
			RuntimeStore.runUI().handle_table(new BuildingRequestImpl().query(), t, false);
			return null;
		});
		
		TableSelector selector_room = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new RoomTableModelImpl());
			RuntimeStore.runUI().handle_table(new RoomRequestImpl().query(), t, false);
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
		
		selector_room.comfirm(args -> {
			UIHandler.handle_table_selector("room_id", "room_id", args);
			return null;
		}, "0");
		this.setValue("community_name", selector_communities);
		this.setValue("building_id", selector_building);
		this.setValue("room_id", selector_room);
	}

}
