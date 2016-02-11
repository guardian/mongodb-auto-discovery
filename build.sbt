import sbtrelease._
import ReleaseTransformations._
import Dependencies._

name := "mongodb-auto-discovery"

scalaVersion := "2.11.7"

organization := "com.gu"

crossScalaVersions := Seq(scalaVersion.value)

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/mongodb-auto-discovery"),
  "scm:git:git@github.com:guardian/mongodb-auto-discovery.git"
))

homepage := Some(url("https://github.com/guardian/mongodb-auto-discovery"))

libraryDependencies ++= Seq(
  awsSdk,
  scalaTest,
  scalaCheck
)

sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

publishMavenStyle := true
publishArtifact in Test := false
bintrayOrganization := Some("guardian")
bintrayRepository := "platforms"
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

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
