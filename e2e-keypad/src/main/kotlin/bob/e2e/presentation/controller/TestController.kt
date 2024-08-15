package bob.e2e.presentation.controller

import bob.e2e.domain.service.KeypadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class TestController(private val keypadService: KeypadService) {
    @GetMapping("/api/keypad")
    fun getKeypad(
        @RequestParam keypadId: String
    ): ResponseEntity<Map<String, Any>> {
        val keypadData = keypadService.currentKeypadImage(keypadId)

        return if (keypadData != null) {
            ResponseEntity.ok(mapOf("keypadItems" to keypadData.first, "keypadImage" to keypadData.second))
        } else {
            return ResponseEntity.status(404).body(mapOf("error" to "No keypad image"))
        }
    }

    @PostMapping("/api/keypad")
    fun newKeypad(): ResponseEntity<Map<String, Any>> {
        val (keypadId, keypadItems) = keypadService.keyPadHashMap()
//        return ResponseEntity.ok(mapOf("keypadId" to keypadId))
        return ResponseEntity.ok(mapOf("keypadId" to keypadId, "keypadItems" to keypadItems))
    }
}
