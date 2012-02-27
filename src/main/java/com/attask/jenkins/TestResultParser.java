package com.attask.jenkins;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * User: joeljohnson
 * Date: 2/22/12
 * Time: 1:22 PM
 */
public class TestResultParser {
	private final File root;
	public TestResultParser(File root) {
		this.root = root;
	}
	
	public Collection<TestResult> parseResults(Collection<File> files) throws FileNotFoundException {
		List<TestResult> testResults = new ArrayList<TestResult>();
		for (File file : files) {
			testResults.addAll(getTestResults(new File(root, file.getPath())));
		}
		return testResults;
	}

	public Collection<? extends TestResult> getTestResults(File file) throws FileNotFoundException {
		if(file == null || !file.exists() || file.isDirectory()) {
			return Collections.emptyList();
		}
		List<TestResult> testResults = new ArrayList<TestResult>();
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			TestResult result;
			if("finished".equals(parts[0]) || "skipped".equals(parts[0])) {
				String fullTestName = parts[1];
				String threadName = parts[2];
				String time = parts[3];
				result = new TestResult(fullTestName, threadName, time);
			} else if("failed".equals(parts[0])) {
				String fullTestName = parts[1];
				String threadName = parts[2];
				String time = parts[3];
				StringBuilder exception = new StringBuilder();
				while(!line.trim().isEmpty() && scanner.hasNextLine()) {
					line = scanner.nextLine();
					exception.append(line).append("\n");
				}
				result = new TestResult(fullTestName, threadName, time, exception.toString());
			} else {
				continue;
			}
			testResults.add(result);
		}
		return testResults;
	}
}
