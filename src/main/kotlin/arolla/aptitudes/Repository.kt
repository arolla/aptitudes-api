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
import javax.persistence.Lob

@Repository
class Repository(private val eventDAO: EventDAO) {
    private val serializer = EventSerializer()

    val employees: Collection<Employee>
        get() = eventDAO.findAll()
                .map { serializer.deserialize(it) }
                .groupBy { it.employeeId }
                .map { it.value }
                .filter { it.last() is EmployeeHolderEvent }
                .map { it.last() }
                .map { it as EmployeeHolderEvent }
                .map { it.employee }

    fun create(employee: Employee) {
        eventDAO.save(serializer.employeeCreated(employee))
    }

    fun update(employee: Employee) {
        eventDAO.save(serializer.employeeUpdated(employee))
    }

    fun deleteEmployee(id: String) {
        eventDAO.save(serializer.employeeDeleted(id))
    }
}

private sealed class DeserializedEvent {
    abstract val employeeId: String
}

private open class EmployeeHolderEvent(open val employee: Employee) : DeserializedEvent() {
    override val employeeId: String
        get() = employee.id
}

private data class EmployeeCreated(override val employee: Employee) : EmployeeHolderEvent(employee)
private data class EmployeeUpdated(override val employee: Employee) : EmployeeHolderEvent(employee)
private data class EmployeeDeleted(override val employeeId: String) : DeserializedEvent()

private class EventSerializer {
    private val employeeCodec = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(Employee::class.java)

    fun deserialize(event: Event): DeserializedEvent = when (event.type) {
        EventType.EmployeeCreated.type -> EmployeeCreated(employeeCodec.fromJson(event.body!!)!!)
        EventType.EmployeeUpdated.type -> EmployeeUpdated(employeeCodec.fromJson(event.body!!)!!)
        else -> EmployeeDeleted(event.body!!)
    }

    fun employeeCreated(employee: Employee): Event = Event(
            type = EventType.EmployeeCreated.type,
            body = employeeCodec.toJson(employee)
    )

    fun employeeDeleted(id: String): Event = Event(
            type = EventType.EmployeeDeleted.type,
            body = id
    )

    fun employeeUpdated(employee: Employee): Event = Event(
            type = EventType.EmployeeUpdated.type,
            body = employeeCodec.toJson(employee)
    )
}

@Repository
interface EventDAO : CrudRepository<Event, Int>

enum class EventType(val type: String) {
    EmployeeCreated("employee created"),
    EmployeeDeleted("employee deleted"),
    EmployeeUpdated("employee updated"),
}

@Entity
data class Event(
        @Id
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        @GeneratedValue(generator = "uuid")
        @Type(type = "pg-uuid")
        val id: UUID? = null,
        val type: String? = null,
        @Lob
        val body: String? = null
)
