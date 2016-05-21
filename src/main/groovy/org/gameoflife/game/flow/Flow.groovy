package org.gameoflife.game.flow

class Flow {
	
	private Closure run
	
	private Flow() {
		run = { List<Closure> fns, Closure recoveryFn, data=null ->
			if ( fns.size() == 0 ){
				data
			} else if ( fns.size() == 1 ) {
				run.trampoline([], recoveryFn, executeNext(fns, data, recoveryFn))
			} else {
				run.trampoline(fns.tail(), recoveryFn, executeNext(fns, data, recoveryFn))
			}
			
		}
		run = run.trampoline()
	}
	
	def executeNext(List<Closure> fns, data, Closure recoveryFn){
		try {
			fns.head()(data)
		} catch (ex) {
			if(recoveryFn) recoveryFn(ex)
		}
	}
	
	def start(List<Closure> fns, Closure recoveryFn=null, data=null){
		run(fns, recoveryFn, data)
	}
	
	static def waterfall(List<Closure> fns, Closure recoveryFn=null, data=null){
		new Flow().start(fns, recoveryFn, data)
	}
	
	static main(args){
		Flow.waterfall([{
			1
		}, {
			it + 1
			
		}, {
			println it
			
		}], { ex ->
			println ex
		})
	}

}
