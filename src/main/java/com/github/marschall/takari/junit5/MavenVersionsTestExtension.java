package com.github.marschall.takari.junit5;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.runners.model.InitializationError;

import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;

/**
 * Provides {@link MavenRuntimeBuilder} objects based on {@link MavenVersions}.
 */
public final class MavenVersionsTestExtension implements TestTemplateInvocationContextProvider {

  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    if (MavenHomeUtils.isForced()) {
      return false;
    }
    //@formatter:off
    return context.getTestClass()
                  .map(clazz -> clazz.isAnnotationPresent(MavenVersions.class))
                  .orElse(false);
    //@formatter:on
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
    String displayName = context.getDisplayName();
    String[] versions = context.getTestClass().get().getAnnotation(MavenVersions.class).value();
    List<TestTemplateInvocationContext> contexts = new ArrayList<>();
    List<Throwable> errors = new ArrayList<>();

    try {
      new MavenVersionResolver() {
        @Override
        protected void resolved(File mavenHome, String version) throws InitializationError {
          contexts.add(new MavenVersionTestInvocationContext(version, mavenHome, new MavenVersionDisplayNameFormatter(displayName)));
        }

        @Override
        protected void error(String version, Exception cause) {
          errors.add(new Exception("Could not resolve maven version " + version, cause));
        }
      }.resolve(versions);
    } catch (Exception e) {
      throw new ExtensionConfigurationException("Could not resolve maven versions", e);
    }
    if (!errors.isEmpty()) {
      ExtensionConfigurationException extensionConfigurationException = new ExtensionConfigurationException("Could not resolve maven versions");
      for (Throwable error : errors) {
        extensionConfigurationException.addSuppressed(error);
      }
      throw extensionConfigurationException;
    }
    return contexts.stream();
  }

}
