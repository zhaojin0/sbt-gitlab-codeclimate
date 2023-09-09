ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

licenses := Seq(
  "BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")
)

homepage := Some(
  url("https://gitlab.hxdts.com/supports/sbt-gitlab-codeclimate")
)

scmInfo := Some(
  ScmInfo(
    url("https://gitlab.hxdts.com/supports/sbt-gitlab-codeclimate"),
    "scm:git@gitlab.hxdts.com:60022/supports/sbt-gitlab-codeclimate.git"
  )
)

developers := List(
  Developer(
    id = "zhaojin",
    name = "Zhao Jin",
    email = "zhaojin@hbhxdts.com",
    url = url("https://www.hxdts.com")
  )
)

publishTo := {
  val repo = "https://repo.hxdts.com"
  if (isSnapshot.value) Some("snapshots" at repo + "/snapshots")
  else Some("releases" at repo + "/releases")
}

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, SbtPlugin)
  .settings(
    name := "sbt-gitlab-codeclimate",
    organization := "com.hxdts.sbt",
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "com.hxdts.sbt.gitlab.codeclimate",
    sbtPlugin := true,
    publishMavenStyle := true,
    Test / publishArtifact := false,
    pluginCrossBuild / sbtVersion := "1.5.5",
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq(
          "-Xmx512m",
          "-Dplugin.version=" + version.value
        )
    },
    sbtTestDirectory := sourceDirectory.value / "sbt-test",
    scriptedBufferLog := false
  )
