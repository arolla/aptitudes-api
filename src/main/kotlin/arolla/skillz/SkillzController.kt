package arolla.skillz

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SkillzController {
    private val employees = listOf(
            Employee("Laurel", listOf(Skill("Falling"), Skill("Jumping"))),
            Employee("Hardy", listOf(Skill("Hitting")))
    )

    @GetMapping("/employees")
    fun employees(): Collection<Employee> = employees

    @GetMapping("/employees/{name}")
    fun employee(@PathVariable name: String): ResponseEntity<Employee> {
        val employee = employees.firstOrNull { it.name == name }
        return if (employee == null)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity(employee, HttpStatus.OK)
    }

    @PostMapping("/employees")
    fun createEmployee(@RequestBody employee: Employee) = employee
}
