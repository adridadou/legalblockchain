package org.adridadou.clause

import java.io.InputStreamReader

import org.adridadou.fields._
import org.yaml.snakeyaml.Yaml
import sun.jvm.hotspot.oops.DefaultOopVisitor

import scala.util.{Failure, Success, Try}
import scala.collection.JavaConversions._

/**
  * Created by davidroon on 05.06.16.
  * This code is released under Apache 2 license
  */
class ClauseParser {

  def parse(input:InputStreamReader): Try[Clause] = {
    val parser = new Yaml()
    parser.load(input) match {
      case map:java.util.Map[String,String] => toClause(map.toMap)
      case _ => Failure(ParsingException("error while parsing the clause file"))
    }
  }

  private def toClause(map:Map[String,Any]):Try[Clause] = {
    val parameterSection = map("parameters").asInstanceOf[java.util.Map[String,Any]].toMap
    val text = map("text").asInstanceOf[String]

    val parameters = parameterSection.map({case (name,value) =>
      val options = value.asInstanceOf[java.util.Map[String,String]].toMap
      toContractField(options("type")).map(name -> _) match {
        case Some(v) => v
        case None => throw new IllegalArgumentException("unknown type " + options("type") + " for parameter " + name)
      }
    })

    Success(Clause(text,parameters))
  }

  private def toContractField(value:String):Option[ContractField] = value match {
    case Address.id => Some(Address)
    case FullName.id => Some(FullName)
    case ContractDate.id => Some(ContractDate)
    case DollarPrice.id => Some(DollarPrice)
    case _ => None
  }
}

case class ParsingException(msg:String) extends Exception
