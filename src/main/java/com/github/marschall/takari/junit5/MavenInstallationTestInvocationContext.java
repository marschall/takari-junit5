package com.github.marschall.takari.junit5;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

final class MavenInstallationTestInvocationContext implements TestTemplateInvocationContext {

  private final MavenInstallationDisplayNameFormatter formatter;
  private final String installation;

  MavenInstallationTestInvocationContext(String installation, MavenInstallationDisplayNameFormatter formatter) {
    this.installation = installation;
    this.formatter = formatter;
  }

  @Override
  public String getDisplayName(int invocationIndex) {
    return this.formatter.format(this.installation);
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    File mavenHome;
    try {
      mavenHome = new File(this.installation).getCanonicalFile();
    } catch (IOException e) {
      throw new ExtensionConfigurationException("could not access maven installation: " + this.installation, e);
    }
    if (mavenHome.isDirectory()) {
      return Collections.singletonList(new MavenRuntimeBuilderParameterResolver(mavenHome));
    } else {
      throw new ExtensionConfigurationException("Invalid maven installation location " + installation);
    }
  }

}
