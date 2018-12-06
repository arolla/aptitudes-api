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
            |{ "name": "Senna", "skills": [
            |    { "name": "driving", "level": 3 },
            |    { "name": "cachaça", "level": 1}
            |]}""".trimMargin()
        whenever(eventDAO.findAll()).thenReturn(listOf(Event(body = serializedEmployee)))
        assertThat(repository.employees).isEqualTo(listOf(Employee(
                "Senna",
                listOf(
                        Skill("driving", 3),
                        Skill("cachaça", 1)
                )
        )))
    }

    @Test
    fun `serializes employee`() {
        repository.create(Employee(
                "Prost",
                listOf(
                        Skill("driving", 3),
                        Skill("pastis", 1)
                )
        ))
        verify(eventDAO).save(Event(body = """{"name":"Prost","skills":[{"name":"driving","level":3},{"name":"pastis","level":1}]}"""))
    }
}
