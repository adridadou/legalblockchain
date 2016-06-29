package org.adridadou.clause

import org.adridadou.fields._


/**
  * Created by davidroon on 05.06.16.
  * This code is released under Apache 2 license
  */
case class Clause (markdown: String, parameters:Map[String,ContractField])


import org.parboiled2._

class ClauseParserParboiled (val input: ParserInput, val parameters:Map[String,ContractField]) extends Parser with StringBuilding{
  val openS = "[["
  val closeS = "]]"

  def clause: Rule1[Clause] = rule { Parameters ~ Text ~ EOI ~> ((parameters:Map[String,ContractField], text:String) => Clause(text, parameters))}

  def Parameter:Rule1[(String,ContractField)] = rule {
    ID ~ ':' ~ ID ~> ((id:String,id2:String) => createParameterPair(id,id2))
  }

  def Parameters: Rule1[Map[String,ContractField]] = rule {
    oneOrMore(Parameter).separatedBy('\n') ~ "###" ~> ((seq:Seq[(String, ContractField)]) => seq.toMap)
  }

  def ID:Rule1[String] = rule {
    oneOrMore(CharPredicate.AlphaNum) ~ push(sb.toString)
  }

  def Text: Rule1[String] = rule {
    oneOrMore(CharPredicate.All) ~push(sb.toString)
  }

  private def createParameterPair(name:String, parameterType:String):(String,ContractField) = name -> toParameterType(parameterType)

  private def toParameterType(name:String):ContractField = name match {
    case "FullName" => FullName
    case "Address" => Address
    case "Date" => ContractDate
    case _ => Unknown(name)
  }
}