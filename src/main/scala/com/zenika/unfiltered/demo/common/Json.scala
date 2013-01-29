package com.zenika.unfiltered.demo.common

import unfiltered.request._
import net.liftweb.json.JsonAST._

class JsonMapper[A](f: Option[JValue] => Option[A]) extends (Option[JValue] => Option[A]) {
    def apply(oj: Option[JValue]) = f(oj)
}

object JsonBody {
  implicit val formats = net.liftweb.json.DefaultFormats
    
  class Extract[A](f: JsonMapper[A]) {
    
    def this( )(implicit mf: Manifest[A]) =
      this( new JsonMapper[A]( _.map(_.extractOpt[A]).getOrElse(None))   )
          
    def unapply[T](req: HttpRequest[T]): Option[A] = 
    	f( unfiltered.request.JsonBody(req) )   
  }
}

object JsonObject {
	implicit val formats = net.liftweb.json.DefaultFormats
	
	def apply[T](jvalue: Option[JValue])(implicit mf: Manifest[T]):  Option[T] =
		jvalue.map(json => json.extractOpt[T]).getOrElse(None)
}
