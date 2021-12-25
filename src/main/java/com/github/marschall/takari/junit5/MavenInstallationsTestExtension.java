package com.github.marschall.takari.junit5;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import io.takari.maven.testing.executor.MavenInstallations;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;

/**
 * Provides {@link MavenRuntimeBuilder} objects based on {@link MavenInstallations}.
 */
public final class MavenInstallationsTestExtension implements TestTemplateInvocationContextProvider {

  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    if (MavenHomeUtils.isForced()) {
      return false;
    }
    //@formatter:off
    return context.getTestClass()
                  .map(clazz -> clazz.isAnnotationPresent(MavenInstallations.class))
                  .orElse(false);
    //@formatter:on
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
    String displayName = context.getDisplayName();
    String[] installations = context.getTestClass().get().getAnnotation(MavenInstallations.class).value();
    return Arrays.stream(installations)
        .map(installation -> new MavenInstallationTestInvocationContext(installation, new MavenInstallationDisplayNameFormatter(displayName)));
  }

}
