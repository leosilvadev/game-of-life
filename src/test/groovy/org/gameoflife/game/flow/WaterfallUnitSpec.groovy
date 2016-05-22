package org.gameoflife.game.flow

import spock.lang.Specification

class WaterfallUnitSpec extends Specification {
	
	def "Should run all the waterfall steps when has no exception"(){
		given:
			def fn1 = { 10 }
			
		and:
			def fn2 = { it + 15 }
			
		and:
			def fn3 = { it - 5 }
			
		and:
			def fn4 = { it * 2 }
			
		when:
			def result = Flow.waterfall([fn1, fn2, fn3, fn4])
			
		then:
			result == 40
	}
	
	def "Should run the recovery function in waterfall when got an exception"(){
		given:
			def fn1 = { 10 }
			
		and:
			def fn2 = { it + 15 }
			
		and:
			def exception = new IllegalArgumentException('Any error!')
			
		and:
			def fn3 = { throw exception }
			
		and:
			def fn4 = { it * 2 }
			
		and:
			def recoveryFn = Mock(Closure)
			
		when:
			Flow.waterfall([fn1, fn2, fn3], recoveryFn)
			
		then:
			1 * recoveryFn(exception)
	}

}
