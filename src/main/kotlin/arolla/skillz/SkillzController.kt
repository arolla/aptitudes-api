package arolla.skillz

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SkillzController(private val service: SkillzService) {
    @GetMapping("/employees")
    fun employees(): Collection<Employee> = service.employees

    @GetMapping("/employees/{name}")
    fun employee(@PathVariable name: String): ResponseEntity<Employee> {
        val employee = service.employee(name)
        return if (employee == null)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity(employee, HttpStatus.OK)
    }

    @PostMapping("/employees")
    fun createEmployee(@RequestBody employee: Employee): ResponseEntity<Employee> = ResponseEntity(employee, HttpStatus.OK)
}
