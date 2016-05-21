package org.gameoflife.game.services

import org.gameoflife.game.domains.Citizen
import org.gameoflife.game.domains.Game
import org.gameoflife.game.flow.Flow
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GameOfLife {
	
	@Async
	void start(Game game, Closure notify, Closure finish){
		Citizen[][] map = new Citizen[game.config.y][game.config.x] 
		
		def recoveryFn = { ex -> finish() }
		def inputData = [map:map, points:game.initialPoints, cicles:game.cicles, delay:game.delay, notify:notify]
		
		Flow.waterfall([init, linkCitizens, bootstrap, startGame, finish], recoveryFn, inputData)
	}
	
	def init = { data ->
		data.map.eachWithIndex { item, indexX ->
			item.eachWithIndex { citizen, indexY ->
				item[indexY] = new Citizen(indexX, indexY)
			}
		}
		data
	}
	
	def startGame = { data ->
		0.upto(data.cicles) {
			def toToggle = []
			for ( int rowIndex = 0 ; rowIndex < data.map.size() ; rowIndex++ ) {
				def row = data.map[rowIndex]
				for ( int colIndex = 0 ; colIndex < row.size() ; colIndex++ ) {
					def citizen = row[colIndex]
					if ( citizen.mustToggle() ) {
						toToggle << citizen
					}
				}
			}
			data.notify(data.map)
			toToggle.each { Citizen citizen -> citizen.toggleLife() }
			sleep(data.delay)
		}
		data
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
	
	def linkCitizens = { data ->
		def map = data.map
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
		data
	}
	
	def bootstrap = { data ->
		data.points.each { point ->
			def citizen = data.map[point.y][point.x]
			citizen.live()
		}
		data
	}

}
