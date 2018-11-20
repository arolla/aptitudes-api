package arolla.skillz

import org.springframework.stereotype.Service

@Service
class SkillzService {
    val _employees = mutableListOf(
            Employee("Laurel", listOf(Skill("Falling", 2), Skill("Jumping", 1))),
            Employee("Hardy", listOf(Skill("Hitting", 3)))
    )
    val employees: List<Employee>
        get() = _employees.toList()

    fun employee(name: String): Employee? = _employees.firstOrNull { it.name == name }

    fun createEmployee(employee: Employee): Employee {
        _employees.add(employee)
        return employee
    }
}
