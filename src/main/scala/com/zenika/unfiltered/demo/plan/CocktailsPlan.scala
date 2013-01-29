package com.zenika.unfiltered.demo.plan

import unfiltered.request._
import unfiltered.response._
import unfiltered.request.QParams._

import com.zenika.unfiltered.demo.domain.Cocktail
import com.zenika.unfiltered.demo.common.{JsonBody, JsonMapper}
import com.zenika.unfiltered.demo.representation.CocktailRepresentation._

import net.liftweb.json.JsonAST.{JValue, JObject, JField}
import net.liftweb.json.JsonDSL._
import net.liftweb.json._


class CocktailsPlan extends unfiltered.filter.Plan {
    
  @volatile private var repository: scala.collection.mutable.Map[Int, Cocktail] = scala.collection.mutable.Map.empty
      
  // A specific extractor to map a JsonBody to a Cocktail object.
  object CocktailBody extends JsonBody.Extract[Cocktail]( new JsonMapper((o : Option[JValue]) => {
      	 implicit val formats = net.liftweb.json.DefaultFormats
	     o.map( _.transform {
	     		case JObject( List(JField("name", n), JField("quantity", q) ) ) => 
		            JObject( List(JField("_1", n) , JField("_2", q)) )
	          }
	     .extractOpt[Cocktail]).getOrElse(None)    
  }))
  
  def intent = {
    case req@ Path( Seg( "cocktails" :: id :: Nil ) ) => req match {
      
      // GET /cocktails/{id} --header "Accept: application/json"
      case GET(_) => req match {        
        case Accepts.Json(_) => 
    	  repository.get(id.toInt).map( 
    	      (c: Cocktail) =>  Ok ~> Json(c) ) getOrElse{ NotFound ~> ResponseString("resource not found") }       
        case _ => NotAcceptable ~> ResponseString("You must accept application/json")
      }
      // PUT /cocktails/{id} --header "Content-Type: application/json"
      case PUT(_) & RequestContentType(ct) => ct match {
        case "application/json" => req match {
    	  		case  CocktailBody(c) => {
    	  			if( repository.contains(id.toInt)) {
    	  			  repository.update(id.toInt, c)
    	  			  Ok ~> ResponseString("The cocktail has been successfully updated")
    	  			}
    	  			else NotFound ~> ResponseString("resource not found")
    	  		}
    	  		case _ => BadRequest ~> ResponseString("data must be valid application/json")
    	    }
        case _ => UnsupportedMediaType ~> ResponseString("content-type must be application/json")
      }     
      // DELETE /cocktails/{id}
      case DELETE(_)  =>
      	  repository.remove(id.toInt).map(
      	      (c: Cocktail) => Ok ~> ResponseString("The cocktail has been successfully deleted"))
      	      .getOrElse{ NotFound ~> ResponseString("resource not found") }
       
      // ERROR
      case _ => MethodNotAllowed ~> ResponseString("method MUST be GET, PUT or DELETE")
    }
 
    case req@ Path("/cocktails") => req match {
      // GET /cocktails --header "Accept: application/json"
      case GET(_) => req match {
        case Accepts.Json(_) => Ok ~> Json(repository.values.toSeq)
        case _ => NotAcceptable ~> ResponseString("You must accept application/json")
      }
      // POST /cocktails --header "Content-Type: application/json"
      case POST(_) & RequestContentType(ct) => ct match {
        case "application/json" => req match {
          case  CocktailBody(c) => {
            repository +=  (repository.lastOption.map(x => x._1).getOrElse(0) + 1) -> c
            Created ~> ResponseString("The cocktail has been successfully created")
            
          }
          case _ => BadRequest ~> ResponseString("Invalid json data")
        }
      }  
      case _ => MethodNotAllowed ~> ResponseString("method MUST be GET or POST")
    }
  }
}