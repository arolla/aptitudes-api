package arolla.skillz

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController (private val service:HelloService){
    @GetMapping("/hello")
    fun hello() = service.hello
}
