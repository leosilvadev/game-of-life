package org.gameoflife

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class Application {
	
	static main(args){
		configurePort {
			SpringApplication.run(Application, args)
		}
	}
	
	static configurePort(Closure run) {
		if ( System.getenv().PORT ) {
			String webPort = System.getenv().PORT
			System.setProperty("server.port", webPort)
			
		}
		run()
	}
}
