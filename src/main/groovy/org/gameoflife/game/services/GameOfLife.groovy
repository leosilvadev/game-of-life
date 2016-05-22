package org.gameoflife.game.services

import org.gameoflife.game.domains.Citizen
import org.gameoflife.game.domains.City
import org.gameoflife.game.domains.Game
import org.gameoflife.game.domains.Street
import org.gameoflife.game.flow.Flow
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GameOfLife {
	
	@Async
	void start(Game game, Closure notify, Closure finish){
		City city = new City(game.config.y, game.config.x) 
		
		def recoveryFn = { ex -> 
			ex.printStackTrace()
			finish()
		}
		def inputData = [city:city, points:game.initialPoints, cicles:game.cicles, delay:game.delay, notify:notify]
		
		Flow.waterfall([link, bootstrap, start, finish], recoveryFn, inputData)
	}
	
	def link = { data ->
		City city = data.city
		city.eachStreet { Street street, streetNumber ->
			def nextStreet = streetNumber + 1
			def lastStreet = streetNumber - 1
			if ( streetNumber == 0 ) {
				street.eachCitizen { Citizen citizen, houseNumber ->
					def nextCitizen = houseNumber + 1
					def lastCitizen = houseNumber - 1
					if ( isTheFirstOfTheStreet(houseNumber) ) {
						citizen.addNeighbour(city.street(nextStreet).citizen(nextCitizen))
						citizen.addNeighbour(city.street(nextStreet).citizen(houseNumber))
						citizen.addNeighbour(city.street(nextStreet).citizen(nextCitizen))
						
					} else if ( isNeitherFirstOrLastColumn(houseNumber, street) ) {
						citizen.addNeighbour(street.citizen(lastCitizen))
						citizen.addNeighbour(street.citizen(nextCitizen))
						citizen.addNeighbour(city.street(nextStreet).citizen(houseNumber))
						citizen.addNeighbour(city.street(nextStreet).citizen(nextCitizen))
						citizen.addNeighbour(city.street(nextStreet).citizen(lastCitizen))
						
					} else if ( isTheLastOfTheStreet(houseNumber, street) ) {
						citizen.addNeighbour(street.citizen(lastCitizen))
						citizen.addNeighbour(city.street(nextStreet).citizen(houseNumber))
						citizen.addNeighbour(city.street(nextStreet).citizen(lastCitizen))
						
					}
				}
				
			} else {
				street.eachCitizen { Citizen citizen, houseNumber ->
					def nextCitizen = houseNumber + 1
					def lastCitizen = houseNumber - 1
					if ( isTheFirstOfTheStreet(houseNumber) ) {
						citizen.addNeighbour(street.citizen(nextCitizen))
						citizen.addNeighbour(city.street(lastStreet).citizen(houseNumber))
						citizen.addNeighbour(city.street(lastStreet).citizen(nextCitizen))
						if ( isNotTheLastOfTheCity(streetNumber, city.streets) ) {
							citizen.addNeighbour(city.street(nextStreet).citizen(houseNumber))
							citizen.addNeighbour(city.street(nextStreet).citizen(nextCitizen))
						}
						
					} else if ( isNeitherFirstOrLastColumn(houseNumber, street) ) {
						citizen.addNeighbour(street.citizen(lastCitizen))
						citizen.addNeighbour(street.citizen(nextCitizen))
						citizen.addNeighbour(city.street(lastStreet).citizen(houseNumber))
						citizen.addNeighbour(city.street(lastStreet).citizen(nextCitizen))
						citizen.addNeighbour(city.street(lastStreet).citizen(lastCitizen))
						if ( isNotTheLastOfTheCity(streetNumber, city.streets) ) {
							citizen.addNeighbour(city.street(nextStreet).citizen(houseNumber))
							citizen.addNeighbour(city.street(nextStreet).citizen(nextCitizen))
							citizen.addNeighbour(city.street(nextStreet).citizen(lastCitizen))
						}
						
					} else if ( isTheLastOfTheStreet(houseNumber, street) ) {
						citizen.addNeighbour(street.citizen(lastCitizen))
						citizen.addNeighbour(city.street(lastStreet).citizen(houseNumber))
						citizen.addNeighbour(city.street(lastStreet).citizen(lastCitizen))
						if ( isNotTheLastOfTheCity(streetNumber, city.streets) ) {
							citizen.addNeighbour(city.street(nextStreet).citizen(houseNumber))
							citizen.addNeighbour(city.street(nextStreet).citizen(lastCitizen))
						}
					}
				}
			}
		}
		data
	}
	
	def start = { data ->
		0.upto(data.cicles) {
			def citizensToChange = []
			data.city.eachStreet { street, streetNumber ->
				street.eachCitizen { citizen, houseNumber ->
					if ( citizen.mustChange() ) citizensToChange << citizen 
				}
			}
			
			data.notify(data.city)
			citizensToChange.each { it.liveOrDie() }
			sleep(data.delay)
		}
		data
	}
	
	def isNotTheLastOfTheCity(streetNumber, streets){
		streetNumber < streets.size()-1
	}
	
	def isTheFirstOfTheStreet(houseNumber) {
		houseNumber==0
	}
	
	def isNeitherFirstOrLastColumn(Integer houseNumber, Street street) {
		houseNumber < street.citizens.size() -1
	}
	
	def isTheLastOfTheStreet(Integer houseNumber, Street street) {
		houseNumber == street.citizens.size() -1
	}
	
	def bootstrap = { data ->
		data.points.each { point ->
			data.city.street(point.y).citizen(point.x).live()
		}
		data
	}

}
