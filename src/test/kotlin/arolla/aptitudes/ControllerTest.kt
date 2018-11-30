package arolla.aptitudes

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ControllerTest {
    private lateinit var api: MockMvc
    private val service = mockk<Service>()

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
            every { service.employee("johnny") } returns Employee("johnny", listOf(Skill("singing", 3)))
            api
                    .perform(get("/employees/johnny"))
                    .andExpect(status().isOk)
                    .andExpect(content().json("""{name: "johnny", skills: [{name: "singing", level: 3}]}"""))
        }

        @Test
        fun `returns 404 when employee doesn't exist`() {
            every { service.employee("noone") } returns null
            api
                    .perform(get("/employees/noone"))
                    .andExpect(status().isNotFound)
        }
    }
}
