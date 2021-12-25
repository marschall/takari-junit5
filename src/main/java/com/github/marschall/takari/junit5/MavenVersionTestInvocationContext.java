package com.github.marschall.takari.junit5;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

final class MavenVersionTestInvocationContext implements TestTemplateInvocationContext {

  private final String mavenVersion;
  private final File mavenHome;
  private final MavenVersionDisplayNameFormatter formatter;

  MavenVersionTestInvocationContext(String mavenVersion, File mavenHome, MavenVersionDisplayNameFormatter formatter) {
    this.mavenVersion = mavenVersion;
    this.mavenHome = mavenHome;
    this.formatter = formatter;
  }

  @Override
  public String getDisplayName(int invocationIndex) {
    return this.formatter.format(this.mavenVersion);
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    return Collections.singletonList(new MavenRuntimeBuilderParameterResolver(this.mavenHome));
  }

}
