package org.gameoflife.game.services

import groovy.json.JsonOutput

import org.gameoflife.Citizen
import org.gameoflife.game.domains.Game.Coordinates
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilderImpl

@Component
class GameOfLife {
	
	def start(Coordinates config, List<Coordinates> points, Closure notify){
		Citizen[][] map = new Citizen[config.x][config.y] 
		
		map.eachWithIndex { item, indexX ->
			item.eachWithIndex { citizen, indexY ->
				item[indexY] = new Citizen(indexX, indexY)
			}
		}
		
		map = linkCitizens(map)
		map = init(map, points)
		
		startGame(map, notify)
	}
	
	def startGame(Citizen[][] map, Closure notify){
		int i =0
		while(i<40) {
			def toToggle = []
			for ( int rowIndex = 0 ; rowIndex < map.size() ; rowIndex++ ) {
				def row = map[rowIndex]
				for ( int colIndex = 0 ; colIndex < row.size() ; colIndex++ ) {
					def citizen = row[colIndex]
					if ( citizen.mustToggle() ) {
						toToggle << citizen
					}
				}
			}
			toToggle.each { Citizen citizen -> citizen.toggleLife() }
			notify(map)
			i++
			Thread.sleep(500)
		}
	}
	
	def linkCitizens(Citizen[][] map){
		map.eachWithIndex { citizens, row ->
			if ( row == 0 ) {
				citizens.eachWithIndex { Citizen citizen, column ->
					if ( column==0 ) {
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row+1][column])
						citizen.addNeighbour(map[row+1][column+1])
						
					} else if ( column < citizens.size() -1 ) {
						citizen.addNeighbour(citizens[column-1])
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row+1][column])
						citizen.addNeighbour(map[row+1][column+1])
						citizen.addNeighbour(map[row+1][column-1])
						
					} else if ( column == citizens.size() -1 ) {
						citizen.addNeighbour(citizens[column-1])
						citizen.addNeighbour(map[row+1][column])
						citizen.addNeighbour(map[row+1][column-1])
						
					}
				}
				
			} else {
				citizens.eachWithIndex { Citizen citizen, column ->
					if ( column==0 ) {
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row-1][column])
						citizen.addNeighbour(map[row-1][column+1])
						if ( row < map.size()-1 ) {
							citizen.addNeighbour(map[row+1][column])
							citizen.addNeighbour(map[row+1][column+1])
						}
						
					} else if ( column < citizens.size() -1 ) {
						citizen.addNeighbour(citizens[column-1])
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row-1][column])
						citizen.addNeighbour(map[row-1][column+1])
						citizen.addNeighbour(map[row-1][column-1])
						if ( row < map.size()-1 ) {
							citizen.addNeighbour(map[row+1][column])
							citizen.addNeighbour(map[row+1][column+1])
							citizen.addNeighbour(map[row+1][column-1])
						}
						
					} else if ( column == citizens.size() -1 ) {
						citizen.addNeighbour(citizens[column-1])
						citizen.addNeighbour(map[row-1][column])
						citizen.addNeighbour(map[row-1][column-1])
						if ( row < map.size()-1 ) {
							citizen.addNeighbour(map[row+1][column])
							citizen.addNeighbour(map[row+1][column-1])
						}
					}
				}
			}
		}
		map
	}
	
	def init(Citizen[][] map, List<Coordinates> points) {
		def result = map.clone()
		points.each { 
			result[it.x-1][it.y-1].live()
		}
		result
	}

}
