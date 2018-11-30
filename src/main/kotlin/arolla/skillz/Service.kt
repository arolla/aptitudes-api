package arolla.skillz

import org.springframework.stereotype.Service

@Service
class Service {
    val _employees = mutableListOf<Employee>()

    val employees: List<Employee>
        get() = _employees.toList()

    fun employee(name: String): Employee? = _employees.firstOrNull { it.name == name }

    fun createEmployee(employee: Employee): Employee {
        _employees.add(employee)
        return employee
    }

    val skills: List<String>
        get() = _employees.flatMap { it.skills }
                .map { it.name }
                .distinctBy { it.toLowerCase() }
}
