/**
 * Copyright (c) 2014 Takari, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.github.marschall.takari.junit5;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.Description;

/**
 * JUnit 5 test extenation to extract and assert test resources.
 */
public final class TestResources extends io.takari.maven.testing.TestResources implements BeforeEachCallback {

  public TestResources() {
    super();
  }

  public TestResources(String projectsDir, String workDir) {
    super(projectsDir, workDir);
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    String methodName = context.getTestMethod().map(Method::getName).orElse(null);
    Description description = Description.createTestDescription(context.getRequiredTestClass(), methodName);
    this.starting(description);
  }
}
