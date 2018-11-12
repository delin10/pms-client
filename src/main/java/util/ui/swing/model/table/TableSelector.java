package util.ui.swing.model.table;

import javax.swing.JPanel;

import util.comm.lambda.functon.SimpleTask;

public abstract class TableSelector extends JPanel{
	private static final long serialVersionUID = 1L;

	public abstract TableSelector comfirm(SimpleTask task,Object...args);
	public abstract TableSelector update();
}
