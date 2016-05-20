package org.gameoflife.game.domains

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.NotEmpty

class Game {
	
	@NotNull
	Coordinates config
	
	@NotNull @NotEmpty
	List<Coordinates> initialPoints
	
	static class Coordinates {
		
		@NotNull @Min(0l)
		Integer x
		
		@NotNull @Min(0l)
		Integer y
		
	}

}
