package org.gameoflife.game.domains

import com.fasterxml.jackson.annotation.JsonIgnore

class Citizen {
	
	@JsonIgnore
	List<Citizen> neighbours
	Boolean alive
	Tuple2 position
	
	Citizen(int x, int y, Boolean alive=false, List<Citizen> neighbours=[]){
		this.alive = alive
		this.neighbours = neighbours
		this.position = new Tuple2(x, y)
	}
	
	Citizen live(){
		this.alive = true
		this
	}
	
	Citizen die(){
		this.alive = false
		this
	}
	
	Boolean isAlive(){
		alive
	}
	
	Boolean isDead(){
		!isAlive()
	}
	
	Citizen addNeighbour(Citizen neighbour){
		neighbours << neighbour
		this
	}
	
	Boolean mustChange(){
		def alives = neighbours.findAll { it.isAlive() }.size()
		
		if ( isAlive() && alives < 2 ) {
			true
			
		} else if ( isAlive() && (alives == 2 || alives == 3) )  {
			false
		
		} else if ( isAlive() && alives > 3 ) {
			true
		
		} else if ( isDead() && alives == 3 ) {
			true
			
		} else {
			false
		}
	}
	
	void liveOrDie(){
		this.alive = !isAlive() 
	}
	
}
