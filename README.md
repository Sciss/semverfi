# semverfi

[![Build Status](https://github.com/Sciss/semverfi/workflows/Scala%20CI/badge.svg?branch=main)](https://github.com/Sciss/semverfi/actions?query=workflow%3A%22Scala+CI%22)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/semverfi_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/semverfi_2.13)

A library for parsing, querying, and ordering the always faithful, always loyal [semantic versions][sv].
Originally written by Doug Tangren (softprops).

## install

### the cut-and-paste method

Add the following to your sbt build definition

    libraryDependencies += "de.sciss" %% "semverfi" % "0.3.0"

Supported Scala versions are 3, 2.13, 2.12, platforms JVM and JS.

## usage.

### it parses

```scala
import semverfi._

val patches =
  Map("normal"    -> "1.0.1",
    "pre-release" -> "1.0.2-alpha.1",
    "build"       -> "1.0.3-alpha.1+build10",
    "garbage"     -> "asfnaasfiasdf")
    .values
    .map(Version.apply)
    .map({
       case n @ NormalVersion(_, _, p) =>
         p
       case pr @ PreReleaseVersion(_, _, p, _) =>
         p
       case b @ BuildVersion(_, _, p, _, _) =>
         p
       case i @ Invalid(in) =>
         println("%s was invalid" format in)
         i.patch
    })
```

### it orders

The semver specification defines precedence rules for how to order versions.
This library abides by those rules.

```scala
import semverfi._
import scala.util.Random.shuffle

val expected = List("1.0.0-alpha", "1.0.0-alpha.1",  "1.0.0-beta.2", "1.0.0-beta.11", "1.0.0-rc.1", "1.0.0-rc.1+build.1", "1.0.0", "1.0.0+0.3.7", "1.3.7+build", "1.3.7+build.2.b8f12d7", "1.3.7+build.11.e0f985a")

val shuffled = shuffle(expected)

val parsed = shuffled.map(Version.apply)

// print list of sorted versions zipped with expected input
(parsed.sorted zip expected).foreach(println)
```

### it bumps ( valid versions )

```scala
import semverfi._
Version("1.1.1").opt { version =>
  println(version.bumpMajor) // NormalVersion(2,0,0)
  println(version.bumpMinor) // NormalVersion(1,2,0)
  println(version.bumpPatch) // NormalVersion(1,1,2)
}
```

### it transitions ( valid versions )

```scala
import semverfi._
Version("1.1.1").opt.map(_.prerelease("SNAPSHOT").build("123").normalize)
```

Doug Tangren (softprops) 2012

[sv]: http://semver.org/
