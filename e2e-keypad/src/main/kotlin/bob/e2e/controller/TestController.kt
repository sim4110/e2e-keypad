package bob.e2e.controller

import bob.e2e.KeypadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test/a")
    fun getKeypad(@PathVariable filename: String) : ResponseEntity<Resource> {
        val image: Resource = KeypadService.loadKeypad(filename)



        return "Hello Spring"
    }




}
