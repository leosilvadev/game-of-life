package org.gameoflife.game.controllers

import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

import javax.validation.Valid

import org.gameoflife.Citizen
import org.gameoflife.game.domains.Game
import org.gameoflife.game.exceptions.ExpiredGame
import org.gameoflife.game.services.GameOfLife
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/games")
class GameController {
	
	static final Integer MAX_IDLE_TIME = 2
	
	@Autowired GameOfLife gameOfLife
	private ConcurrentHashMap maps = new ConcurrentHashMap()
	
	@RequestMapping(method=RequestMethod.POST)
	def start(@Valid @RequestBody Game game){
		def token = UUID.randomUUID().toString()
		gameOfLife.start(game) { Citizen[][] rows ->
			def data = [rows:[]]
			rows.eachWithIndex { columns, rowInde ->
				data.rows << Arrays.asList(columns)
			}
			maps.put(token, [lastUpdate:LocalDateTime.now(), data:data])
		}
		[token:token]
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{token}")
	def get(@PathVariable String token){
		def map = maps.get(token) ?: [data:[rows:[]], lastUpdate:LocalDateTime.now()]
		if ( LocalDateTime.now().minusSeconds(MAX_IDLE_TIME).isAfter(map.lastUpdate) ) {
			maps.remove(token)
			throw new ExpiredGame()
		}
		map
	}

}
