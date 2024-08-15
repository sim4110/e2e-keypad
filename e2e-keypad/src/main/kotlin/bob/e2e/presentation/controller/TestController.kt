package bob.e2e.presentation.controller

import bob.e2e.domain.service.KeypadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(private val keypadService: KeypadService) {
    @GetMapping("/keypad")
    fun getKeypad(
        @RequestParam keypadId: String,
        @RequestParam timestamp: Long
    ):Map<String, Any> {
        val keypadItems = keypadService.keyPadHashMap()
        return mapOf(
            "keypadItems" to keypadItems,
            "timeStamp" to timestamp
        )
    }
}
