package arolla.aptitudes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ControllerTest {
    private lateinit var api: MockMvc
    private val service:Service = mock()

    @BeforeEach
    fun `set up`() {
        val controller = Controller(service)
        api = MockMvcBuilders
                .standaloneSetup(controller)
                .build()
    }

    @Nested
    inner class `get employee` {
        @Test
        fun `returns requested employee`() {
            whenever(service.employee("johnny"))
                    .thenReturn(Employee("johnny", listOf(Skill("singing", 3))))
            api
                    .perform(get("/employees/johnny"))
                    .andExpect(status().isOk)
                    .andExpect(content().json("""{name: "johnny", skills: [{name: "singing", level: 3}]}"""))
        }

        @Test
        fun `returns 404 when employee doesn't exist`() {
            whenever(service.employee("noone")).thenReturn(null)
            api
                    .perform(get("/employees/noone"))
                    .andExpect(status().isNotFound)
        }
    }
}
