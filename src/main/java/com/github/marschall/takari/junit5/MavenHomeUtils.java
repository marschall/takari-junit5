package com.github.marschall.takari.junit5;

import java.io.File;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;

import io.takari.maven.testing.executor.MavenInstallationUtils;

final class MavenHomeUtils {

  private MavenHomeUtils() {
    throw new AssertionError("not instantiable");
  }

  static boolean isForced() {

    File forcedMavenHome = MavenInstallationUtils.getForcedMavenHome();
    File forcedClassworldsConf = MavenInstallationUtils.getForcedClassworldsConf();

    if (forcedMavenHome != null) {
      if (forcedMavenHome.isDirectory() || (forcedClassworldsConf != null && forcedClassworldsConf.isFile())) {
        return true;
      }
      throw new ExtensionConfigurationException("Invalid -Dmaven.home=" + forcedMavenHome.getAbsolutePath());
    }
    return false;
  }

}
