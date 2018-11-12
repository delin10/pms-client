package pms.client.ui.errorRest;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;

import pms.client.data.RuntimeStore;
import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.ChargeFormRequestImpl;
import pms.client.ui.BasicMainPanel;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.form.AbstractCommonFormModel;
import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;
import util.ui.swing.model.table.TableSelector;

public class ChargeFormPanel extends BasicMainPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private static ChargeFormRequestImpl req=new ChargeFormRequestImpl();
	public BasicMainPanel initTable() {
		Func.exec(() -> {
			UIHandler.handle_chargeforms_table(Func.charge_record(), table);
			JSONTableModel model = (JSONTableModel) table.getModel();
			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
					Func.exec(() -> {
						FormModel m = new AbstractCommonFormModel(model.columns());
						m.disable(new String[] { "item_id" ,"community_name","building_id","room_id"});
						model.getValuesAt(selected).entrySet().forEach(entry -> {
							String key = entry.getKey();
							m.setValue(key, entry.getValue());
						});
						String[] params = { "item_id" };
						UIHandler.showComplexe2Form(args -> {
							HttpResponse response = (HttpResponse) args[0];
							UIHandler.add_success_callback(response, args1 -> {
								// 需要给表格模型添加内容
								String json = args[1].toString();
								Util.runUi(() -> {
									// 添加进来才能更新
									model.getData().set(selected, json);
									table.updateUI();
									scroll.updateUI();
									this.updateUI();
								});
								return null;
							});
							return null;
						}, "更新住户信息", "comm?action=update&type="+id, m, params);

					});
				}
			}, e -> {
				UIHandler.handle_delete("comm?action=delete&type="+id, table, new String[] { "item_id" }, args -> {
					int[] selects = (int[]) args[0];
					Arrays.stream(selects).forEach(model::remove);
					Util.runUi(() -> {
						table.updateUI();
						scroll.updateUI();
						this.updateUI();
					});

					return null;
				});
			}, e -> {
				Func.exec(() -> {
					FormModel m = new AbstractCommonFormModel(model.columns());
					m.setType("community_name", FieldType.TABLE_SELECTOR);
					m.setType("building_id", FieldType.TABLE_SELECTOR);
					m.setType("room_id", FieldType.TABLE_SELECTOR);
					m.setType("item_id", FieldType.TABLE_SELECTOR);
					TableSelector selector_communities= UIHandler.selector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_communities_table(Func.communities(), t);
						return null;
					});
					TableSelector selector_building = UIHandler.selector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_buildings_table(Func.buildings(), t);
						return null;
					});
					
					TableSelector selector_room = UIHandler.selector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_rooms_table(Func.rooms(), t);
						return null;
					});
					TableSelector selector_items = UIHandler.selector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_chargeitems_table(Func.charge_items(), t);
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
					selector_items.comfirm(args -> {
						UIHandler.handle_table_selector("item_id", "item_id", args);
						return null;
					}, "0");
					m.setValue("community_name", selector_communities);
					m.setValue("building_id", selector_building);
					m.setValue("room_id", selector_room);
					m.setValue("item_id", selector_items);
					UIHandler.showComplexe2Form(args -> {
						HttpResponse response = (HttpResponse) args[0];
						UIHandler.add_success_callback(response, args1 -> {
							// 需要给表格模型添加内容
							String json = args[1].toString();
							Util.runUi(() -> {
								// 添加进来才能更新
								model.getData().add(json);
								System.out.println(json);
								table.updateUI();
								scroll.updateUI();
								this.updateUI();
							});
							return null;
						});
						return null;
					}, "新增楼房", "comm?action=add&type="+id, m, null);

				});
			} };
			table.setComponentPopupMenu(CompGenerator.genPopupMenu(null, null, listeners, "编辑", "删除", "新增"));
		});
		return this;
	}

	@Override
	public SwitchPanel switchIn(JComponent container, Runnable run) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Util.runUi(() -> {
			container.removeAll();
			container.add(this);
			// table.updateUI();
			// scroll.updateUI();
			// root.updateUI();
			this.updateUI();
			container.updateUI();
			if (run != null) {
				run.run();
			}
		});
		return this;
	}


	@Override
	public SwitchPanel refresh() {
		RuntimeStore.runUI().refresh(req.query(), table.getModel());
		return null;
	}


	@Override
	protected String getTitle() {
		// TODO Auto-generated method stub
		return "收款记录";
	}

}
