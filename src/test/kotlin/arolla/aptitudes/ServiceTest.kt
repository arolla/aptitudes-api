package arolla.aptitudes

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ServiceTest {
    private lateinit var aptitudes: Service
    private val repository = mockk<Repository>(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        aptitudes = Service(repository)
    }

    @Test
    fun `adds employee`() {
        val yoda = Employee("yoda", listOf(Skill("inversion", 3)))
        aptitudes.createEmployee(yoda)
        assertThat(aptitudes.employees).containsExactly(yoda)
    }

    @Test
    fun `aggregates skills`() {
        aptitudes.createEmployee(Employee("Bee Gees", listOf(Skill("dancing", 1))))
        aptitudes.createEmployee(Employee("Swift", listOf(Skill("shaking", 2))))
        assertThat(aptitudes.skills).containsExactlyInAnyOrder("shaking", "dancing")
    }

    @Test
    fun `skills removes duplicates`() {
        aptitudes.createEmployee(Employee("Bruce Lee", listOf(Skill("fighting", 3))))
        aptitudes.createEmployee(Employee("Check Norris", listOf(Skill("fighting", 2))))
        assertThat(aptitudes.skills).containsExactly("fighting")
    }

    @Test
    fun `skills is not case sensitive`() {
        aptitudes.createEmployee(Employee("Bruce Lee", listOf(Skill("fighting", 3))))
        aptitudes.createEmployee(Employee("Check Norris", listOf(Skill("FighTing", 2))))
        assertThat(aptitudes.skills).containsExactly("fighting")
    }
}
