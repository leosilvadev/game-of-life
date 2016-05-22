package org.gameoflife.game.domains

import java.util.List;

import org.gameoflife.game.domains.Game.Coordinates;

class City {
	
	final List<Street> streets
	
	City(Integer streetsPerCity, Integer citizensPerStreet, List<Coordinates> firstCitizens=[]) {
		if (streetsPerCity<=0 || citizensPerStreet<=0)
			throw new IllegalArgumentException('A City must at least one street with one citizen!')
		
		streets = (0..<streetsPerCity).collect { streetNumber ->
			def firstCitizensOfTheStreet = firstCitizens.findAll { it.y == streetNumber }
			new Street(streetNumber, citizensPerStreet, firstCitizensOfTheStreet)
		}
	}
	
	void eachStreet(Closure action) {
		streets.eachWithIndex action
	}

	Street street(Integer number){
		streets.get(number)
	}
	
}
