package uk.aeo.mra.data.test

import uk.aeo.mra.data.api.ROWCsvtoXml

class csvToXmlRow extends AcceptanceTestSpec{

  feature("Rest of World CSV to XML Conversion Test") {

    scenario("Happy path file transformation from zip to csv") {
      Given("Transformation microservice triggered")
      ROWCsvtoXml.successRowCsvToXmlConverter("fixed_20210101_Japan_AEO_list_anonymized.csv")
    }

    scenario("Unhappy path file transformation from zip to csv, Unsupported Status") {
      Given("Transformation microservice triggered")
      ROWCsvtoXml.failureRowCsvToXmlConverter("broken_20210101_Japan_AEO_list_anonymized.csv")
    }
  }
}
