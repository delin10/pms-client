package util.ui.swing.model.table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import util.comm.array.ArrayUtil;
import util.ui.swing.bean.KV;
import util.ui.swing.model.table.func.Formatter;

public class JSONTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private KV[] columns;
	private JSONRows data;
	private Formatter formatter;
	private HashSet<Integer> uneditable=new HashSet<>();
    protected JSONTableModel() {}
    public static JSONTableModel create() {
    	return new JSONTableModel();
    }
    
    {
    	init();
    }
    
    public JSONTableModel init() {
    	data=JSONRows.create().init();
    	return this;
    }
    
    public JSONTableModel setColumns(KV[] columns) {
    	this.columns=columns;
    	return this;
    }
    
    public JSONRows getData() {
    	return data;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex].getTitle();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (uneditable.contains(rowIndex)) {
    		return false;
    	}
    	
        return false;
    }

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		String field=columns[col].getField();
		Object value=data.get(row,field);
		if (formatter!=null) {
			value=formatter.format(field, value);
		}
		return value;
	}
	

	public Object getRealValueAt(int row, int col) {
		// TODO Auto-generated method stub
		String field=columns[col].getField();
		Object value=data.get(row,field);
		return value;
	}
	
	
	public int colIndex(String field) {
		return ArrayUtil.indexOf(columns, field,KV::getField);
	}
	public Map<String, Object> getValuesAt(int row){
		Map<String, Object> values=new HashMap<>();
		for (int i=0;i<columns.length;++i) {
			values.put(columns[i].getField(), getRealValueAt(row, i));
		}
		return values;
	}
	
	public void setValueAt(Object value,int row,String field) {
		int col=ArrayUtil.indexOf(columns, field,KV::getField);
		setValueAt(value, row, col);
	}
	
	public void setValueAt(Map<String, String> values,int row) {
		Arrays.stream(columns).map(KV::getField).forEach(col->{
			setValueAt(values.get(col), row, col);
		});
	}
	
	public void setValueAt(String json,int row) {
		data.set(row, json);
	}
	
	public void remove(int row) {
		data.remove(row);
	}
	
	public Formatter getFormatter() {
		return formatter;
	}
	
	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}
	
	public void addUneditable(int col) {
		uneditable.add(col);
	}
	
	public KV[] columns() {
		return columns;
	}
	
}
