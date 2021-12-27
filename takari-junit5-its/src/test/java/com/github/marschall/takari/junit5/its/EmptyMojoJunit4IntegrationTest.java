package com.github.marschall.takari.junit5.its;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecution;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenInstallations;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.8.3", "3.8.4"})
@MavenInstallations(
        {"target/apache-maven-3.8.2",
          "target/apache-maven-3.8.1"})
public class EmptyMojoJunit4IntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public EmptyMojoJunit4IntegrationTest(MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder
            .withCliOptions("--batch-mode")
            .build();
  }

  @Test
  public void empty() throws Exception {
    File basedir = this.resources.getBasedir("simple");
    MavenExecution execution = this.mavenRuntime.forProject(basedir);

    MavenExecutionResult result = execution.execute("clean", "package");
    result.assertErrorFreeLog();
    result.assertLogText("EmptyMojo ran");
  }

}
