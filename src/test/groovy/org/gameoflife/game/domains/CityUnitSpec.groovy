package org.gameoflife.game.domains

import org.gameoflife.game.domains.Game.Coordinates

import spock.lang.Specification

class CityUnitSpec extends Specification {
	
	def "Should init a map with 2 streets, 2 citizens in each one, all them died"(){
		when:
			City city = new City(2, 2)
			
		then:
			city.street(0).citizen(0).position == new Tuple2(0,0)
			city.street(0).citizen(1).position == new Tuple2(0,1)
			city.street(1).citizen(0).position == new Tuple2(1,0)
			city.street(1).citizen(1).position == new Tuple2(1,1)
	}
	
	def "Should init a map with 2 streets, 2 citizens in each one, with 2 citizens alive"(){
		when:
			City city = new City(2, 2, [new Coordinates(y:0, x:1), new Coordinates(y:1, x:0)])
			
		then:
			city.street(0).citizen(0).position == new Tuple2(0,0)
			city.street(0).citizen(1).position == new Tuple2(0,1)
			city.street(1).citizen(0).position == new Tuple2(1,0)
			city.street(1).citizen(1).position == new Tuple2(1,1)
			
		and:
			city.street(0).citizen(0).isDead()
			city.street(0).citizen(1).isAlive()
			city.street(1).citizen(0).isAlive()
			city.street(1).citizen(1).isDead()
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
