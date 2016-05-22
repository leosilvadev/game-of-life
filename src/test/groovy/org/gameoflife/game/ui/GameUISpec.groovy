package org.gameoflife.game.ui

import geb.spock.GebSpec

import org.gameoflife.Application
import org.junit.Test;
import org.junit.runner.RunWith
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@SpringApplicationConfiguration(classes = Application)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
class GameUISpec extends GebSpec {

	def appUrl
	
	def setup(){
		appUrl = "http://localhost:9000"
	}
	
	def "Should start a new game with 40 streets, 40 citizens in each one"(){
		when: 'enter at game page'
			go(appUrl)
			
		and: 'put the number of cicles to 20'
			$('#cicles').value(20)
		
		and: 'select on the first citizens of the city'
			$('#citizen-1-2').click()
			$('#citizen-2-3').click()
			$('#citizen-3-3').click()
			$('#citizen-3-2').click()
			$('#citizen-3-1').click()
			
		and: 'start the game'
			$('#btn-game-start').click()
			
		and: 'wait for the game to be finished'
			waitFor {
				$('#btn-game-start').displayed
			}
			
		then: 'the alive citizens must be the right ones'
			$('#citizen-6-6').attr('data-alive') == 'true'
			$('#citizen-7-7').attr('data-alive') == 'true'
			$('#citizen-7-8').attr('data-alive') == 'true'
			$('#citizen-8-6').attr('data-alive') == 'true'
			$('#citizen-8-7').attr('data-alive') == 'true'
			
	}
	
}
