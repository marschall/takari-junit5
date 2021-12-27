package com.github.marschall.takari.junit5;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.inject.Module;

public final class TestMavenRuntime extends io.takari.maven.testing.TestMavenRuntime implements BeforeEachCallback, AfterEachCallback {

  private final Module[] modules;

  public TestMavenRuntime() {
    super();
    this.modules = new Module[0];
  }

  public TestMavenRuntime(Module... modules) {
    super(modules);
    this.modules = modules;
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Object runtimeInstance = this.createRuntimeReflectively();
    this.setRuntimeReflectively(runtimeInstance);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Object runtime = this.getRuntimeReflectively();
    this.shutdownRuntime(runtime);
    this.setRuntimeReflectively(null);
  }

  private void shutdownRuntime(Object runtime) throws ReflectiveOperationException {
    Method shutdownMethod = runtime.getClass().getMethod("shutdown");
    shutdownMethod.invoke(runtime);
  }

  private void setRuntimeReflectively(Object runtimeInstance) throws ReflectiveOperationException {
    Field runtimeField = io.takari.maven.testing.TestMavenRuntime.class.getDeclaredField("runtime");
    runtimeField.setAccessible(true);
    runtimeField.set(this, runtimeInstance);
  }

  private Object getRuntimeReflectively() throws ReflectiveOperationException {
    Field runtimeField = io.takari.maven.testing.TestMavenRuntime.class.getDeclaredField("runtime");
    runtimeField.setAccessible(true);
    return runtimeField.get(this);
  }

  private Object createRuntimeReflectively() throws ReflectiveOperationException {
    Method newMavenRuntimeMethod = io.takari.maven.testing.TestMavenRuntime.class.getDeclaredMethod("newMavenRuntime", Module[].class);
    newMavenRuntimeMethod.setAccessible(true);
    Object runtimeInstance = newMavenRuntimeMethod.invoke(this, new Object[] {this.modules});
    return runtimeInstance;
  }


}
