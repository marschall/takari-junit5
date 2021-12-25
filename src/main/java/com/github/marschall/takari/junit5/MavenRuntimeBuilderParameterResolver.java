package com.github.marschall.takari.junit5;

import java.io.File;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;

final class MavenRuntimeBuilderParameterResolver implements ParameterResolver {
  
  private final File mavenHome;

  MavenRuntimeBuilderParameterResolver(File mavenHome) {
    this.mavenHome = mavenHome;
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType() == MavenRuntimeBuilder.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return  MavenRuntime.builder(this.mavenHome, null);
  }

}
