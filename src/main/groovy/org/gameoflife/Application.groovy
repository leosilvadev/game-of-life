package org.gameoflife

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync


@EnableAsync
@SpringBootApplication
class Application {
	
	static main(args){
		SpringApplication.run(Application, args)
	}

}
