JUnit 5 Support for Takari Maven Plugin Testing Framework
=========================================================

Adds JUnit 5 Support for [Takari Maven Plugin Testing Framework](https://github.com/takari/takari-plugin-testing-project) until [takari-plugin-testing-project#38](https://github.com/takari/takari-plugin-testing-project/pull/38) is merged.


Unit testing
------------

JUnit 4 is still required as a dependency.

```java
class PluginUnitTest {
  @RegisterExtension
  final TestResources resources = new TestResources();

  @RegisterExtension
  final TestMavenRuntime maven = new TestMavenRuntime();

  @Test
  void test() throws Exception {
    File basedir = resources.getBasedir("testproject");
    maven.executeMojo(basedir, "mymojo", newParameter("name", "value");
    assertFilesPresent(basedir, "target/output.txt");
  }
}
```

import `com.github.marschall.takari.junit5.TestResources` and `com.github.marschall.takari.junit5.TestMavenRuntime` instead of `io.takari.maven.testing.TestResources` and `io.takari.maven.testing.TestMavenRuntime`.

Integration testing
-------------------

JUnit 4 is still required as a dependency.

```java
@MavenVersions({"3.2.3", "3.2.5"})
public class PluginIntegrationTest {
  @RegisterExtension
  public final TestResources resources = new TestResources();

  private final MavenRuntime maven;

  public PluginIntegrationTest(MavenRuntimeBuilder mavenBuilder) {
    this.maven = mavenBuilder.withCliOptions("-B", "-U").build();
  }

  @MavenPluginTest
  void test() throws Exception {
    File basedir = resources.getBasedir("basic");
    maven.forProject(basedir)
      .withCliOption("-Dproperty=value")
      .withCliOption("-X")
      .execute("deploy")
      .assertErrorFreeLog()
      .assertLogText("some build message");
  }
}

```

This is unofficial and not related to Takari.