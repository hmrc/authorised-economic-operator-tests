package uk.aeo.mra.data.api

import play.api.libs.json.Json
import play.api.libs.ws.WSResponse

object DataConverterApi extends Api {

  override def serviceName: String = "aeo-data-converter"

  def aeoMraDataConverter(orgFileName: String, checkSum: String, resultCSV: String, ackXml: String, resultXml: String, orgXml: String): Unit = {
    val payload =
      s"""
         |{
         |    "serviceReferenceNumber": "123456789012",
         |    "download": {
         |        "url": "${testConfig.url}/download/$orgFileName",
         |        "checksum": "$checkSum",
         |        "filename": "$orgFileName",
         |        "filetype": "xml"
         |    },
         |    "upload": {
         |        "url": "${testConfig.url}/upload-multi-part/$resultCSV",
         |        "filename": "$resultCSV"
         |    },
         |    "ack": {
         |       "url": "${testConfig.url}/upload-multi-part/$ackXml",
         |       "filename": "$ackXml"
         |    },
         |    "result":{
         |      "url": "${testConfig.url}/upload-multi-part/$resultXml",
         |      "filename": "$resultXml"
         |    },
         |    "original": {
         |        "url": "${testConfig.url}/upload-multi-part/$orgXml",
         |        "filename": "$orgXml"
         |    }
         |}
         |""".stripMargin

    val resp: WSResponse = POST("notifySourceReady", Json.parse(payload))
    if (resp.status==(202)) {
      resp should have('status(202))
      print(resp.body)
      print("Test is successful")
    }
    else {
      resp.status should (equal(400) or equal(500))
      print(resp.body)
    }
  }

  def invalidPayload(orgFileName: String, checkSum: String, resultCSV: String, ackXml: String, resultXml: String): Unit = {
    val payload =
      s"""
         |{
         |    "serviceReferenceNumber": "123456789012",
         |    "download": {
         |        "url": "${testConfig.url}/download/$orgFileName",
         |        "checksum": "$checkSum",
         |        "filename": "$orgFileName",
         |        "filetype": "xml"
         |    },
         |    "upload": {
         |        "url": "${testConfig.url}/upload-multi-part/$resultCSV",
         |        "filename": "$resultCSV"
         |    },
         |    "ack": {
         |       "url": "${testConfig.url}/upload-multi-part/$ackXml",
         |       "filename": "$ackXml"
         |    },
         |    "result":{
         |      "url": "${testConfig.url}/upload-multi-part/$resultXml",
         |      "filename": "$resultXml"
         |    }
         |}
         |""".stripMargin

    val resp: WSResponse = POST("notifySourceReady", Json.parse(payload))
    resp.status should (equal (400))
    print(resp.body)
  }


  def downloadFile(path: String): Unit = {
    val response = GET(path)
    response.status should (equal(200) or equal(204))
  }
}
