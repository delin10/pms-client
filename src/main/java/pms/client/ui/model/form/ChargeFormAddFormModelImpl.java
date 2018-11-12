package pms.client.ui.model.form;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import pms.client.data.RuntimeStore;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.ChargeItemRequestImpl;
import pms.client.handler.request.impl.RoomRequestImpl;
import pms.client.ui.model.ChargeItemTableModelImpl;
import pms.client.ui.model.RoomTableModelImpl;
import util.ui.swing.bean.KV;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.table.TableSelector;

public class ChargeFormAddFormModelImpl extends AbstractCommonFormModel {
	public ChargeFormAddFormModelImpl(KV[] kvs) {
		super(kvs);
	}

	@Override
	public void init(Map<String, Object> values) {
		disable(new String[] { "item_id" ,"community_name","building_id","room_id"});
		this.setType("room_id", FieldType.TABLE_SELECTOR);
		this.setType("item_id", FieldType.TABLE_SELECTOR);
		TableSelector selector_room = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new RoomTableModelImpl());
			RuntimeStore.runUI().refresh(new RoomRequestImpl().query(), t.getModel());;
			return null;
		});
		TableSelector selector_items = UIHandler.selector(args -> {
			JTable t = (JTable) args[0];
			t.setModel(new ChargeItemTableModelImpl());
			RuntimeStore.runUI().refresh(new ChargeItemRequestImpl().query(), t.getModel());;
			return null;
		});
		
		selector_room.comfirm(args -> {
			Map<String, String> mapper=new HashMap<>();
			mapper.put("community_name", "community_name");
			mapper.put("building_id", "building_id");
			mapper.put("room_id", "room_id");
			RuntimeStore.runUI().handle_table_selector(mapper, args);
			return null;
		}, "0");
		selector_items.comfirm(args -> {
			Map<String, String> mapper=new HashMap<>();
			mapper.put("item_id", "item_id");
			RuntimeStore.runUI().handle_table_selector(mapper, args);
			return null;
		}, "0");
		this.setValue("room_id", selector_room);
		this.setValue("item_id", selector_items);
	}
}
