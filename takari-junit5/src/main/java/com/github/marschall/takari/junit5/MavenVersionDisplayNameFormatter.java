package com.github.marschall.takari.junit5;

final class MavenVersionDisplayNameFormatter {

  private final String displayName;

  MavenVersionDisplayNameFormatter(String displayName) {
    this.displayName = displayName;
  }

  String format(String mavenVersion) {
    return this.displayName + '[' + mavenVersion + ']';
  }
  
}
