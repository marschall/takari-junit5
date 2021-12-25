package com.github.marschall.takari.junit5;

import java.io.File;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.takari.maven.testing.executor.MavenInstallationUtils;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;

/**
 * Provides a {@link MavenRuntimeBuilder} based on {@code -Dmaven.home}
 * and optionally {@code -Dclassworlds.conf}.
 */
public final class ForcedMavenRuntimeBuilderParameterResolver implements ParameterResolver {

  public ForcedMavenRuntimeBuilderParameterResolver() {
    super();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType() == MavenRuntimeBuilder.class
        && MavenHomeUtils.isForced();
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    File forcedMavenHome = MavenInstallationUtils.getForcedMavenHome();
    File forcedClassworldsConf = MavenInstallationUtils.getForcedClassworldsConf();

    if (forcedMavenHome != null) {
      if (forcedMavenHome.isDirectory() || (forcedClassworldsConf != null && forcedClassworldsConf.isFile())) {
        return MavenRuntime.builder(forcedMavenHome, forcedClassworldsConf);
      }
    }
    throw new ParameterResolutionException("Invalid -Dmaven.home=" + forcedMavenHome.getAbsolutePath());
  }

}
