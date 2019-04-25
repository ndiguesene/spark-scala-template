/*
 * Copyright 2011-2016 Marconi Lanna
 * Copyright 2017 Daniel Bast
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// http://www.scalastyle.org/
// http://github.com/scalastyle/scalastyle-sbt-plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// http://github.com/puffnfresh/wartremover
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.7")

// http://github.com/wartremover/wartremover-contrib/
addSbtPlugin("org.wartremover" % "sbt-wartremover-contrib" % "1.2.4")

// http://github.com/danielnixon/extrawarts
addSbtPlugin("org.danielnixon" % "sbt-extrawarts" % "1.0.3")

// http://github.com/sksamuel/scapegoat
// http://github.com/sksamuel/sbt-scapegoat
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.9")

// http://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

// Adds a `scalafmt` task for automatic source code formatting
// https://github.com/lucidsoftware/neo-sbt-scalafmt
addSbtPlugin("com.lucidchart" % "sbt-scalafmt-coursier" % "1.15")

// Adds a `dependencyUpdates` task to check Maven repositories for dependency updates
// http://github.com/rtimush/sbt-updates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

// https://github.com/sbt/sbt-buildinfo
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

// Adds a `assembly` task to create a fat JAR with all of its dependencies
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")

// http://github.com/sbt/sbt-license-report
addSbtPlugin("com.typesafe.sbt" % "sbt-license-report" % "1.2.0")

// http://github.com/orrsella/sbt-stats
addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.7")

// https://github.com/coursier/coursier.git
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")

// Creates Eclipse project definitions (.classpath, .project, .settings/)
// http://github.com/typesafehub/sbteclipse
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

// Adds a `ensimeConfig` task to generate ensime project definitions
// http://ensime.org/build_tools/sbt/
addSbtPlugin("org.ensime" % "sbt-ensime" % "2.5.1")

// A Sbt plugin that fills apiMappings for common Scala libraries.
// https://github.com/ThoughtWorksInc/sbt-api-mappings/releases
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.0")

// A git plugin for SBT https://github.com/sbt/sbt-git
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

// sbt dependency tree
//dependencyTree , dependencyBrowseGraph, dependencyBrowseTree, whatDependsOn 
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

// disables warning: SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
// as explained here https://github.com/sbt/sbt-git
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25"
