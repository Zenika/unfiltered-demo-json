package com.zenika.unfiltered.demo.server

import com.zenika.unfiltered.demo.intent.SayHello

class SayHelloFilter extends 
	unfiltered.filter.Planify(SayHello.intent)