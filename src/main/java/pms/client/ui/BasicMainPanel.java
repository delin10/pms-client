package pms.client.ui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.TableModel;

import org.apache.http.HttpResponse;

import pms.client.data.RuntimeStore;
import pms.client.funcs.Func;
import pms.client.funcs.UIHandler;
import pms.client.handler.request.impl.ADQUPatternRequestImpl;
import pms.client.ui.model.form.AbstractCommonFormModel;
import util.comm.lambda.functon.SimpleTask;
import util.ui.swing.comm.Util;
import util.ui.swing.gen.CompGenerator;
import util.ui.swing.model.form.model.FormModel;
import util.ui.swing.model.table.JSONTableModel;

public abstract class BasicMainPanel extends AbstractMainPanel {
	private static final long serialVersionUID = -6471722456830033376L;

	public BasicMainPanel initTable() {
		Func.exec(() -> {
			table.setModel(getJSONTableModel());
			Util.setHeaderWidth(table, 150, 35);
			refresh();
			JSONTableModel model = (JSONTableModel) table.getModel();
			ArrayList<ActionListener> listeners = new ArrayList<>();
			ArrayList<String> items = new ArrayList<>();
			if (enableAdd()) {
				items.add(getPopupAddItemTitle());
				listeners.add(e -> {
					add(model);
				});
			}
			if (enableDelete()) {
				items.add(getPopupDeleteItemTitle());
				listeners.add(e -> {
					delete(model);
				});
			}
			if (enableEdit()) {
				items.add(getPopupEditItemTitle());
				listeners.add(e -> {
					update(model);
				});
			}

			table.setComponentPopupMenu(CompGenerator.genPopupMenu(null, null, listeners.toArray(new ActionListener[0]),
					items.toArray(new String[0])));
		});
		return this;

	}

	public boolean enableAdd() {
		return true;
	}

	public boolean enableEdit() {
		return true;
	}

	public boolean enableDelete() {
		return true;
	}

	public void add(JSONTableModel model) {
		SimpleTask callback = args -> {
			HttpResponse response = (HttpResponse) args[0];
			UIHandler.add_success_callback(response, args1 -> {
				// 需要给表格模型添加内容
				String json = args[1].toString();
				Util.runUi(() -> {
					// 添加进来才能更新
					model.getData().add(json);
					update();
					this.updateUI();
				});
				return null;
			});
			return null;
		};

		SimpleTask submit = args1 -> {
			System.out.println(args1[0]);
			return getRequest().add(args1[0]);
		};
		RuntimeStore.exec(() -> {
			AbstractCommonFormModel m = (AbstractCommonFormModel) getAddFormModel(model);
			m.init(null);
			RuntimeStore.runUI().showComplexe2Form(callback, getAddFormTitle(), submit, m);

		});
	}

	public void update(JSONTableModel model) {
		int selected = table.getSelectedRow();
		if (selected != -1) {
			SimpleTask callback = args -> {
				HttpResponse response = (HttpResponse) args[0];
				UIHandler.add_success_callback(response, args1 -> {
					// 需要给表格模型添加内容
					String json = args[1].toString();
					Util.runUi(() -> {
						// 添加进来才能更新
						model.getData().set(selected, json);
						System.out.println(json);
						update();
						this.updateUI();
					});
					return null;
				});
				return null;
			};
			SimpleTask submit = args -> {
				System.out.println("在submit中++" + args[0]);
				return getRequest().update(args[0]);
			};
			RuntimeStore.exec(() -> {
				AbstractCommonFormModel m = (AbstractCommonFormModel) getEditFormModel(model);
				m.init(model.getValuesAt(table.getSelectedRow()));
				RuntimeStore.runUI().showComplexe2Form(callback, getEditFormTitle(), submit, m);
			});
		}
	}

	public void delete(JSONTableModel model) {
		int[] select_rows = table.getSelectedRows();
		RuntimeStore.exec(() -> {

			ArrayList<Map<String, Object>> selects = RuntimeStore.runUI().select_keys_values(model, select_rows,
					getKeys());
			selects.stream().forEach(System.out::println);
			RuntimeStore.runUI().handle_delete_reponse(getRequest().delete(selects), model, select_rows);
			update();
		});
	}
	
	protected String getPopupAddItemTitle() {
		return "新增";
	}
	
	
	protected String getPopupDeleteItemTitle() {
		return "删除";
	}
	
	
	protected String getPopupEditItemTitle() {
		return "编辑";
	}

	/**
	 * @return title is the title of table
	 */
	protected abstract String getTitle();

	/**
	 * @return 改面板的请求对象
	 */
	protected abstract ADQUPatternRequestImpl getRequest();

	protected abstract String getAddFormTitle();

	protected abstract String getEditFormTitle();

	protected abstract FormModel getAddFormModel(JSONTableModel model);

	protected abstract FormModel getEditFormModel(JSONTableModel model);

	/**
	 * @return 获取删除需要的域
	 */
	protected abstract String[] getKeys();

	protected abstract TableModel getJSONTableModel();

}
