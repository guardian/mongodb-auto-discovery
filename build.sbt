import Dependencies._

name := "auto-mongodb-discovery"

version := "1.0"

scalaVersion := "2.11.7"

organization := "com.gu"

crossScalaVersions := Seq(scalaVersion.value)

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/auto-mongodb-discovery"),
  "scm:git:git@github.com:guardian/auto-mongodb-discovery.git"
))

libraryDependencies ++= Seq(
  awsSdk,
  scalaTest,
  scalaCheck
)