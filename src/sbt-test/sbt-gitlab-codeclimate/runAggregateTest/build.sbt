//import com.hxdts.sbt.gitlab.codeclimate.GitlabCodeclimatePlugin
//import com.hxdts.sbt.gitlab.codeclimate.GitlabCodeclimatePlugin.autoImport._

lazy val subproject1 = (project in file("project-1"))
  .settings(
    scalaVersion := "2.13.10",
    version := "0.1",
    checkstyleFile := "target/scala-2.13/scapegoat-report/scapegoat-scalastyle.xml"
  )

lazy val subproject2 = (project in file("project-2"))
  .settings(
    scalaVersion := "2.13.10",
    version := "0.1",
    checkstyleFile := "target/scala-2.13/scapegoat-report/scapegoat-scalastyle.xml"
  )

lazy val subproject3 = (project in file("modules/project-3"))
  .settings(
    scalaVersion := "2.13.10",
    checkstyleFile := "target/scala-2.13/scapegoat-report/scapegoat-scalastyle.xml"
  )

lazy val root = (project in file("."))
  .enablePlugins(GitlabCodeclimatePlugin)
  .settings(
    scalaVersion := "2.13.10",
    version := "0.1",
    checkstyleFile := "target/scala-2.13/scapegoat-report/scapegoat-scalastyle.xml"
  )
  .aggregate(subproject1, subproject2, subproject3)
