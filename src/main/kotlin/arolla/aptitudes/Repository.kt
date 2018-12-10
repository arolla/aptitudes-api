package arolla.aptitudes

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Repository
class Repository(private val eventDAO: EventDAO) {
    private val serializer = EventSerializer()

    val employees: Collection<Employee>
        get() = eventDAO.findAll()
                .map { serializer.deserialize(it) }
                .groupBy { it.employeeId }
                .map { it.value }
                .filter { it.last() is EmployeeCreated }
                .map { it.last() }
                .map { it as EmployeeCreated }
                .map { it.employee }

    fun create(employee: Employee) {
        eventDAO.save(serializer.employeeCreated(employee))
    }

    fun deleteEmployee(id: String) {
        eventDAO.save(serializer.employeeDeleted(id))
    }
}

private sealed class DeserializedEvent {
    abstract val employeeId: String
}

private data class EmployeeCreated(val employee: Employee) : DeserializedEvent() {
    override val employeeId: String
        get() = employee.id
}

private data class EmployeeDeleted(override val employeeId: String) : DeserializedEvent()

private class EventSerializer {
    private val employeeCodec = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(Employee::class.java)

    fun deserialize(event: Event): DeserializedEvent =
            if (event.type!! == EventType.EmployeeCreated.type)
                EmployeeCreated(employeeCodec.fromJson(event.body!!)!!)
            else
                EmployeeDeleted(event.body!!)

    fun employeeCreated(employee: Employee): Event = Event(
            type = EventType.EmployeeCreated.type,
            body = employeeCodec.toJson(employee)
    )

    fun employeeDeleted(id: String): Event = Event(
            type = EventType.EmployeeDeleted.type,
            body = id
    )
}

@Repository
interface EventDAO : CrudRepository<Event, Int>

enum class EventType(val type: String) {
    EmployeeCreated("employee created"),
    EmployeeDeleted("employee deleted"),
}

@Entity
data class Event(
        @Id
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        @GeneratedValue(generator = "uuid")
        @Type(type="pg-uuid")
        val id: UUID? = null,
        val type: String? = null,
        val body: String? = null
)
