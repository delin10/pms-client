package util.ui.swing.model.impl;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import util.ui.swing.model.IMapperDataModel;

public abstract class AbstractComboBoxMapperDataModel implements IMapperDataModel, ComboBoxModel<Object> {

	@Override
	public void addListDataListener(ListDataListener l) {}

	@Override
	public void removeListDataListener(ListDataListener l) {}

}
