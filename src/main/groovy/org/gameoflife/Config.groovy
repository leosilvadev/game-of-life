package org.gameoflife

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Configuration
class Config {
	
	@Bean
	SseEmitter sseEmitter(){
		new SseEmitter()
	}

}
