package no.novari.fint.core.model

import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.timeplan.Fag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResolveLinkTest {

    private val person = Elev.relations.first { it.name == "person" }
    private val elevforhold = Elev.relations.first { it.name == "elevforhold" }
    private val grepreferanse = Fag.relations.first { it.name == "grepreferanse" }

    @Test
    fun `an encoded id value stays one segment and arrives still encoded`() {
        val link = person.resolveLink("https://api.felleskomponent.no/utdanning/elev/person/fodselsnummer/ABC%2FDEF")
        assertEquals("fodselsnummer", link.idField)
        assertEquals("ABC%2FDEF", link.idValue)
        assertNull(link.unresolved)
    }

    @Test
    fun `a relative href resolves the same as an absolute one`() {
        val relative = person.resolveLink("fodselsnummer/12345678901")
        val absolute = person.resolveLink("https://api.felleskomponent.no/utdanning/elev/person/fodselsnummer/12345678901")
        assertEquals(relative, absolute)
        assertEquals("fodselsnummer", relative.idField)
        assertEquals("12345678901", relative.idValue)
    }

    @Test
    fun `a target with no id fields stays unresolved`() {
        val href = "https://data.udir.no/kl06/v201906/fagkoder/FSP01-01"
        val link = grepreferanse.resolveLink(href)
        assertEquals(href, link.unresolved)
        assertNull(link.idField)
        assertNull(link.idValue)
        assertTrue(grepreferanse.targetIdFields.isEmpty())
    }

    @Test
    fun `an href naming no id field of the target stays unresolved`() {
        val href = "https://api.felleskomponent.no/utdanning/elev/person"
        val link = person.resolveLink(href)
        assertEquals(href, link.unresolved)
        assertNull(link.idField)
    }

    @Test
    fun `an unencoded id value is refused rather than truncated`() {
        val href = "utdanning/elev/person/fodselsnummer/ABC/DEF"
        val link = person.resolveLink(href)
        assertEquals(href, link.unresolved)
        assertNull(link.idValue)
    }

    @Test
    fun `a malformed href is told apart from one that was never resolvable`() {
        val malformed = person.resolveLink("utdanning/elev/person/fodselsnummer/ABC/DEF")
        assertTrue(malformed.unresolved != null && person.targetIdFields.isNotEmpty())

        val neverResolvable = grepreferanse.resolveLink("https://data.udir.no/kl06/v201906/fagkoder/FSP01-01")
        assertTrue(neverResolvable.unresolved != null && grepreferanse.targetIdFields.isEmpty())
    }

    @Test
    fun `an id field with no value after it stays unresolved`() {
        val href = "utdanning/elev/person/fodselsnummer"
        assertEquals(href, person.resolveLink(href).unresolved)
        assertEquals("$href/", person.resolveLink("$href/").unresolved)
    }

    @Test
    fun `a bare value with no id field before it stays unresolved`() {
        assertEquals("12345678901", person.resolveLink("12345678901").unresolved)
    }

    @Test
    fun `the id field is matched ignoring case and stored lowercase`() {
        val link = elevforhold.resolveLink("https://api.felleskomponent.no/utdanning/elev/elevforhold/SystemId/S-1")
        assertEquals("systemid", link.idField)
        assertEquals("S-1", link.idValue)
    }

    @Test
    fun `any declared id field of a multi-id target resolves`() {
        assertEquals("systemid", elevforhold.resolveLink("utdanning/elev/elevforhold/systemid/S-1").idField)
        assertEquals("fodselsnummer", person.resolveLink("utdanning/elev/person/fodselsnummer/12345678901").idField)
    }

    @Test
    fun `nothing is decoded on the way in`() {
        for (raw in listOf("12%2034", "ABC%2FDEF", "Bj%C3%B8rn", "a%2Bb", "50%25")) {
            assertEquals(raw, person.resolveLink("fodselsnummer/$raw").idValue)
        }
    }

    @Test
    fun `the whole flow, from what a sender writes to what each reader gets`() {
        val onTheWire = "person/fodselsnummer/ABC%2FDEF"

        val parsed = person.resolveLink(onTheWire)
        assertEquals("fodselsnummer", parsed.idField)
        assertEquals("ABC%2FDEF", parsed.idValue)

        val stored = parsed.copy(idValue = decode(parsed.idValue!!))
        assertEquals("ABC/DEF", stored.idValue)

        assertEquals(
            "https://api.felleskomponent.no/utdanning/elev/person/fodselsnummer/ABC%2FDEF",
            stored.href("https://api.felleskomponent.no", "utdanning/elev/person", encode(stored.idValue!!)),
        )
    }

    private fun decode(value: String): String = java.net.URLDecoder.decode(value, Charsets.UTF_8)

    private fun encode(value: String): String =
        java.net.URLEncoder.encode(value, Charsets.UTF_8).replace("+", "%20")
}
