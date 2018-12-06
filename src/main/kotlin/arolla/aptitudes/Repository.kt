package arolla.aptitudes

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Repository
class Repository(private val eventDAO: EventDAO) {
    private val employeeCodec = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(Employee::class.java)

    val employees: Collection<Employee>
        get() = eventDAO.findAll()
                .map { it.body }
                .map { employeeCodec.fromJson(it!!)!! }

    fun create(employee: Employee) {
        val event = Event(body = employeeCodec.toJson(employee))
        eventDAO.save(event)
    }
}

@Repository
interface EventDAO : CrudRepository<Event, Int>

@Entity
data class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int? = null,
        val body: String? = null
)
