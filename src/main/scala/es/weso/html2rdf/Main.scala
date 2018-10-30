package es.weso.html2rdf
import es.weso.html2rdf.HTML2RDF._
import es.weso.rdf.RDFReader

object Main {
  def main(args:Array[String]): Unit = {

    val htmlStr =
      """|<!DOCTYPE html>
       |<html lang="en">
       |  <head>
       |    <title>Example Document</title>
       |  </head>
       |  <body vocab="http://schema.org/">
       |    <p typeof="Blog">
       |      Welcome to my <a property="url" href="http://example.org/">blog</a>.
       |    </p>
       |  </body>
       |</html>
       | </html>
    """.stripMargin

    val microdataExample = """|<div itemscope itemtype="http://schema.org/SoftwareApplication">
                              |  <span itemprop="name">Angry Birds</span> -
                              |  <div itemprop="offers" itemscope itemtype="http://schema.org/Offer">
                              |    Price: $<span itemprop="price">1.00</span>
                              |    <meta itemprop="priceCurrency" content="USD" />
                              |  </div>
                              |</div>""".stripMargin

    println(s"RDFa---------------------->")
    val rdf1 = HTML2RDF.htmlExtract(microdataExample)
    println(s"RDF1: ${rdf1.serialize("TURTLE").getOrElse("")}")
    println(s"Microdata----------------------")
    val rdf2 = HTML2RDF.htmlExtract(htmlStr)
    println(s"RDF2: ${rdf2.serialize("TURTLE").getOrElse("")}")

  }
}
