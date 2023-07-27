//import com.hxdts.sbt.gitlab.codeclimate.GitlabCodeclimatePlugin
//import com.hxdts.sbt.gitlab.codeclimate.GitlabCodeclimatePlugin.autoImport._

lazy val root = (project in file("."))
  .enablePlugins(GitlabCodeclimatePlugin)
  .settings(
    scalaVersion := "2.13.10",
    version := "0.1",
    checkstyleFile := "target/scala-2.13/scapegoat-report/scapegoat-scalastyle.xml"
  )
