package util.ui.swing.event.focus;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusAdapter implements FocusListener {
	private FocusFunc gained;
	private FocusFunc lost;
	
	public static FocusAdapter create() {
		return new FocusAdapter();
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if (gained!=null) {
			gained.handle(e);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if (lost!=null) {
			lost.handle(e);
		}
	}

	public FocusFunc getLost() {
		return lost;
	}

	public FocusAdapter setLost(FocusFunc lost) {
		this.lost = lost;
		return this;
	}

	public FocusFunc getGained() {
		return gained;
	}

	public FocusAdapter setGained(FocusFunc gained) {
		this.gained = gained;
		return this;
	}

}
