package com.github.marschall.takari.junit5.its;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.github.marschall.takari.junit5.TestMavenRuntime;
import com.github.marschall.takari.junit5.TestResources;


class EmptyMojoJunit5UnitTests {

  @RegisterExtension
  final TestResources resources = new TestResources();

  @RegisterExtension
  final TestMavenRuntime mavenRuntime = new TestMavenRuntime();

  @Test
  void empty() throws Exception {
    File basedir = this.resources.getBasedir("simple");

    this.mavenRuntime.executeMojo(basedir, "empty");
  }

}
