package com.hxdts.sbt.gitlab.codeclimate

import sbt._

trait GitlabCodeclimateKeys {

  /** input checkstyle.xml file location
    */
  val checkstyleFile = settingKey[String]("input checkstyle.xml")

  /** output file location, default target codeclimate.json
    */
  val codeclimateFile = settingKey[Option[String]]("output codeclimate.json")

  /** file base dir
    */
  val fileBasedir = settingKey[Option[String]](
    "base dir of files, default as root project basedir"
  )

  /** task key
    */
  val `gitlab-codeclimate` =
    taskKey[Unit]("covert checkstyle to gitlab codeclimate formatter")

  val `gitlab-codeclimate-aggregate` =
    taskKey[Unit]("aggregate checkstyle to gitlab codeclimate formatter")
}
