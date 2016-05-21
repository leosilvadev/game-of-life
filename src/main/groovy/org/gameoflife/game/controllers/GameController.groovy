package org.gameoflife.game.controllers

import javax.validation.Valid

import org.gameoflife.game.domains.Citizen;
import org.gameoflife.game.domains.Game
import org.gameoflife.game.enums.GameStatus
import org.gameoflife.game.services.GameOfLife
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/games")
class GameController {
	
	@Autowired GameOfLife gameOfLife
	@Autowired SimpMessagingTemplate messagingTemplate
	
	@RequestMapping(method=RequestMethod.POST)
	def start(@Valid @RequestBody Game game){
		def token = UUID.randomUUID().toString()
		
		def onUpdate = { Citizen[][] rows ->
			def data = [
				rows: rows.collect(Arrays.&asList), 
				status: GameStatus.RUNNING
			]
			
			def queue = "/queue/playing-$token" as String
			messagingTemplate.convertAndSend(queue, data)
		}
		
		def onFinish = {
			def queue = "/queue/playing-$token" as String
			messagingTemplate.convertAndSend(queue, [status: GameStatus.FINISHED])
		}
		
		gameOfLife.start(game, onUpdate, onFinish)
		 
		[token:token]
	}

}
