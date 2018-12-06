package arolla.aptitudes

import org.springframework.stereotype.Service

@Service
class Service(private val repository: Repository) {
    val employees: Collection<Employee>
        get() = repository.employees

    fun employee(name: String): Employee? = employees.firstOrNull { it.name == name }

    fun createEmployee(employee: Employee): Employee {
        repository.create(employee)
        return employee
    }

    val skills: List<String>
        get() = employees.flatMap { it.skills }
                .map { it.name }
                .distinctBy { it.toLowerCase() }
}
