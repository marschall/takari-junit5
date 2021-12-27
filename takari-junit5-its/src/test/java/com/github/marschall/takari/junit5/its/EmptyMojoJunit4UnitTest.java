package com.github.marschall.takari.junit5.its;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;

import io.takari.maven.testing.TestMavenRuntime;
import io.takari.maven.testing.TestResources;

public class EmptyMojoJunit4UnitTest {

  @Rule
  public final TestResources resources = new TestResources();

  @Rule
  public final TestMavenRuntime mavenRuntime = new TestMavenRuntime();

  @Test
  public void empty() throws Exception {
    File basedir = this.resources.getBasedir("simple");

    this.mavenRuntime.executeMojo(basedir, "empty");
  }

}
