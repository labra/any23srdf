package es.weso.html2rdf
import es.weso.rdf.jena.RDFAsJenaModel
import org.scalatest._

class HTML2RDFTest extends FunSpec with Matchers {
  describe(s"Extract RDF data from HTML") {
    it(s"Should extract info from RDFa 1.1") {
    val htmlStr =
      """|<body prefix = "xsd: http://www.w3.org/2001/XMLSchema#"
         |      vocab = "http://schema.org/" >
         |<div resource="http://example.org/post" typeOf="Blog">
         |  <p>Post created
         |     <span content="2018-10-30"
         |           property="created"
         |           datatype="xsd:date">last saturday</span>.
         |  </p>
         |</div>
         |</body>
      """.stripMargin

    val expectedStr =
      """|prefix rdfa:   <http://www.w3.org/ns/rdfa#>
         |prefix schema: <http://schema.org/>
         |prefix xsd:    <http://www.w3.org/2001/XMLSchema#>
         |
         |<http://example.org>  rdfa:usesVocabulary schema: .
         |
         |<http://example.org/post> a schema:Blog ;
         |         schema:created "2018-10-30"^^xsd:date .
      """.stripMargin

    val rdf = HTML2RDF.htmlExtract(htmlStr)
    info(s"Model:\n${rdf.serialize("Turtle").getOrElse("")}")
    val r = for {
      expected <- RDFAsJenaModel.fromChars(expectedStr, "TURTLE")
      isIsomorphic <- rdf.isIsomorphicWith(expected)
    } yield isIsomorphic
    r.fold(
      e => fail(s"Error: $e"),
      b => b should be(true)
    )
  }

  it(s"Should extract info from Microdata") {
    val htmlStr =
      """|<div itemscope itemid="http://example.org/eliza">
         | <p>My name is <span itemprop="name">Elizabeth</span>.</p>
         |</div>""".stripMargin

    val expectedStr =
      """|prefix md:   <http://www.w3.org/1999/xhtml/microdata#>
         |prefix schema: <http://schema.org/>
         |prefix xsd:    <http://www.w3.org/2001/XMLSchema#>
         |prefix : <http://example.org/>
         |
         |<http://example.org>  md:item :eliza .
         |:eliza schema:name "Elizabeth" .
      """.stripMargin

    val rdf = HTML2RDF.htmlExtract(htmlStr)
    info(s"Model:\n${rdf.serialize("Turtle").getOrElse("")}")
    val r = for {
      expected <- RDFAsJenaModel.fromChars(expectedStr, "TURTLE")
      isIsomorphic <- rdf.isIsomorphicWith(expected)
    } yield isIsomorphic
    r.fold(
      e => fail(s"Error: $e"),
      b => b should be(true)
    )
  }
 }
}