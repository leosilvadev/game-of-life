package org.gameoflife.game.services

import java.util.concurrent.TimeUnit

import org.gameoflife.game.domains.City

import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

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
	
	def "Should start a new game with 40 streets and 40 citizens on each one, run 50 cicles and the final citizens must be ok"(){
		def currentCicle = new BlockingVariable(30, TimeUnit.SECONDS)
		currentCicle.set 0
		
		given:
			City city = new City(40, 40)
			city.street(1).citizen(2).live()
			city.street(2).citizen(3).live()
			city.street(3).citizen(3).live()
			city.street(3).citizen(2).live()
			city.street(3).citizen(1).live()
			
		and:
			def notify = { currentCicle.set(currentCicle.get()+1) }
			
		and:
			def data = [city:city, cicles:50, notify:notify]
			
		and:
			def gameOfLife = new GameOfLife()
			
		when:
			gameOfLife.link(data)
		
		and:
			gameOfLife.start(data)
			
		then:
			currentCicle.get() == 50 
			
		and:
			city.street(15).citizen(13).isAlive()
			city.street(14).citizen(15).isAlive()
			city.street(15).citizen(15).isAlive()
			city.street(16).citizen(15).isAlive()
			city.street(16).citizen(14).isAlive()
			
	}
	
}
