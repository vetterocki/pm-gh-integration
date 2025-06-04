package pm.gh.integration.infrastructure.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestControllerAdvice
internal object ResponseEntityControllerAdvice {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleBadRequest(runtimeException: RuntimeException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body (ExceptionResponse(runtimeException.message.orEmpty()))
    }

    data class ExceptionResponse(val message: String, val timeStamp: String) {
        constructor(message: String) : this(
            message,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
        )
    }
}