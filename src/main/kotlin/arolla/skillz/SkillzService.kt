package arolla.skillz

import org.springframework.stereotype.Service

@Service
class SkillzService {
    val employees = listOf(
            Employee("Laurel", listOf(Skill("Falling"), Skill("Jumping"))),
            Employee("Hardy", listOf(Skill("Hitting")))
    )
    fun employee(name: String): Employee? = employees.firstOrNull { it.name == name }
}
