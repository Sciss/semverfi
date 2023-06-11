lazy val baseName       = "semverfi"
lazy val gitHost        = "codeberg.org"
lazy val gitUser        = "sciss"
lazy val gitRepo        = baseName
lazy val projectVersion = "0.3.0"

lazy val root = project
  .in(file("."))
  .aggregate(semverfiJS, semverfiJVM)
  .settings(
    scalaVersion := "2.12.3",
    publish := {},
    publishLocal := {}
  )

lazy val deps = new {
  lazy val test = new {
    val specs = "4.20.0"
  }
  lazy val main = new {
     val parserCombinators = "2.3.0"
  }
}

lazy val semverfi = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(commonSettings)
  .settings(
    name := "semverfi",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % deps.test.specs % Test
    )).
    jvmSettings(
      // Add JVM-specific settings here
      libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % deps.main.parserCombinators,
      name := "semverfi"
    ).
    jsSettings(
      // Add JS-specific settings here
      libraryDependencies += "org.scala-lang.modules" %%% "scala-parser-combinators" % deps.main.parserCombinators,
      name := "semverfi-js"
  )

lazy val semverfiJVM = semverfi.jvm
lazy val semverfiJS  = semverfi.js

ThisBuild / version       := projectVersion
ThisBuild / organization  := "de.sciss"
ThisBuild / versionScheme := Some("pvp")

lazy val commonSettings = Seq(
  scalaVersion        := "2.13.11",
  crossScalaVersions  := Seq("3.3.0", "2.13.11", "2.12.18"),
  moduleName          := baseName,
  description         := "Always Faithful, always loyal semantic versions",
  homepage            := Some(url(s"https://$gitHost/$gitUser/$gitRepo")),
  licenses            := Seq("APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  scalacOptions ++= Seq(
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
    "-language:implicitConversions",     // Allow definition of implicit functions called views
    "-language:postfixOps"
  )
) ++ publishSettings

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  developers := List(
    Developer(
      id = "softprops",
      name = " Doug Tangren",
      email = "d.tangren@gmail.com",
      url = url("https://lessis.me/")
    ),
    Developer(
      id = "rleibman",
      name = "Robero Leibman",
      email = "roberto@leibman.net",
      url = url("https://github.com/rleibman")
    ),
    Developer(
      id    = "sciss",
      name  = "Hanns Holger Rutz",
      email = "contact@sciss.de",
      url   = url("https://www.sciss.de")
    )
  ),
  scmInfo := {
    val h = gitHost
    val a = s"$gitUser/$gitRepo"
    Some(ScmInfo(url(s"https://$h/$a"), s"scm:git@$h:$a.git"))
  }
)
