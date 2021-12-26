package com.github.marschall.takari.junit5;

final class MavenInstallationDisplayNameFormatter {

  private final String displayName;

  MavenInstallationDisplayNameFormatter(String displayName) {
    this.displayName = displayName;
  }

  String format(String mavenInstallation) {
    return this.displayName + '[' + mavenInstallation + ']';
  }
  
}
