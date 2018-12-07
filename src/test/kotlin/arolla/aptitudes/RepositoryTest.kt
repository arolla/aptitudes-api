package arolla.aptitudes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("repository")
class RepositoryTest {
    private lateinit var eventDAO: EventDAO
    private lateinit var repository: Repository

    @BeforeEach
    fun init() {
        eventDAO = mock()
        repository = Repository(eventDAO)
    }

    @Test
    fun `returns empty employees list`() {
        whenever(eventDAO.findAll()).thenReturn(emptyList())
        assertThat(repository.employees).isEqualTo(emptyList<Employee>())
    }

    @Test
    fun `deserializes employee`() {
        val serializedEmployee = """
            |{ "id": "Ayrton", "name": "Senna", "skills": [
            |    { "name": "driving", "level": 3 },
            |    { "name": "cachaça", "level": 1}
            |]}""".trimMargin()
        whenever(eventDAO.findAll()).thenReturn(listOf(Event(
                type = EventType.EmployeeCreated.type,
                body = serializedEmployee
        )))
        assertThat(repository.employees).isEqualTo(listOf(Employee(
                "Ayrton","Senna",
                listOf(
                        Skill("driving", 3),
                        Skill("cachaça", 1)
                )
        )))
    }

    @Test
    fun `serializes employee`() {
        repository.create(Employee(
                "Alain", "Prost",
                listOf(
                        Skill("driving", 3),
                        Skill("pastis", 1)
                )
        ))
        verify(eventDAO).save(Event(
                type = EventType.EmployeeCreated.type,
                body = """{"id":"Alain","name":"Prost","skills":[{"name":"driving","level":3},{"name":"pastis","level":1}]}""")
        )
    }

    @Test
    fun `hides deleted employee`() {
        whenever(eventDAO.findAll()).thenReturn(listOf(
                Event(
                        type = EventType.EmployeeCreated.type,
                        body = """
                        |{ "id": "Hulk", "name": "Hulk Hogan", "skills": [
                        |    { "name": "shouting", "level": 3 },
                        |    { "name": "nimbleness", "level": 1}
                        |]}""".trimMargin()
                ),
                Event(
                        type = EventType.EmployeeDeleted.type,
                        body = "Hulk Hogan"
                )
        ))
        assertThat(repository.employees).isEmpty()
    }

    @Test
    fun `shows recreated employee`() {
        val hulk = """
            |{ "id": "Hulk", "name": "Hogan", "skills": [
            |    { "name": "shouting", "level": 3 },
            |    { "name": "nimbleness", "level": 1}
            |]}""".trimMargin()
        whenever(eventDAO.findAll()).thenReturn(listOf(
                Event(
                        type = EventType.EmployeeCreated.type,
                        body = hulk
                ),
                Event(
                        type = EventType.EmployeeDeleted.type,
                        body = "Hulk Hogan"
                ),
                Event(
                        type = EventType.EmployeeCreated.type,
                        body = hulk
                )
        ))
        assertThat(repository.employees).containsExactly(
                Employee("Hulk", "Hogan", listOf(
                        Skill("shouting", 3),
                        Skill("nimbleness", 1)
                ))
        )
    }
}
