package org.adridadou.fields

/**
  * Created by davidroon on 05.06.16.
  * This code is released under Apache 2 license
  */
case class Unknown(name:String) extends ContractField {
  override def id: String = name
}
