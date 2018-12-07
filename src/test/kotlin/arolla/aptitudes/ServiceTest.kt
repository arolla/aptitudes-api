package arolla.aptitudes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ServiceTest {
    private lateinit var aptitudes: Service
    private lateinit var repository: Repository

    @BeforeEach
    internal fun setUp() {
        repository = mock()
        aptitudes = Service(repository)
    }

    @Test
    fun `adds employee`() {
        val yoda = Employee(uuid(), "yoda", listOf(Skill("inversion", 3)))
        createEmployees(yoda)
        assertThat(aptitudes.employees).containsExactly(yoda)
    }

    @Test
    fun `aggregates skills`() {
        createEmployees(
                Employee(uuid(), "Bee Gees", listOf(Skill("dancing", 1))),
                Employee(uuid(), "Swift", listOf(Skill("shaking", 2)))
        )
        assertThat(aptitudes.skills).containsExactlyInAnyOrder("shaking", "dancing")
    }

    @Test
    fun `skills removes duplicates`() {
        createEmployees(
                Employee(uuid(), "Bruce Lee", listOf(Skill("fighting", 3))),
                Employee(uuid(), "Check Norris", listOf(Skill("fighting", 2)))
        )
        assertThat(aptitudes.skills).containsExactly("fighting")
    }

    @Test
    fun `skills is not case sensitive`() {
        createEmployees(
                Employee(uuid(), "Bruce Lee", listOf(Skill("fighting", 3))),
                Employee(uuid(), "Check Norris", listOf(Skill("FighTing", 2)))
        )
        assertThat(aptitudes.skills).containsExactly("fighting")
    }

    private fun createEmployees(vararg employees: Employee) {
        whenever(repository.employees)
                .thenReturn(employees.toList())
    }
}
