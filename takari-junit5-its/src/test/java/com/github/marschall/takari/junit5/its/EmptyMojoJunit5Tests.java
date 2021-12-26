package com.github.marschall.takari.junit5.its;

import org.junit.jupiter.api.Test;

import com.github.marschall.takari.junit5.MavenPluginTest;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;

@MavenPluginTest
@MavenVersions({"3.8.3", "3.8.4"})
class EmptyMojoJunit5Tests {

  private final MavenRuntime mavenRuntime;

  EmptyMojoJunit5Tests(MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder
            .withCliOptions("--batch-mode")
            .build();
  }

  @Test
  void empty() throws Exception {
    // FIXME
  }

}
