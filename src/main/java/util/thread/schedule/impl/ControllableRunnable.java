package util.thread.schedule.impl;

import util.thread.schedule.Controllable;

public abstract class ControllableRunnable implements Controllable,Runnable {
	protected boolean stop=false;
	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(){
		this.stop=true;
	}

}
