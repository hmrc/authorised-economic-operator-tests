package uk.aeo.mra.data.api

import play.api.libs.json.Json
import play.api.libs.ws.WSResponse

object DataConverterApi extends Api {

  override def serviceName: String = "aeo-data-converter"

  def aeoMraDataConverter(orgFileName: String, resultCSV: String, ackXml: String, resultXml: String, orgXml: String, url: String): Unit = {
    val payload =
      s"""
         |{
         |    "serviceReferenceNumber": "123456789012",
         |    "download": {
         |        "url": "$url/download/$orgFileName",
         |        "checksum": "27e41209098b9b6c11b997f0fd84586c",
         |        "filename": "$orgFileName",
         |        "filetype": "xml"
         |    },
         |    "upload": {
         |        "url": "$url/upload-multi-part/$resultCSV",
         |        "filename": "$resultCSV"
         |    },
         |    "ack": {
         |       "url": "$url/upload-multi-part/$ackXml",
         |       "filename": "$ackXml"
         |    },
         |    "result":{
         |      "url": "$url/upload-multi-part/$resultXml",
         |      "filename": "$resultXml"
         |    },
         |    "original": {
         |        "url": "$url/upload-multi-part/$orgXml",
         |        "filename": "$orgXml"
         |    }
         |}
         |""".stripMargin

    val resp: WSResponse = POST("notifySourceReady", Json.parse(payload))
    resp should have('status(202))
    print(payload)
  }

  def downloadFile(path: String): Unit = {
    val response = GET(path)
    response.status should (equal(200) or equal(204))
    response
  }

}