package org.gameoflife.game.domains

import org.gameoflife.game.domains.Game.Coordinates

class Street {
	
	final List<Citizen> citizens
	
	Street(Integer street, Integer citizensPerStreet, List<Coordinates> firstCitizens=[]){
		citizens = (0..<citizensPerStreet).collect { houseNumber ->
			def firstCitizen = firstCitizens.find { it.x == houseNumber }
			def citizen = new Citizen(street, houseNumber)
			firstCitizen ? citizen.live() : citizen
		}
	}

	def eachCitizen(Closure action) {
		citizens.eachWithIndex action
	}
	
	Citizen citizen(Integer houseNumber){
		citizens.get(houseNumber)
	}
	
}
