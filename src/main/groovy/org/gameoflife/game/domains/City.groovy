package org.gameoflife.game.domains

class City {
	
	final List<Street> streets
	
	City(Integer streetsPerCity, Integer citizensPerStreet) {
		if (streetsPerCity<=0 || citizensPerStreet<=0)
			throw new IllegalArgumentException('A City must at least one street with one citizen!')
		
		Integer streetNumber = 0
		streets = (0..<streetsPerCity).collect {
			def street = new Street(streetNumber, citizensPerStreet)
			streetNumber++
			street
		}
	}
	
	void eachStreet(Closure action) {
		streets.eachWithIndex action
	}

	Street street(Integer number){
		streets.get(number)
	}
	
}
