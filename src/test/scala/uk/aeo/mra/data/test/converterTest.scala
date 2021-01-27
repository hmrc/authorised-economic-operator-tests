package uk.aeo.mra.data.test

import uk.aeo.mra.data.api.DataConverterApi

class converterTest extends AcceptanceTestSpec {


  feature("Sending a message for GMC service") {

    scenario("Happy path file transformation from zip to csv") {
      Given("Transformation microservice triggered")
      DataConverterApi.aeoMraDataConverter("AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv", "ack.xml", "result.xml", "AEO_MRA_EU_UK_2020-11-10T14.36.75Z.zip_2611.xml", testConfig.url)
      When("CSV file is generated when file transformation is a success")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/AEO_MRA_EU_UK_2020-11-10T14.36.75Z.csv")
      Then("user can see the results and ack files")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/ack.xml")
      DataConverterApi.downloadFile(s"${testConfig.url}/download/result.xml")
    }
  }
}
