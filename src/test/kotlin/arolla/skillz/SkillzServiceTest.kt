package arolla.skillz

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SkillzServiceTest {
    private lateinit var skillz: SkillzService

    @BeforeEach
    internal fun setUp() {
        skillz = SkillzService()
    }

    @Test
    fun `adds employee`() {
        val yoda = Employee("yoda", listOf(Skill("inversion", 3)))
        skillz.createEmployee(yoda)
        assertThat(skillz.employees).containsExactly(yoda)
    }

    @Test
    fun `aggregates skills`() {
        skillz.createEmployee(Employee("Bee Gees", listOf(Skill("dancing", 1))))
        skillz.createEmployee(Employee("Swift", listOf(Skill("shaking", 2))))
        assertThat(skillz.skills).containsExactlyInAnyOrder("shaking", "dancing")
    }

    @Test
    fun `skills removes duplicates`() {
        skillz.createEmployee(Employee("Bruce Lee", listOf(Skill("fighting", 3))))
        skillz.createEmployee(Employee("Check Norris", listOf(Skill("fighting", 2))))
        assertThat(skillz.skills).containsExactly("fighting")
    }

    @Test
    fun `skills is not case sensitive`() {
        skillz.createEmployee(Employee("Bruce Lee", listOf(Skill("fighting", 3))))
        skillz.createEmployee(Employee("Check Norris", listOf(Skill("FighTing", 2))))
        assertThat(skillz.skills).containsExactly("fighting")
    }
}
