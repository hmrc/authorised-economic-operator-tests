package uk.aeo.mra.data.test

import uk.aeo.mra.data.api.DataConverterApi

class converterTest extends AcceptanceTestSpec {


  feature("transformation from zip to csv file") {

    scenario("Happy path file transformation from zip to csv") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip", "27e41209098b9b6c11b997f0fd84586c", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml")
      When("CSV file is generated when file transformation is a success")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv")
      Then("user can see the results and ack files")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/result.xml")
    }

    scenario("Happy path differential file transformation from zip to csv") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("EU-AEO-differential-extraction-example.zip", "13a5bbed9aef2773ec086b24b2cb1a31", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml")
      When("CSV file is generated when file transformation is a success")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv")
      Then("user can see the results and ack files")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/result.xml")
    }

    scenario("Transformation from zip to csv, Failed due to Broken Schema") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("broken-EU-AEO-full-extraction-example.zip", "4ee87998d6042bf6ba75f122cf28a18e", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")

    }

    scenario("Transformation from zip to csv, Failed due to Invalid file type") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("EU-AEO-full-extraction-example.xml", "d0c368356cbe8bf41dd63b71dc980488", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")

    }

    scenario("Transformation from zip to csv, Failed due to Invalid file within the zip") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("garden.jpg.zip", "af4f472f4b0baa2af5665670e609d4d2", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")

    }

    scenario("Transformation from zip to csv, Failed due to Invalid checksum") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("garden.jpg.zip", "f4f472f4b0baa2af5665670e609d4d2", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")

    }

    scenario("Transformation from zip to csv, Failed due to Invalid payload") {
      Given("Transformation microservice triggered")
      DataConverterApi.invalidPayload("AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip", "27e41209098b9b6c11b997f0fd84586c", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")

    }
  }
}
