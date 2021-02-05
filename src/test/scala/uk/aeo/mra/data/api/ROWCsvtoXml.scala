package uk.aeo.mra.data.api

import java.io.{File, FileInputStream, FileNotFoundException, IOException}
import java.security.{DigestInputStream, MessageDigest}

import play.api.libs.json.Json
import play.api.libs.ws.WSResponse

import scala.reflect.io.Directory


object ROWCsvtoXml extends Api {

  override def serviceName: String = "aeo-data-converter"


  def successRowCsvToXmlConverter(orgFileName: String): Unit = {
    val src = scala.io.Source.fromURL(s"${testConfig.url}/download/$orgFileName")
    val out = new java.io.FileWriter(s"src/test/resources/$orgFileName")
    out.write(src.mkString)
    out.close
    val payload =
      s"""
         |{
         |    "download": {
         |        "url": "${testConfig.url}/download/$orgFileName",
         |        "checksum": "${ROWCsvtoXml.computeHash(s"src/test/resources/$orgFileName")}",
         |        "filename": "unused",
         |        "filetype": ".xml"
         |    }
         |}
         |""".stripMargin
    print(payload)
    val resp: WSResponse = PUT("row/AEOData", Json.parse(payload))
    resp should have('status(202))
    val directory = new Directory(new File(s"src/test/resources/$orgFileName"))
    directory.deleteRecursively()
  }

  def failureRowCsvToXmlConverter(orgFileName: String): Unit = {
    val src = scala.io.Source.fromURL(s"${testConfig.url}/download/$orgFileName")
    val out = new java.io.FileWriter(s"src/test/resources/$orgFileName")
    out.write(src.mkString)
    out.close
    val payload =
      s"""
         |{
         |    "download": {
         |        "url": "${testConfig.url}/download/$orgFileName",
         |        "checksum": "${ROWCsvtoXml.computeHash(s"src/test/resources/$orgFileName")}",
         |        "filename": "unused",
         |        "filetype": ".xml"
         |    }
         |}
         |""".stripMargin
    val resp: WSResponse = PUT("row/AEOData", Json.parse(payload))
    try{
      resp should have('status(400))
    }
      catch{
        case e: FileNotFoundException => print("Couldn't find that file.")
        case e: IOException => print("Had an IOException trying to read that file")
      }

    val directory = new Directory(new File(s"src/test/resources/$orgFileName"))
    directory.deleteRecursively()
  }

  def computeHash(path: String): String = {
    val buffer = new Array[Byte](8192)
    val md5 = MessageDigest.getInstance("MD5")

    val dis = new DigestInputStream(new FileInputStream(new File(path)), md5)
    try {
      while (dis.read(buffer) != -1) {}
    } finally {
      dis.close()
    }

    md5.digest.map("%02x".format(_)).mkString
  }
}