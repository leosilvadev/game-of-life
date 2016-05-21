package org.gameoflife.game.services

import org.gameoflife.Citizen
import org.gameoflife.game.domains.Game
import org.gameoflife.game.domains.Game.Coordinates
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GameOfLife {
	
	@Async
	void start(Game game, Closure notify){
		Citizen[][] map = new Citizen[game.config.y][game.config.x] 
		
		init(map)
		linkCitizens(map)
		bootstrap(map, game.initialPoints)
		
		startGame(map, game.cicles, game.delay, notify)
	}
	
	def init(map){
		map.eachWithIndex { item, indexX ->
			item.eachWithIndex { citizen, indexY ->
				item[indexY] = new Citizen(indexX, indexY)
			}
		}
	}
	
	def startGame(Citizen[][] map, Integer cicles, Long delay, Closure notify){
		0.upto(cicles) {
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
			notify(map)
			toToggle.each { Citizen citizen -> citizen.toggleLife() }
			sleep(delay)
		}
	}
	
	def isFirstColumn(column) {
		column==0
	}
	
	def isNeitherFirstOrLastColumn(column, citizens) {
		column < citizens.size() -1
	}
	
	def isLastColumn(column, citizens) {
		column == citizens.size() -1
	}
	
	void linkCitizens(Citizen[][] map){
		map.eachWithIndex { citizens, row ->
			if ( row == 0 ) {
				citizens.eachWithIndex { Citizen citizen, column ->
					if ( isFirstColumn(column) ) {
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row+1][column])
						citizen.addNeighbour(map[row+1][column+1])
						
					} else if ( isNeitherFirstOrLastColumn(column, citizens) ) {
						citizen.addNeighbour(citizens[column-1])
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row+1][column])
						citizen.addNeighbour(map[row+1][column+1])
						citizen.addNeighbour(map[row+1][column-1])
						
					} else if ( isLastColumn(column, citizens) ) {
						citizen.addNeighbour(citizens[column-1])
						citizen.addNeighbour(map[row+1][column])
						citizen.addNeighbour(map[row+1][column-1])
						
					}
				}
				
			} else {
				citizens.eachWithIndex { Citizen citizen, column ->
					if ( isFirstColumn(column) ) {
						citizen.addNeighbour(citizens[column+1])
						citizen.addNeighbour(map[row-1][column])
						citizen.addNeighbour(map[row-1][column+1])
						if ( row < map.size()-1 ) {
							citizen.addNeighbour(map[row+1][column])
							citizen.addNeighbour(map[row+1][column+1])
						}
						
					} else if ( isNeitherFirstOrLastColumn(column, citizens) ) {
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
						
					} else if ( isLastColumn(column, citizens) ) {
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
	}
	
	def bootstrap(Citizen[][] map, List<Coordinates> points) {
		points.each { point ->
			def citizen = map[point.y][point.x]
			citizen.live()
		}
	}

}
