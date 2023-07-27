package com.hxdts.sbt.gitlab.codeclimate

import sbt.internal.util.ManagedLogger
import sjsonnew.*
import sjsonnew.BasicJsonProtocol.*
import sjsonnew.support.scalajson.unsafe.{CompactPrinter, Converter}

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.security.MessageDigest
import java.util.Formatter
import scala.util.Using
import scala.xml.*

object converter {
  case class CheckstyleError(
      severity: String,
      line: Integer,
      column: String,
      source: String,
      message: String,
      explanation: String
  )
  case class CheckstyleFile(
      filename: String,
      errors: Seq[CheckstyleError]
  )
  case class Checkstyle(
      version: String,
      files: Seq[CheckstyleFile]
  )

  private val hex = new Formatter()

  private def sha256hex(originalString: => String): String = {
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedhash =
      digest.digest(originalString.getBytes(StandardCharsets.UTF_8))

    Using(new Formatter())(
      encodedhash
        .foldLeft(_)((hex, b) =>
          hex.format("%02x", Seq((b & 0xff).asInstanceOf[Object]): _*)
        )
        .toString
    ).getOrElse("")
  }

  implicit object CheckstyleWriter extends JsonWriter[Checkstyle] {

    private def writeCheckstyleFile[J](
        file: CheckstyleFile,
        builder: Builder[J]
    ): Unit = {
      val filename = file.filename
      file.errors.foreach(error => {
        // begin object
        builder.beginObject()
        builder.addField("type", "issue")
        builder.addField("check_name", error.source)
        builder.addField("description", error.message)
        builder.addField(
          "fingerprint",
          sha256hex(s"$filename:${error.message}:${error.line}")
        )

        // begin location
        builder.addFieldName("location")
        builder.beginObject()
        builder.addField("path", filename)
        // begin location.lines
        builder.addFieldName("lines")
        builder.beginObject()
        builder.addField("begin", error.line)
        builder.endObject()
        // end location.lines
        builder.endObject()
        // end location
        builder.endObject()
        // end object
      })

    }

    override def write[J](checkstyle: Checkstyle, builder: Builder[J]): Unit = {
      builder.beginArray()
      checkstyle.files.foreach(writeCheckstyleFile(_, builder))
      builder.endArray()
    }
  }

  def convert(
      input: String,
      output: File,
      basedir: String,
      log: ManagedLogger
  ): Unit = {

    val inputXml = XML.load(input)
    val version = inputXml \@ "version"
    val files = (inputXml \ "file").map(toFile(basedir))

    val checkstyle = Checkstyle(version, files)

    val json = Converter.toJson(checkstyle)

    json.fold(
      ex =>
        log.error(
          s"convert checkstyle file: $input to codeclimate.json file: $output failed: ${ex.getMessage}"
        ),
      a =>
        Using(Files.newBufferedWriter(output.toPath))(
          CompactPrinter.print(a, _)
        )
    )

  }

  private def toError(node: NodeSeq): CheckstyleError = {
    val line = node \@ "line"
    val message = node \@ "message"
    val severity = node \@ "severity"
    val source = node \@ "source"
    val explanation = node \@ "explanation"

    CheckstyleError(
      severity = severity,
      line = line.toInt,
      column = "",
      message = message,
      source = source,
      explanation = explanation
    )
  }

  private def toFile(basedir: String)(node: NodeSeq): CheckstyleFile = {
    val filename = node \@ "name"
    val errors = (node \ "error").map(toError)
    CheckstyleFile(filename = filename.replace(basedir, ""), errors = errors)
  }

}
