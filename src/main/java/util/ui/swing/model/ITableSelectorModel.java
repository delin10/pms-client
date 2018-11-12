package util.ui.swing.model;

import javax.swing.table.TableModel;

import util.comm.lambda.functon.SimpleTask;

public interface ITableSelectorModel  {
	public TableModel getTableModel();
	public SimpleTask initializeTableFunction();
}
