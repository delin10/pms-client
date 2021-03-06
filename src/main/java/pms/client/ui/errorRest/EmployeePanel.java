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

public class EmployeePanel extends SwitchPanel {
	private static final long serialVersionUID = 5512081542720124924L;
	private final int PANEL_W = 600;
	private final int PANEL_H = 700;
	private JLabel title = new JLabel("瑙掕壊鍒楄〃");
	private JTable table = new JTable();
	private Box root = Box.createVerticalBox();
	private Box top_title_box=Box.createHorizontalBox();
	private ToolBox tool_box=new ToolBox();
	private JScrollPane scroll = new JScrollPane();
	//涓嬭浇浜哄浘鏍�
	private Box button_group=Box.createHorizontalBox();

	public EmployeePanel init() {
		this.setSize(new Dimension(PANEL_W, PANEL_H));
		title.setFont(new Font("瀹嬩綋", Font.BOLD, 20));
		table.setDragEnabled(false);
		scroll.setViewportView(table);
		scroll.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
		table.setPreferredSize(new Dimension(500, 500));
		scroll.setPreferredSize(new Dimension(500, 500));

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
			UIHandler.handle_employees_table(Func.employees(), table);
			JSONTableModel model = (JSONTableModel) table.getModel();
			ActionListener[] listeners = new ActionListener[] { e -> {
				int selected = table.getSelectedRow();
				if (selected != -1) {
						Func.exec(() -> {
							FormModel m = new AbstractCommonFormModel(model.columns());
							m.disable(new String[] {"eid","did"});
							model.getValuesAt(selected).entrySet().forEach(entry->{
								String key=entry.getKey();
								m.setValue(key, entry.getValue());
							});
							String[] params= {"eid"};
							UIHandler.showComplexe2Form(args -> {
								HttpResponse response = (HttpResponse) args[0];
								UIHandler.add_success_callback(response, args1 -> {
									// 需要给表格模型添加内容
									String json = args[1].toString();
									Util.runUi(() -> {
										// 添加进来才能更新
										model.getData().set(selected,json);
										table.updateUI();
										scroll.updateUI();
										this.updateUI();
									});
									return null;
								});
								return null;
							}, "更新住户信息", "comm?action=update&type=employee", m,params);

						});
				}
			}, e -> {
				UIHandler.handle_delete("comm?action=delete&type=employee", table, new String[] {"eid"} ,args->{
					int[]selects=(int[]) args[0];
					Arrays.stream(selects).forEach(model::remove);
					Util.runUi(()->{
						table.updateUI();
						scroll.updateUI();
						this.updateUI();
					});

					return null;
				});
			}, e -> {
				Func.exec(() -> {
					FormModel m = new AbstractCommonFormModel(model.columns());
					m.setType("did", FieldType.TABLE_SELECTOR);
					m.setType("contract_id", FieldType.TABLE_SELECTOR);
					TableSelector selector_contracts= UIHandler.selector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_contracts_table(Func.contracts(), t);
						return null;
					});
					TableSelector selector_departments = UIHandler.selector(args -> {
						JTable t = (JTable) args[0];
						UIHandler.handle_departments_table(Func.departments(), t);
						return null;
					});
					selector_contracts.comfirm(args -> {
						UIHandler.handle_table_selector("contract_id", "contract_id", args);
						return null;
					}, "0");
					selector_departments.comfirm(args -> {
						UIHandler.handle_table_selector("did", "did", args);
						return null;
					}, "0");

					m.setValue("contract_id", selector_contracts);
					m.setValue("did", selector_departments);
					UIHandler.showComplexe2Form(args -> {
						HttpResponse response = (HttpResponse) args[0];
						UIHandler.add_success_callback(response, args1 -> {
							// 需要给表格模型添加内容
							String json = args[1].toString();
							Util.runUi(() -> {
								// 添加进来才能更新
								model.getData().add(json);
								table.updateUI();
								scroll.updateUI();
								this.updateUI();
							});
							return null;
						});
						return null;
					}, "新增楼房", "comm?action=add&type=employee", m,null);

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
		UIHandler.refresh(table, "comm?action=query&type=employee");;
		return null;
	}

}
