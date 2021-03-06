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

    @GetMapping("/employees/{id}")
    fun employee(@PathVariable id: String): ResponseEntity<Employee> {
        val employee = service.employee(id)
        return if (employee == null)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity(employee, HttpStatus.OK)
    }

    @PostMapping("/employees")
    fun createEmployee(@RequestBody employee: Employee): ResponseEntity<Employee> = ResponseEntity(service.create(employee), HttpStatus.OK)

    @PutMapping("/employees/{id}")
    fun updateEmployee(@RequestBody employee: Employee) {
        service.update(employee)
    }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: String) {
        service.deleteEmployee(id)
    }

    @GetMapping("/skills")
    fun skills(): Collection<String> = service.skills
}
