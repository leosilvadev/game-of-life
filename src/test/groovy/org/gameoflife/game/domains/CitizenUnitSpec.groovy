package org.gameoflife.game.domains

import spock.lang.Specification

class CitizenUnitSpec extends Specification {
	
	def "A dead citizen that has 3 neighbours alive must live"(){
		given:
			def neighbours = [
				new Citizen(0, 0).live(),
				new Citizen(0, 1).live(),
				new Citizen(0, 2).live(),
				new Citizen(1, 0).die(),
				new Citizen(1, 2).die(),
				new Citizen(2, 0).die(),
				new Citizen(2, 1).die(),
				new Citizen(2, 2).die()
			]
			
		and:
			def citizen = new Citizen(1, 1, false, neighbours)
			
		when:
			def alive = citizen.mustChange()
			
		then:
			alive
	}
	
	def "An alive citizen that has 2 neighbours alive must live"(){
		given:
			def neighbours = [
				new Citizen(0, 0).live(),
				new Citizen(0, 1).live(),
				new Citizen(0, 2).die(),
				new Citizen(1, 0).die(),
				new Citizen(1, 2).die(),
				new Citizen(2, 0).die(),
				new Citizen(2, 1).die(),
				new Citizen(2, 2).die()
			]
			
		and:
			def citizen = new Citizen(1, 1, true, neighbours)
			
		when:
			def die = citizen.mustChange()
			
		then:
			!die
	}
	
	def "An alive citizen that has more than 3 neighbours alive must die"(){
		given:
			def neighbours = [
				new Citizen(0, 0).live(),
				new Citizen(0, 1).live(),
				new Citizen(0, 2).live(),
				new Citizen(1, 0).die(),
				new Citizen(1, 2).die(),
				new Citizen(2, 0).die(),
				new Citizen(2, 1).die(),
				new Citizen(2, 2).die()
			]
			
		and:
			def citizen = new Citizen(1, 1, true, neighbours)
			
		when:
			def alive = citizen.mustChange()
			
		then:
			!alive
	}
	
	def "An alive citizen that has less than 2 neighbours alive must die"(){
		given:
			def neighbours = [
				new Citizen(0, 0).live(),
				new Citizen(0, 1).die(),
				new Citizen(0, 2).die(),
				new Citizen(1, 0).die(),
				new Citizen(1, 2).die(),
				new Citizen(2, 0).die(),
				new Citizen(2, 1).die(),
				new Citizen(2, 2).die()
			]
			
		and:
			def citizen = new Citizen(1, 1, true, neighbours)
			
		when:
			def die = citizen.mustChange()
			
		then:
			die
	}

}
