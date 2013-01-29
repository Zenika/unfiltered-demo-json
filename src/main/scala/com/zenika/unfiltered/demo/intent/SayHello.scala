package com.zenika.unfiltered.demo.intent

import unfiltered.request._
import unfiltered.response._

object NonEmptyName extends Params.Extract("name",Params.first ~> Params.nonempty)

object SayHello {

	val intent = unfiltered.Cycle.Intent[Any, Any] {
	  
		case req@Path(Seg("hello" :: name :: Nil)) => req match {
			// GET /hello/{name}
			case GET(_) => 
			  Ok ~> ResponseString("Hello " + name)
			case _ => MethodNotAllowed ~> ResponseString("Method must be GET")
		}
		case req@Path("/hello") => req match {
			// POST /hello --data name=$name
			case POST(_) & Params(NonEmptyName(name)) =>
				Ok ~> ResponseString("Hello " + name.toString)
			case POST(_) => 
				BadRequest ~> ResponseString("""The parameter "name" is missing""")
			case _ => MethodNotAllowed ~> ResponseString("Method must be POST")
		}
	}
}
