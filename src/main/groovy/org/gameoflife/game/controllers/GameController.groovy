package org.gameoflife.game.controllers

import javax.validation.Valid

import org.gameoflife.Citizen;
import org.gameoflife.game.domains.Game
import org.gameoflife.game.services.GameOfLife
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/games")
class GameController {
	
	@Autowired GameOfLife gameOfLife
	private Map map = [:]
	
	@RequestMapping(method=RequestMethod.POST)
	def start(@Valid @RequestBody Game game){
		gameOfLife.start(game.config, game.initialPoints) { Citizen[][] rows ->
			def res = [rows:[]]
			rows.eachWithIndex { columns, rowInde ->
				res.rows << Arrays.asList(columns)
			}
			map = res
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	def get(){
		map
	}

}
