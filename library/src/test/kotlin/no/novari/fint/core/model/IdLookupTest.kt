package no.novari.fint.core.model

import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import no.novari.fint.core.model.utdanning.vurdering.Anmerkninger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IdLookupTest {

    private fun id(value: String) = Identifikator(identifikatorverdi = value)

    private val elev = Elev(
        brukernavn = id("hensta"),
        elevnummer = id("E-42"),
        feidenavn = id("hensta@vlfk.no"),
        systemId = id("S-1"),
    )

    @Test
    fun `every id field is found by the value it holds`() {
        assertEquals("brukernavn" to "hensta", elev.idFor("hensta"))
        assertEquals("elevnummer" to "E-42", elev.idFor("E-42"))
        assertEquals("feidenavn" to "hensta@vlfk.no", elev.idFor("hensta@vlfk.no"))
        assertEquals("systemId" to "S-1", elev.idFor("S-1"))
    }

    @Test
    fun `a value no id field holds is null`() {
        assertNull(elev.idFor("12345678901"))
        assertNull(elev.idFor(""))
    }

    @Test
    fun `matching is exact, case included`() {
        assertNull(elev.idFor("s-1"))
        assertNull(elev.idFor(" S-1"))
        assertEquals("systemId" to "S-1", elev.idFor("S-1"))
    }

    @Test
    fun `an id field that is not set is never matched`() {
        val sparse = Elev(systemId = id("S-1"))
        assertEquals("systemId" to "S-1", sparse.idFor("S-1"))
        assertNull(sparse.idFor("hensta"))
    }

    @Test
    fun `when two id fields hold the same value the first declared wins`() {
        val both = Elev(brukernavn = id("X"), elevnummer = id("X"))
        assertEquals(listOf("brukernavn", "elevnummer"), Elev.idFields.take(2))
        assertEquals("brukernavn" to "X", both.idFor("X"))
    }

    @Test
    fun `only this resource is looked at, never the ones nested below it`() {
        val elevforhold = Elevforhold(
            systemId = id("E-1"),
            anmerkninger = listOf(Anmerkninger(systemId = id("A-1"))),
        )

        assertEquals("systemId" to "E-1", elevforhold.idFor("E-1"))
        assertNull(elevforhold.idFor("A-1"))
        assertEquals("systemId" to "A-1", elevforhold.nestedResources.single().idFor("A-1"))
    }

    @Test
    fun `the field it returns is one the resource answers for in both directions`() {
        val (field, value) = elev.idFor("E-42")!!

        assertTrue(Elev.isIdField(field))
        assertTrue(Elev.isIdField(field.lowercase()))
        assertEquals(value, elev.identifikatorverdi(field))
        assertEquals(value, elev.identifikatorverdi(field.lowercase()))
    }
}
