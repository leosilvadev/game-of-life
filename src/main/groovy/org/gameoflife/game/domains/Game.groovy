package org.gameoflife.game.domains

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.NotEmpty

class Game {
	
	@NotNull @Min(1l)
	Integer cicles
	
	@NotNull @Min(10l)
	Long delay
	
	@NotNull
	Coordinates config
	
	@NotNull @NotEmpty
	List<Coordinates> firstCitizens
	
	static class Coordinates {
		
		@NotNull @Min(0l)
		Integer x
		
		@NotNull @Min(0l)
		Integer y
				
	}

}
