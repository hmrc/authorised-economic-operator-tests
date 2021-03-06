package uk.aeo.mra.data.api

import com.mongodb.{BasicDBObject, DBCollection, DBCursor, DBObject}
import com.mongodb.casbah.{MongoClient, MongoClientURI, MongoDB}
import com.mongodb.util.JSON
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

  def connectAEOMongoDB():  Int ={

    val aeodb : String = "authorised-economic-operator-data-converter"
    val aeocollection : String = "aeo_mra_message"

//    val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017/")
//    val database: MongoDB = mongoClient.getDB(aeodb)
//    val collection: DBCollection = database.getCollection(aeocollection)

    val mongo_url = MongoClientURI("mongodb://localhost:27017/")
    val mongoClient: MongoClient = MongoClient(mongo_url)
    val db = mongoClient(aeodb)
    val collection: DBCollection = db.getCollection(aeocollection)
//    val collection = db(aeocollection)



    val jsonString = """{ "source" : "EU", "target" : "GB","messageId" : "00000000-0000-0000-0000-000000000000","sequenceNumber" : 0}"""
    val dbObject: DBObject = JSON.parse(jsonString).asInstanceOf[DBObject]
//    val buffer = new java.util.ArrayList[DBObject]()
//    buffer.add(dbObject)

    println("Collection Starts **********************************************************")

//    val cursor = collection.find
//    while ( {
//      cursor.hasNext
//    })println(cursor.next)

//    /**** Retrieve only the specified fields *****/
//    val allQuery : BasicDBObject = new BasicDBObject()
//    val fields : BasicDBObject = new BasicDBObject()
//    fields.put("messageId", "1")
//    val cursor = collection.find(allQuery, fields)


//    /**** Retrieve based on only one where condition *****/
//    val whereQuery : BasicDBObject = new BasicDBObject
//    whereQuery.append("source", "ROW")
//    val cursor = collection.find(whereQuery)


//    /**** Retrieve based on more than one where condition *****/
    val andQuery : BasicDBObject = new BasicDBObject()
    import java.util
    val obj  = new util.ArrayList[BasicDBObject]
    obj.add(new BasicDBObject("messageId", "00000000-0000-0000-0000-000000000000"))
    obj.add(new BasicDBObject("sequenceNumber", 0))
    obj.add(new BasicDBObject("source", "EU"))
    andQuery.put("$and", obj)

    println("andQuery Below : ")
    println(andQuery.toString)

    val cursor = collection.find(andQuery).count()

    println(cursor)

//    while ( {
//      cursor.hasNext
//    }) System.out.println(cursor.next)


    println("Collection Ends ************************************************************")
    cursor
  }

  def sequence_messageId_persisted(): Unit={
    if(connectAEOMongoDB().equals(1)){
      println("Sequence and Message ID is Stored in MongoDB!!!")
    }
    else{
      println("Sequence Number and Message ID is NOT Stored in MongoDB !!!")
    }


  }
}
