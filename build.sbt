import sbtrelease._
import ReleaseTransformations._
import Dependencies._

name := "mongodb-auto-discovery"

version := "1.0"

scalaVersion := "2.11.7"

organization := "com.gu"

crossScalaVersions := Seq(scalaVersion.value)

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/mongodb-auto-discovery"),
  "scm:git:git@github.com:guardian/mongodb-auto-discovery.git"
))

libraryDependencies ++= Seq(
  awsSdk,
  scalaTest,
  scalaCheck
)

publishMavenStyle := true
publishArtifact in Test := false
bintrayOrganization := Some("guardian")
bintrayRepository := "platforms"
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
