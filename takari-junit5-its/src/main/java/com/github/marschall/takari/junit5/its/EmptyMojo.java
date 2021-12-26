package com.github.marschall.takari.junit5.its;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
@Mojo(
        name = "empty",
        defaultPhase = PACKAGE,
        requiresProject = true,
        threadSafe = true)
public class EmptyMojo extends AbstractMojo {

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    this.getLog().info("EmptyMojo ran");
  }

}
