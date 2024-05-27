# Gluon plugin for SBT

The Gluon plugin for sbt projects leverages GraalVM, OpenJDK and JavaFX 21,
by compiling into native code the Java Client application and all its required dependencies,
so it can directly be executed as a native application on the target platform.

## Getting started

To use the plugin, apply the following steps:

### 1. Apply the plugin


Notice: You should add resolves because of dependency

    resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/public"

Edit your project/plugins.bt file and add the plugin:

    addSbtPlugin("io.github.thinkiny" % "sbt-gluon-plugin" % "0.3.0")

The plugin allows some options that can be set in `configuration`, to modify the default settings, and several goals, to build and run the native application.
Don't forget to set up `GRAALVM_PATH` environment.

### 2. Goals

Once the project is ready, the Gluon plugin has these main goals:

#### `gluonPackage`
#### `gluonBuild`

This goal does the AOT compilation. It is a very intensive and lengthy task (several minutes, depending on your project and CPU), so it should be called only when the project is ready and runs fine on a VM.

Run:

    sbt gluonBuild
