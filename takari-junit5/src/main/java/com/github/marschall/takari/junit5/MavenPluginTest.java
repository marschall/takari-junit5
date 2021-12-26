package com.github.marschall.takari.junit5;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Meta-annotation for Maven plugin tests, registers all necessary extensions.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
@ExtendWith({
  ForcedMavenRuntimeBuilderParameterResolver.class,
  MavenInstallationsTestExtension.class,
  MavenVersionsTestExtension.class})
public @interface MavenPluginTest {

}
