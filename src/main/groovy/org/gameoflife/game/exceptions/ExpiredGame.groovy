package org.gameoflife.game.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code=HttpStatus.NOT_FOUND, reason='The game has expired')
class ExpiredGame extends RuntimeException {

}
