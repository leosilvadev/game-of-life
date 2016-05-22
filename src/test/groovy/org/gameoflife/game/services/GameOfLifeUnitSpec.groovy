package org.gameoflife.game.services

import org.gameoflife.game.domains.City

import spock.lang.Specification

class GameOfLifeUnitSpec extends Specification {

	def "Should link the citizens from a city with 3 streets and 3 citizens in each one"(){
		given:
			def city = new City(3, 3)
			
		and:
			def data = [city:city]
			
		and:
			def gameOfLife = new GameOfLife()
			
		when:
			def result = gameOfLife.link(data)
			
		then:
			city.street(0).citizen(0).neighbours.size() == 3
			city.street(0).citizen(1).neighbours.size() == 5
			city.street(0).citizen(2).neighbours.size() == 3
			
		and:
			city.street(1).citizen(0).neighbours.size() == 5
			city.street(1).citizen(1).neighbours.size() == 8
			city.street(1).citizen(2).neighbours.size() == 5
			
		and:
			city.street(2).citizen(0).neighbours.size() == 3
			city.street(2).citizen(1).neighbours.size() == 5
			city.street(2).citizen(2).neighbours.size() == 3
	}
	
}
