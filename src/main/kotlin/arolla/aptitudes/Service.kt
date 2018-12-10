package arolla.aptitudes

import org.springframework.stereotype.Service

@Service
class Service(private val repository: Repository) {
    val employees: Collection<Employee>
        get() = repository.employees

    fun employee(name: String): Employee? = employees.firstOrNull { it.name == name }

    fun create(employee: Employee): Employee {
        repository.create(employee)
        return employee
    }

    fun update(employee: Employee) {
        repository.update(employee)
    }

    fun deleteEmployee(id: String) {
        repository.deleteEmployee(id)
    }

    val skills: List<String>
        get() = employees.flatMap { it.skills }
                .map { it.name }
                .distinctBy { it.toLowerCase() }
}
