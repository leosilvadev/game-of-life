package org.gameoflife.game.domains

import groovy.lang.Closure;

class Street {
	
	final List<Citizen> citizens
	
	Street(Integer street, Integer citizensPerStreet){
		citizens = (0..<citizensPerStreet).collect { houseNumber ->
			new Citizen(street, houseNumber)
		}
	}

	def eachCitizen(Closure action) {
		citizens.eachWithIndex action
	}
	
	Citizen citizen(Integer houseNumber){
		citizens.get(houseNumber)
	}
	
}
