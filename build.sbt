//importing dependencies
import Dependencies._

/*
 * Project metadata
 */

name := "PROJECT"

// enable versioning based on tags, see https://git-scm.com/docs/git-describe
// requires a full repo clone on the continuous integration machine (not a shallow clone)
enablePlugins(GitVersioning)
git.useGitDescribe := true

description := "PROJECT DESCRIPTION"
organization := "com.sonatel"
organizationName := "Sonatel SA"
organizationHomepage := Some(url("http://orange-sonatel.com"))
homepage := Some(url("http://project.org"))
startYear := Some(2019)

licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")

/*
 * scalac configuration
 */
// Use the same scala version Spark is build with, see scala.version in
// https://github.com/apache/spark/blob/master/pom.xml
scalaVersion in ThisBuild := "2.11.12"

compileOrder := CompileOrder.JavaThenScala

// Load test configuration and enable BuildInfo
lazy val root = Project("root", file("."))
  .configs(Testing.all: _*)
  .settings(Testing.settings)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := BuildInfoKey.ofN(name, version, scalaVersion, sbtVersion)
  )

// more memory Spark in local mode, see https://github.com/holdenk/spark-testing-base
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")

val commonScalacOptions = Seq(
  "-encoding",
  "UTF-8", // Specify character encoding used by source files
  "-target:jvm-1.8", // Target platform for object files
  "-Xexperimental", // Enable experimental extensions
  "-Xfuture" // Turn on future language features
//"-Ybackend:GenBCode" // Choice of bytecode emitter
)

val compileScalacOptions = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs
  "-feature", // Emit warning and location for usages of features that should be imported explicitly
  "-g:vars",  // Set level of generated debugging info: none, source, line, vars, notailcalls
//"-language:_" // Enable or disable language features (see list below)
  "-optimise",  // Generates faster bytecode by applying optimisations to the program
  "-unchecked", // Enable additional warnings where generated code depends on assumptions
//"-Xdev" // Indicates user is a developer - issue warnings about anything which seems amiss
  "-Xfatal-warnings", // Fail the compilation if there are any warnings
  "-Xlint:_", // Enable or disable specific warnings (see list below)
  "-Xstrict-inference", // Don't infer known-unsound types
  "-Yclosure-elim", // Perform closure elimination
  "-Yconst-opt", // Perform optimization with constant values
  "-Ydead-code", // Perform dead code elimination
  "-Yinline", // Perform inlining when possible
  "-Yinline-handlers", // Perform exception handler inlining when possible
  "-Yinline-warnings", // Emit inlining warnings
  "-Yno-adapted-args", // Do not adapt an argument list to match the receiver
//"-Yno-imports" // Compile without importing scala.*, java.lang.*, or Predef
//"-Yno-predef" // Compile without importing Predef
  "-Yopt:_", // Enable optimizations (see list below)
  "-Ywarn-dead-code", // Warn when dead code is identified
  "-Ywarn-numeric-widen", // Warn when numerics are widened
  "-Ywarn-unused", // Warn when local and private vals, vars, defs, and types are unused
  "-Ywarn-unused-import", // Warn when imports are unused
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused
)

scalacOptions ++= commonScalacOptions ++ compileScalacOptions ++ Seq(
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused
)

scalacOptions in (Test, compile) := commonScalacOptions ++ compileScalacOptions

scalacOptions in (Compile, console) := commonScalacOptions ++ Seq(
  "-language:_", // Enable or disable language features (see list below)
  "-nowarn" // Generate no warnings
)

scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value

// Have fullClasspath during compile, test and run, but don't assemble what is marked provided
// https://github.com/sbt/sbt-assembly#-provided-configuration
run in Compile := Defaults
  .runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))
  .evaluated

/*
 * Managed dependencies
 */

libraryDependencies ++= dependencies

/*
 * sbt options
 */

// Add task to check for sbt plugin updates
addCommandAlias("pluginUpdates", "; reload plugins; dependencyUpdates; reload return")

// Statements executed when starting the Scala REPL (sbt's `console` task)
initialCommands := """
print('initCommand in sbt console')
"""

cleanupCommands in console := """
print('cleanupCommand in sbt console')
"""

// Do not exit sbt when Ctrl-C is used to stop a running app
cancelable in Global := true

// Improved dependency management
updateOptions := updateOptions.value.withCachedResolution(true)

showSuccess := true
showTiming := true

// Uncomment to enable offline mode
// offline := true

// Download and create Eclipse source attachments for library dependencies
// EclipseKeys.withSource := true

// Enable colors in Scala console (2.11.4+)
initialize ~= { _ =>
  val ansi = System.getProperty("sbt.log.noformat", "false") != "true"
  if (ansi) System.setProperty("scala.color", "true")
}

// Draw a separator between triggered runs (e.g, ~test)
triggeredMessage := { ws =>
  if (ws.count > 1) {
    val ls = System.lineSeparator * 2
    ls + "#" * 100 + ls
  } else { "" }
}

shellPrompt := { state =>
  import scala.Console.{ BLUE, BOLD, RESET }
  s"$BLUE$BOLD${name.value}$RESET $BOLD\u25b6$RESET "
}

/*
 * Scalastyle: http://www.scalastyle.org/
 */
scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"
scalastyleFailOnError := true

// Create a default Scalastyle task to run with tests
lazy val mainScalastyle = taskKey[Unit]("mainScalastyle")
lazy val testScalastyle = taskKey[Unit]("testScalastyle")

mainScalastyle := scalastyle.in(Compile).toTask("").value
testScalastyle := scalastyle.in(Test).toTask("").value

(test in Test) := ((test in Test) dependsOn testScalastyle).value
(test in Test) := ((test in Test) dependsOn mainScalastyle).value

/*
 * sbt-assembly https://github.com/sbt/sbt-assembly
 */
test in assembly := {}
// scala-library is provided by spark cluster execution environment
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

/*
 * WartRemover: http://github.com/wartremover/wartremover
 */
wartremoverErrors ++= Seq(
  Wart.Any,
  Wart.ArrayEquals,
  Wart.AsInstanceOf,
  Wart.DefaultArguments,
  Wart.EitherProjectionPartial,
  Wart.Enumeration,
//Wart.Equals,
//Wart.ExplicitImplicitTypes,
//Wart.FinalCaseClass,
  Wart.FinalVal,
  Wart.ImplicitConversion,
//Wart.ImplicitParameter,
  Wart.IsInstanceOf,
  Wart.JavaConversions,
  Wart.LeakingSealed,
  Wart.MutableDataStructures,
//Wart.NonUnitStatements,
//Wart.Nothing,
  Wart.Null,
  Wart.Option2Iterable,
  Wart.OptionPartial,
  Wart.Overloading,
  Wart.Product,
//Wart.PublicInference,
//Wart.Recursion,
  Wart.Return,
  Wart.Serializable,
  Wart.StringPlusAny,
  Wart.Throw,
  Wart.ToString,
  Wart.TraversableOps,
  Wart.TryPartial,
  Wart.Var,
  Wart.While,
  ContribWart.ExposedTuples,
  ContribWart.OldTime,
  ContribWart.SealedCaseClass,
  ContribWart.SomeApply,
  ExtraWart.EnumerationPartial,
  ExtraWart.FutureObject,
  ExtraWart.GenMapLikePartial,
  ExtraWart.GenTraversableLikeOps,
  ExtraWart.GenTraversableOnceOps,
  ExtraWart.ScalaGlobalExecutionContext,
  ExtraWart.StringOpsPartial,
  ExtraWart.TraversableOnceOps
)

/*
 * Scapegoat: http://github.com/sksamuel/scapegoat
 */
scapegoatVersion in ThisBuild := "1.3.8"
scapegoatDisabledInspections := Seq.empty
scapegoatIgnoredFiles := Seq.empty

// Create a default Scapegoat task to run with tests
lazy val mainScapegoat = taskKey[Unit]("mainScapegoat")
lazy val testScapegoat = taskKey[Unit]("testScapegoat")

mainScapegoat := scapegoat.in(Compile).value
testScapegoat := scapegoat.in(Test).value

(test in Test) := ((test in Test) dependsOn testScapegoat).value
(test in Test) := ((test in Test) dependsOn mainScapegoat).value

/*
 * Linter: http://github.com/HairyFotr/linter
 */

addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.17")

scalacOptions += "-P:linter:enable-only:" +
"AssigningOptionToNull+" +
"AvoidOptionCollectionSize+" +
"AvoidOptionMethod+" +
"AvoidOptionStringSize+" +
"BigDecimalNumberFormat+" +
"BigDecimalPrecisionLoss+" +
"CloseSourceFile+" +
"ContainsTypeMismatch+" +
"DecomposingEmptyCollection+" +
"DivideByOne+" +
"DivideByZero+" +
"DuplicateIfBranches+" +
"DuplicateKeyInMap+" +
"EmptyStringInterpolator+" +
"FilterFirstThenSort+" +
"FloatingPointNumericRange+" +
"FuncFirstThenMap+" +
"IdenticalCaseBodies+" +
"IdenticalCaseConditions+" +
"IdenticalIfCondition+" +
"IdenticalIfElseCondition+" +
"IdenticalStatements+" +
"IfDoWhile+" +
"IndexingWithNegativeNumber+" +
"InefficientUseOfListSize+" +
"IntDivisionAssignedToFloat+" +
"InvalidParamToRandomNextInt+" +
"InvalidStringConversion+" +
"InvalidStringFormat+" +
"InvariantCondition+" +
"InvariantExtrema+" +
"InvariantReturn+" +
"JavaConverters+" +
"LikelyIndexOutOfBounds+" +
"MalformedSwap+" +
"MergeMaps+" +
"MergeNestedIfs+" +
"ModuloByOne+" +
"NumberInstanceOf+" +
"OnceEvaluatedStatementsInBlockReturningFunction+" +
"OperationAlwaysProducesZero+" +
"OptionOfOption+" +
"PassPartialFunctionDirectly+" +
"PatternMatchConstant+" +
"PossibleLossOfPrecision+" +
"PreferIfToBooleanMatch+" +
"ProducesEmptyCollection+" +
"ReflexiveAssignment+" +
"ReflexiveComparison+" +
"RegexWarning+" +
"StringMultiplicationByNonPositive+" +
"SuspiciousMatches+" +
"SuspiciousPow+" +
"TransformNotMap+" +
"TypeToType+" +
"UndesirableTypeInference+" +
"UnextendedSealedTrait+" +
"UnitImplicitOrdering+" +
"UnlikelyEquality+" +
"UnlikelyToString+" +
"UnnecessaryMethodCall+" +
"UnnecessaryReturn+" +
"UnnecessaryStringIsEmpty+" +
"UnnecessaryStringNonEmpty+" +
"UnsafeAbs+" +
"UnthrownException+" +
"UnusedForLoopIteratorValue+" +
"UnusedParameter+" +
"UseAbsNotSqrtSquare+" +
"UseCbrt+" +
"UseConditionDirectly+" +
"UseContainsNotExistsEquals+" +
"UseCountNotFilterLength+" +
"UseExistsNotCountCompare+" +
"UseExistsNotFilterIsEmpty+" +
"UseExistsNotFindIsDefined+" +
"UseExp+" +
"UseExpm1+" +
"UseFilterNotFlatMap+" +
"UseFindNotFilterHead+" +
"UseFlattenNotFilterOption+" +
"UseFuncNotFold+" +
"UseFuncNotReduce+" +
"UseFuncNotReverse+" +
"UseGetOrElseNotPatMatch+" +
"UseGetOrElseOnOption+" +
"UseHeadNotApply+" +
"UseHeadOptionNotIf+" +
"UseHypot+" +
"UseIfExpression+" +
"UseInitNotReverseTailReverse+" +
"UseIsNanNotNanComparison+" +
"UseIsNanNotSelfComparison+" +
"UseLastNotApply+" +
"UseLastNotReverseHead+" +
"UseLastOptionNotIf+" +
"UseLog10+" +
"UseLog1p+" +
"UseMapNotFlatMap+" +
"UseMinOrMaxNotSort+" +
"UseOptionExistsNotPatMatch+" +
"UseOptionFlatMapNotPatMatch+" +
"UseOptionFlattenNotPatMatch+" +
"UseOptionForallNotPatMatch+" +
"UseOptionForeachNotPatMatch+" +
"UseOptionGetOrElse+" +
"UseOptionIsDefinedNotPatMatch+" +
"UseOptionIsEmptyNotPatMatch+" +
"UseOptionMapNotPatMatch+" +
"UseOptionOrNull+" +
"UseOrElseNotPatMatch+" +
"UseQuantifierFuncNotFold+" +
"UseSignum+" +
"UseSqrt+" +
"UseTakeRightNotReverseTakeReverse+" +
"UseUntilNotToMinusOne+" +
"UseZipWithIndexNotZipIndices+" +
"VariableAssignedUnusedValue+" +
"WrapNullWithOption+" +
"YodaConditions+" +
"ZeroDivideBy"

/*
 * scoverage: http://github.com/scoverage/sbt-scoverage
 */
coverageMinimum := 90
coverageFailOnMinimum := false
coverageOutputCobertura := true
coverageOutputHTML := true
coverageOutputXML := true
coverageExcludedPackages := "weatherapi.WeatherMain.scala;weatherapi.Weather.scala;project.Processing.scala"

/*
 * Scalafmt: http://github.com/lucidsoftware/neo-sbt-scalafmt
 */
scalafmtConfig in ThisBuild := baseDirectory.value / "project" / "scalafmt.conf"
scalafmtOnCompile in ThisBuild := true
scalafmtVersion := "1.4.0"
inConfig(IntegrationTest)(scalafmtSettings)

/*
 * Scaladoc options
 */
autoAPIMappings := true
scalacOptions in (Compile, doc) ++= Seq("-groups", "-implicits") // "-diagrams")
