package arolla.aptitudes

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Repository
class Repository(private val eventDao: EventDAO) {
    fun createEmployee(employee: Employee) {
        eventDao.save(Event(body = employee.toString()))
    }
}

@Repository
interface EventDAO : CrudRepository<Event, Int>

@Entity
class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int? = null,
        val body: String
)
