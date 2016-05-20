package org.gameoflife.game.controllers

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class GameEmitter {
	
	@Autowired SseEmitter emmiter

	@RequestMapping("/games/listen")
	SseEmitter enableGameNotifier(){
	   emmiter
	}
	
}
