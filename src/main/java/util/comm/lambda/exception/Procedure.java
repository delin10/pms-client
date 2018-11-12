package util.comm.lambda.exception;

import util.comm.lambda.exception.SimpleExec.DataWrapper;

public interface Procedure {
	public Object process(DataWrapper data) throws Exception;
}
