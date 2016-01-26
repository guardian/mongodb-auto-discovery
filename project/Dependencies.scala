import sbt._

object Dependencies {
  val awsSdk = "com.amazonaws" % "aws-java-sdk" % "1.9.12"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
}