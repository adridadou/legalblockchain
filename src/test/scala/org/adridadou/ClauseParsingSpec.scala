package org.adridadou

import org.adridadou.clause.ClauseParser
import org.adridadou.markdown.{ClauseValidation, ValidationSuccess}
import org.scalatest._

import scala.io.Source
/**
  * Created by davidroon on 05.06.16.
  * This code is released under Apache 2 license
  */
class ClauseParsingSpec extends FlatSpec with Matchers {

  "Clause Parser" should "read a file and create a clause object" in {
    val clauseSource = Source.fromFile("src/test/resources/JustATest.clause").reader()
    val parser = new ClauseParser
    val result = parser.parse(clauseSource).get

    new ClauseValidation().validate(result) should be (ValidationSuccess)
  }

  "Clause Parser" should "read this actual clause" in {
    val clauseSource = Source.fromFile("src/test/resources/legaldomain/convertible_promissory_note_preamble.clause").reader()
    val parser = new ClauseParser
    val result = parser.parse(clauseSource).get

    println(result.parameters)

    new ClauseValidation().validate(result) should be (ValidationSuccess)
  }
}
