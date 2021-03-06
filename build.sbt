name := "Damysos"

version := "1"

scalaVersion := "2.12.8"

// SBT scoverage settings
coverageMinimum := 95
coverageFailOnMinimum := true

val MonocleVersion = "1.5.0"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %%  "monocle-core"  % MonocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % MonocleVersion,
  "org.typelevel" %% "cats-core" % "2.0.0-M1",
  "org.scalatest" %% "scalatest" % "3.0.5"
)

scalacOptions ++= Seq("-deprecation", "-feature")

javaOptions ++= Seq(
  "-XX:+CMSClassUnloadingEnabled" // Enable class unloading under the CMS GC
)

// Test suite settings
fork in Test := true
