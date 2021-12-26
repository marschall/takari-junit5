/**
 * Copyright (c) 2014 Takari, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.github.marschall.takari.junit5;

import java.io.BufferedReader;

// some of the code was originally copied from org.apache.maven.plugin.testing.resources.TestResources

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Junit5 test extenation to extract and assert test resources.
 */
public class TestResources implements BeforeEachCallback {

  private final String projectsDir;

  private final String workDir;

  private String name;

  public TestResources() {
    this("src/test/projects", "target/test-projects");
  }

  public TestResources(String projectsDir, String workDir) {
    this.projectsDir = projectsDir;
    this.workDir = workDir;
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    String methodName = context.getTestMethod()
      .map(Method::getName)
      .map(name -> name.replace('/', '_').replace('\\', '_'))
      .orElse("");
    this.name = context.getRequiredTestClass().getSimpleName() + "_" + methodName;
  }

  /**
   * Creates new clean copy of test project directory structure. The copy is named after both the test being executed and test project name, which allows the same test project can be used by multiple
   * tests and by different instances of the same parametrized tests.<br/>
   * TODO Provide alternative working directory naming for Windows, which still limits path names to ~250 charecters
   */
  public File getBasedir(String project) throws IOException {
    if (this.name == null) {
      throw new IllegalStateException(this.getClass().getSimpleName() + " must be a test class field annotated with org.junit.Rule");
    }
    File basedir = new File(this.workDir, this.name + "_" + project).getCanonicalFile();
    FileUtils.deleteDirectory(basedir);
    Assertions.assertTrue(basedir.mkdirs(), "Test project working directory created");
    File src = new File(this.projectsDir, project).getCanonicalFile();
    Assertions.assertTrue(src.isDirectory(), () -> "Test project directory does not exist: " + src.getPath());
    FileUtils.copyDirectoryStructure(src, basedir);
    return basedir;
  }

  /**
   * Creates new clean test work directory. The directory is named after test being executed.
   *
   * @since 2.2
   */
  public File getBasedir() throws IOException {
    if (this.name == null) {
      throw new IllegalStateException(this.getClass().getSimpleName() + " must be a test class field annotated with org.junit.Rule");
    }
    File basedir = new File(this.workDir, this.name).getCanonicalFile();
    FileUtils.deleteDirectory(basedir);
    Assertions.assertTrue(basedir.mkdirs(), "Test project working directory created");
    return basedir;
  }

  // static helpers

  public static void cp(File basedir, String from, String to) throws IOException {
    // TODO ensure destination lastModified timestamp changes
    FileUtils.copyFile(new File(basedir, from), new File(basedir, to));
  }

  public static void assertFileContents(File basedir, String expectedPath, String actualPath) throws IOException {
    String expected = fileRead(new File(basedir, expectedPath), true);
    String actual = fileRead(new File(basedir, actualPath), true);
    Assertions.assertEquals(expected, actual);
  }

  private static String fileRead(File file, boolean normalizeEOL) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      if (normalizeEOL) {
        String str;
        while ((str = r.readLine()) != null) {
          sb.append(str).append('\n');
        }
      } else {
        int ch;
        while ((ch = r.read()) != -1) {
          sb.append((char) ch);
        }
      }
    }
    return sb.toString();
  }

  public static void assertFileContents(String expectedContents, File basedir, String path) throws IOException {
    String actualContents = fileRead(new File(basedir, path), true);
    Assertions.assertEquals(expectedContents, actualContents);
  }

  public static void assertDirectoryContents(File dir, String... expectedPaths) {
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir(dir);
    scanner.addDefaultExcludes();
    scanner.scan();

    Set<String> actual = new TreeSet<>();
    for (String path : scanner.getIncludedFiles()) {
      actual.add(path.replace(File.separatorChar, '/'));
    }
    for (String path : scanner.getIncludedDirectories()) {
      if (path.length() > 0) {
        actual.add(path.replace(File.separatorChar, '/') + "/");
      }
    }

    Set<String> expected = new TreeSet<>();
    if (expectedPaths != null) {
      for (String path : expectedPaths) {
        expected.add(path.replace(File.separatorChar, '/'));
      }
    }

    Assertions.assertEquals(expected, actual);
  }

  public static void touch(File basedir, String path) throws InterruptedException {
    touch(new File(basedir, path));
  }

  public static void touch(File file) throws InterruptedException {
    if (!file.isFile()) {
      throw new IllegalArgumentException("Not a file " + file);
    }
    long lastModified = file.lastModified();
    file.setLastModified(System.currentTimeMillis());

    // TODO do modern filesystems still have this silly lastModified resolution?
    if (lastModified == file.lastModified()) {
      Thread.sleep(1000L);
      file.setLastModified(System.currentTimeMillis());
    }
  }

  public static void rm(File basedir, String path) {
    Assertions.assertTrue(new File(basedir, path).delete(), () -> "delete " + path);
  }

  public static void create(File basedir, String... paths) throws IOException {
    if ((paths == null) || (paths.length == 0)) {
      throw new IllegalArgumentException();
    }
    for (String path : paths) {
      File file = new File(basedir, path);
      file.getParentFile().mkdirs();
      Assertions.assertTrue(file.getParentFile().isDirectory());
      file.createNewFile();
      Assertions.assertTrue(file.isFile() && file.canRead());
    }
  }

  public static void assertFilesPresent(File basedir, String... paths) {
    if ((basedir == null) || (paths == null) || (paths.length <= 0)) {
      throw new IllegalArgumentException();
    }
    if (paths.length == 1) {
      Assertions.assertTrue(new File(basedir, paths[0]).isFile(), () -> paths[0] + " PRESENT");
    } else {
      StringBuilder expected = new StringBuilder();
      StringBuilder actual = new StringBuilder();
      for (String path : paths) {
        expected.append(path).append("\n");
        if (!new File(basedir, path).isFile()) {
          actual.append("NOT PRESENT ");
        }
        actual.append(path).append("\n");
      }
      Assertions.assertEquals(expected, actual);
    }
  }

  public static void assertFilesNotPresent(File basedir, String... paths) {
    if ((basedir == null) || (paths == null) || (paths.length <= 0)) {
      throw new IllegalArgumentException();
    }
    if (paths.length == 1) {
      Assertions.assertFalse(new File(basedir, paths[0]).isFile(), paths[0] + " NOT PRESENT");
    } else {
      StringBuilder expected = new StringBuilder();
      StringBuilder actual = new StringBuilder();
      for (String path : paths) {
        expected.append(path).append("\n");
        if (new File(basedir, path).isFile()) {
          actual.append("PRESENT ");
        }
        actual.append(path).append("\n");
      }
      Assertions.assertEquals(expected, actual);
    }
  }

  /**
   * @since 2.2
   */
  public static Map<String, String> readProperties(File basedir, String path) throws IOException {
    Properties properties = new Properties();
    try (InputStream is = new FileInputStream(new File(basedir, path))) {
      properties.load(is);
    }
    Map<String, String> result = new HashMap<>();
    for (String key : properties.stringPropertyNames()) {
      result.put(key, properties.getProperty(key));
    }
    return Collections.unmodifiableMap(result);
  }
}
