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
        val yoda = Employee("yoda", listOf(Skill("inversion")))
        skillz.createEmployee(yoda)
        assertThat(skillz.employees).contains(yoda)
    }
}