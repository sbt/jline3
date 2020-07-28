val junit = "junit" % "junit" % "4.12"
val junitInterface = "com.novocode" % "junit-interface" % "0.11"
val easymock = "org.easymock" % "easymock" % "3.3.1"

val commonSettings = Def.settings(
  git.baseVersion := "3.16.0-sbt",
  autoScalaLibrary := false,
  crossPaths := false,
  libraryDependencies ++= Seq(
    junit % Test,
    junitInterface % Test,
    easymock % Test
  ),
  Test / fork := true,
  Test / parallelExecution := false
)

val terminal = project.settings(
  name := "jline-terminal",
  commonSettings,
)

val root = (project in file("."))
  .enablePlugins(GitVersioning)
  .settings(
    commonSettings,
    publish / skip := true,
  )
  .aggregate(terminal)

ThisBuild / organization := "org.scala-sbt.jline3"
ThisBuild / organizationHomepage := Some(url("https://scala-sbt.org/"))
ThisBuild / homepage := Some(url("https://github.com/sbt/jline3"))
ThisBuild / description := "patched jline3 for sbt"
ThisBuild / licenses := List(
  "The BSD License" -> url("http://www.opensource.org/licenses/bsd-license.php")
)
ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/sbt/jline3"), "git@github.com:sbt/jline3.git")
)
ThisBuild / developers := List(
  Developer(
    "eed3si9n",
    "Eugene Yokota",
    "@eed3si9n",
    url("https://github.com/eed3si9n")
  ),
  Developer(
    "eatkins",
    "Ethan Atkins",
    "@eatkins",
    url("https://www.ethanatkins.com")
  )
)
ThisBuild / pomIncludeRepository := { _ =>
  false
}
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  // if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots") else
  Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
