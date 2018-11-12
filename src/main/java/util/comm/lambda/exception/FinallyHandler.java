package util.comm.lambda.exception;

import util.comm.lambda.exception.SimpleExec.DataWrapper;

public interface FinallyHandler{
	public void handle(DataWrapper data);
}
