package com.attask.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * User: joeljohnson
 * Date: 2/22/12
 * Time: 1:27 PM
 */
public class TestResultRecorder extends Recorder {
	private String filePattern;

	@DataBoundConstructor
	public TestResultRecorder(String filePattern) {
		this.filePattern = filePattern;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
		TestResultParser parser = new TestResultParser(build.getArtifactsDir());
		Collection<TestResult> testResults = parser.parseResults(Arrays.asList(new File(filePattern)));
		build.addAction(new TestResultAction(testResults, build));
		return true;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	@Extension
	public static class ResultsOverrideRecorderDescriptor extends BuildStepDescriptor<Publisher> {
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Enable Test Result Analyser";
		}
	}
}
