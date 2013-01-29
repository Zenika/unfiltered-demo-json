package com.zenika.unfiltered.demo.server

import com.zenika.unfiltered.demo.plan.CocktailsPlan
import java.net.URL
	
object Server {
  def main(args: Array[String]) {
	unfiltered.jetty.Http.local(8080)
		.filter(new SayHelloFilter)
		.filter(new CocktailsPlan)
		.run
  }
}
