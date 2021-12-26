package com.github.marschall.takari.junit5.its;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.github.marschall.takari.junit5.MavenPluginTest;
import com.github.marschall.takari.junit5.TestResources;

import io.takari.maven.testing.executor.MavenExecution;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;

@Disabled
@MavenPluginTest
@MavenVersions({"3.8.3", "3.8.4"})
class EmptyMojoJunit5Tests {

  @RegisterExtension
  final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  EmptyMojoJunit5Tests(MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder
            .withCliOptions("--batch-mode")
            .build();
  }

  @Test
  void empty() throws Exception {
    File basedir = this.resources.getBasedir("simple");
    MavenExecution execution = this.mavenRuntime.forProject(basedir);

    MavenExecutionResult result = execution.execute("clean", "package");
    result.assertErrorFreeLog();
    result.assertLogText("EmptyMojo ran");
  }

}
