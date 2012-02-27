package com.attask.jenkins;

/**
 * User: joeljohnson
 * Date: 2/22/12
 * Time: 1:14 PM
 */
public class TestResult {
	private final String packageName;
	private final String className;
	private final String methodName;
	private final String threadName;
	private final String exception;
	private final String time;

	public TestResult(String fullTestName, String threadName, String time) {
		this(fullTestName, threadName, time, null);
	}

	public TestResult(String fullTestName, String threadName, String time, String exception) {
		int lastDot = fullTestName.lastIndexOf(".");
		this.packageName = fullTestName.substring(0, lastDot);

		String[] secondPart = fullTestName.substring(lastDot+1).split("#");
		this.className = secondPart[0];
		this.methodName = secondPart[1];

		this.threadName = threadName;
		this.exception = exception;
		this.time = time;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getThreadName() {
		return threadName;
	}

	public String getException() {
		return exception;
	}

	public boolean getIsFailure() {
		return exception != null;
	}

	public boolean getWasSkipped() {
		return "0".equals(time);
	}

	@Override
	public String toString() {
		return packageName + "." + className + "#" + methodName;
	}

	public String getTime() {
		return time;
	}
}
