package uk.aeo.mra.data.api

import com.mongodb.{BasicDBObject, DBCollection, DBCursor, DBObject}
import com.mongodb.casbah.{MongoClient, MongoClientURI, MongoDB}
import com.mongodb.util.JSON
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import java.util

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

  def connectAEOMongoDB(): DBCollection={
    val aeodb : String = "authorised-economic-operator-data-converter"
    val aeocollection : String = "aeo_mra_message"
    val mongo_url = MongoClientURI(testConfig.mongoUrl)
    val mongoClient: MongoClient = MongoClient(mongo_url)
    val db = mongoClient(aeodb)
    db.getCollection(aeocollection)
  }

  def retriveDetailsFromMongoDB():  Int ={
    val query : BasicDBObject = new BasicDBObject()
    val obj  = new util.ArrayList[BasicDBObject]
    obj.add(new BasicDBObject("messageId", "00000000-0000-0000-0000-000000000000"))
    obj.add(new BasicDBObject("sequenceNumber", 0))
    obj.add(new BasicDBObject("source", "EU"))
    query.put("$and", obj)
    connectAEOMongoDB().find(query).count()
  }

  def verify_sequence_messageId_persisted(): Unit={
    if(retriveDetailsFromMongoDB().equals(1)){
      println("Sequence Number and Message IDs are Stored in MongoDB!!!")
    }
    else{
      println("Sequence Number and Message IDs are NOT Stored in MongoDB !!!")
    }


  }
}
