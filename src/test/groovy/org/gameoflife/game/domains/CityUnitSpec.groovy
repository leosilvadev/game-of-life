package org.gameoflife.game.domains

import org.gameoflife.game.domains.Citizen
import org.gameoflife.game.domains.City;

import spock.lang.Specification

class CityUnitSpec extends Specification {
	
	def "Should init a map with two streets, two citizens in each one"(){
		when:
			City city = new City(2, 2)
			
		then:
			city.street(0).citizen(0).position == new Tuple2(0,0)
			city.street(0).citizen(1).position == new Tuple2(0,1)
			city.street(1).citizen(0).position == new Tuple2(1,0)
			city.street(1).citizen(1).position == new Tuple2(1,1)
	}
	
	def "Should fail when trying to create a city without citizen"(){
		when:
			new City(1, 0)
			
		then:
			thrown(IllegalArgumentException)
	}
	
	def "Should fail when trying to create a city without street"(){
		when:
			new City(0, 1)
			
		then:
			thrown(IllegalArgumentException)
	}

}
