package com.attask.jenkins;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.*;

/**
 * User: joeljohnson
 * Date: 2/22/12
 * Time: 1:35 PM
 */
public class TestResultAction implements Action {
	private Multimap<String, TestResult> testResults;
	private final AbstractBuild<?, ?> build;

	public TestResultAction(Collection<TestResult> testResults, AbstractBuild<?, ?> build) {
		this.build = build;
		this.testResults = ArrayListMultimap.create();
		for (TestResult testResult : testResults) {
			this.testResults.put(testResult.getThreadName(), testResult);
		}
	}

	public Multimap<String, TestResult> getTestResults() {
		return testResults;
	}

	public void doResultFile(StaplerRequest request, StaplerResponse response) throws IOException {
		String id = request.getParameter("threadID");
		
		response.setContentType("text/plain");
		Writer stream = response.getCompressedWriter(request);

		stream.write("AtTask Failures v2\n");
		for (TestResult testResult : testResults.get(id)) {
			stream.write("failed " + testResult.toString() + "\n");
			if(testResult.getException() != null) {
				stream.write(testResult.getException());
			} else {
				stream.write("I actually passed but I'm being included as a failure for the sake of being run again\n\n");
			}
		}
		stream.flush();
	}
	
	public String getLinkForThread(String thread) {
		StringBuilder sb = new StringBuilder();
		sb.append("AtTask Failures v2").append("\n");
		for (TestResult testResult : testResults.get(thread)) {
			sb.append("added ").append(testResult.toString()).append("\n");
		}

		try {
			return "data:text/csv," + URLEncoder.encode(sb.toString(), "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); //this shouldn't happen.
		}
	}

	public String getIconFileName() {
		return "search.png";
	}

	public String getDisplayName() {
		return "Test Analyser";
	}

	public String getUrlName() {
		return "testAnalyser";
	}

	public AbstractBuild<?, ?> getBuild() {
		return build;
	}
}
