package com.hxdts.sbt.gitlab.codeclimate

import sbt.*
import sbt.Keys.{state, streams, target, thisProject}

object GitlabCodeclimatePlugin extends AutoPlugin {
  override val trigger: PluginTrigger = noTrigger
  override val requires: Plugins = plugins.JvmPlugin
  object autoImport extends GitlabCodeclimateKeys

  import autoImport._

  override lazy val globalSettings: Seq[Def.Setting[_]] = Seq(
    checkstyleFile := "target/checkstyle.xml",
    codeclimateFile := None,
    fileBasedir := None
  )

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    `gitlab-codeclimate` := convertTask.value,
    `gitlab-codeclimate-aggregate` := convertAggregateTask.value
  )

  lazy val convertTask = Def.task {

    val basedir =
      fileBasedir.value.getOrElse(thisProject.value.base.getAbsolutePath + "/")

    val output = codeclimateFile.value
      .orElse(Some("codeclimate.json"))
      .map(file => target.value / file)
      .get

    converter.convert(
      checkstyleFile.value,
      output,
      basedir,
      streams.value.log
    )
  }

  lazy val convertAggregateTask = Def.task({
    val s = state.value
    val log = streams.value.log
    val extracted = Project.extract(s)

    val jsonFiles = extracted.structure.allProjectPairs.map {
      case (project, ref) =>
        val basedir = project.base.getAbsolutePath + "/"
        val file = extracted
          .getOpt(ref / checkstyleFile)
          .getOrElse(checkstyleFile.value)
        basedir + file
    }

    val basedir =
      fileBasedir.value.getOrElse(thisProject.value.base.getAbsolutePath + "/")

    val output = codeclimateFile.value
      .orElse(Some("codeclimate.json"))
      .map(file => target.value / file)
      .get

    converter.aggregateConvert(basedir, jsonFiles, output, log)

  })

}
