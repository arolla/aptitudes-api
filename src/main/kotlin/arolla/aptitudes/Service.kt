package arolla.aptitudes

import org.springframework.stereotype.Service

@Service
class Service(private val repository: Repository) {
    val _employees = mutableListOf<Employee>()

    val employees: List<Employee>
        get() = _employees.toList()

    fun employee(name: String): Employee? = _employees.firstOrNull { it.name == name }

    fun createEmployee(employee: Employee): Employee {
        repository.createEmployee(employee)
        _employees.add(employee)
        return employee
    }

    val skills: List<String>
        get() = _employees.flatMap { it.skills }
                .map { it.name }
                .distinctBy { it.toLowerCase() }
}
