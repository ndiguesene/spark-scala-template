import sbt._

object Dependencies {
  // Versions
 // lazy val playVersion = "2.6.8"
  lazy val sparkVersion = "2.1.0"
  lazy val sparkTestingVersion = "2.1.0_0.11.0"

  // test dependencies
  val scalaTest = Seq(
    "org.scalatest"    %% "scalatest"          % "3.0.5",
    "com.holdenkarau"  %% "spark-testing-base" % sparkTestingVersion,
    "org.apache.spark" %% "spark-hive"         % sparkVersion // required by spark-testing-base
    // "org.scalacheck"    %% "scalacheck"                  % "1.13.5",
    // "org.scalamock"     %% "scalamock-scalatest-support" % "3.6.0",
    // "com.storm-enroute" %% "scalameter"                  % "0.8.2",
    // "es.ucm.fdi"        %% "sscheck"                     % "0.3.2",
  ) map (_ % Test)

  // spark dependencies
  val spark2 = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
    "org.apache.spark" %% "spark-sql" % sparkVersion % Provided
    //"org.apache.spark" %% "spark-hive" % sparkVersion % Provided,
    //"org.apache.spark" %% "spark-mllib" % sparkVersion % Provided
  )
  
  //others dependencies
  val gigahorse = "com.eed3si9n" %% "gigahorse-okhttp" % "0.3.1"
  //val playJson  = "com.typesafe.play" %% "play-json" % playVersion
  val slf4j = "org.slf4j" % "slf4j-log4j12"  % "1.7.25"
  val logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
  val scallop = "org.rogach" %% "scallop" % "3.1.5"

  val others = Seq(gigahorse, /*playJson,*/ slf4j, logging, scallop).map(_.exclude("ch.qos.logback", "logback-classic"))   

  // all dependencies
  val dependencies = spark2 ++ scalaTest ++ others
}
