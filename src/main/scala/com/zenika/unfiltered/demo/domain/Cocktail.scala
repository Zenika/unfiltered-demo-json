package com.zenika.unfiltered.demo.domain

object Cocktail {
	  type Ingredient = String
	  type Quantity = String
}
case class Cocktail(
    val name: String, 
    val recipe: String,
    val ingredients: List[(Cocktail.Ingredient, Cocktail.Quantity)]
)

