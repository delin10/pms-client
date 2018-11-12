package pms.client.ui.errorRest;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.http.HttpResponse;

import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.ui.ToolBox;
import pms.client.ui.intef.SwitchPanel;
import pms.client.ui.model.form.AbstractCommonFormModel;
import util.ui.swing.bean.KV;
import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.form.FieldType;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;
import util.ui.swing.model.table.TableSelector;

public class RoomPanel extends SwitchPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private final int PANEL_W = 600;
	private final int PANEL_H = 700;
	private JLabel title = new JLabel("房间列表");
	private JTable table = new JTable();
	private Box root = Box.createVerticalBox();
	private Box top_title_box = Box.createHorizontalBox();
	private ToolBox tool_box = new ToolBox();
	private JScrollPane scroll = new JScrollPane();
	private Box button_group = Box.createHorizontalBox();

	public RoomPanel init() {
		this.setSize(new Dimension(PANEL_W, PANEL_H));
		title.setFont(new Font("瀹嬩綋", Font.BOLD, 20));
		table.setDragEnabled(false);
		scroll.setViewportView(table);
		scroll.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
		scroll.setPreferredSize(new Dimension(500, 500));

		Util.setHeaderWidth(table, 150, 35);

		top_title_box.add(Box.createHorizontalStrut(40));
		top_title_box.add(title);

		root.add(top_title_box);
		root.add(button_group);
		root.add(scroll);
		tool_box.init();
		tool_box.setParent(root);
		this.add(root);
		return this;
	}

	public SwitchPanel initTable() {
		Func.exec(() -> {
			UIHandler.handle_rooms_table(Func.rooms(), table);
			JSONTableModel model = (JSONTableModel) table.getModel();

			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
					Func.exec(() -> {
						FormModel m = new AbstractCommonFormModel(model.columns());
						m.setType("is_vacancy", FieldType.COMBOBOX);
						m.setType("decorated", FieldType.COMBOBOX);
						m.disable(new String[] { "building_id", "community_name", "room_id" });
						// m.setType("building_type", FieldType.COMBOBOX);
						KV decorated_first = new KV().setTitle("是").setValue("1").setField("decorated_1");
						KV is_vacancy_first = new KV().setTitle("是").setValue("1").setField("is_vacancy_1");
						model.getValuesAt(selected).entrySet().forEach(entry -> {
							String key = entry.getKey();
							m.setValue(key, entry.getValue());
							if ("decorated".equals(key)) {
								// System.out.println("获取表格中的值"+entry.getValue());
								decorated_first.setAttr("_default_",
										new KV().setField("decorated_" + entry.getValue()));
								System.out.println(decorated_first.getAttr("_default_"));
							} else if ("is_vacancy".equals(key)) {
								KV kv = new KV().setField("is_vacancy_" + entry.getValue());
								is_vacancy_first.setAttr("_default_", kv);
								System.out.println(kv + "里" + decorated_first.getAttr("_default_"));
							}

						});
						m.setValue("decorated", new KV[] { decorated_first,
								new KV().setTitle("否").setValue("0").setField("decorated_0") });
						m.setValue("is_vacancy", new KV[] { is_vacancy_first,
								new KV().setTitle("否").setValue("0").setField("is_vacancy_0") });
						// m.setValue("building_type", new KV[] {new
						// KV().setTitle("三室一厅").setValue("三室一厅").setField("room_type"),new
						// KV().setTitle("").setValue("普通房").setField("building_type")});
						String[] params = { "building_id", "community_name", "room_id" };
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
						}, "新增楼房", "comm?action=update&type=room", m, params);

					});
				}
			}, e -> {
				UIHandler.handle_delete("comm?action=delete&type=room", table,
						new String[] { "building_id", "community_name" ,"room_id"}, args -> {
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
					// m.setValue("building_type", new KV[] {new
					// KV().setTitle("商业房").setValue("商业房").setField("building_type"),new
					// KV().setTitle("普通房").setValue("普通房").setField("building_type")});
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
					selector_communities.comfirm(args -> {
						UIHandler.handle_table_selector("community_name", "name", args);
						return null;
					}, "0");

					selector_building.comfirm(args -> {
						UIHandler.handle_table_selector("building_id", "building_id", args);
						return null;
					}, "0");
					m.setValue("community_name", selector_communities);
					m.setValue("building_id", selector_building);
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
					}, "新增楼房", "comm?action=add&type=room", m, null);

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
		// TODO Auto-generated method stub
		UIHandler.refresh(table, "room?action=query");
		return null;
	}

}
