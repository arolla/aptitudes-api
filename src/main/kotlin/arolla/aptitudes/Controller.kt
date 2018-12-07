package arolla.aptitudes

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct

@RestController
class Controller(private val service: Service) {
    @PostConstruct
    fun init() {
        service.create(Employee(uuid(), "Laurel", listOf(Skill("Falling", 2), Skill("Jumping", 1))))
        service.create(Employee(uuid(), "Hardy", listOf(Skill("Hitting", 3))))
    }

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
    fun createEmployee(@RequestBody employee: Employee): ResponseEntity<Employee> = ResponseEntity(service.create(employee), HttpStatus.OK)

    @GetMapping("/skills")
    fun skills(): Collection<String> = service.skills

    @DeleteMapping("/employees/{name}")
    fun deleteEmployee(@PathVariable name: String): ResponseEntity<Unit> {
        service.deleteEmployee(name)
        return ResponseEntity(HttpStatus.OK)
    }
}
