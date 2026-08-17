package no.novari.fint.core.model

import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.timeplan.Fag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ResolveLinkTest {

    private val person = Elev.relations.first { it.name == "person" }
    private val elevforhold = Elev.relations.first { it.name == "elevforhold" }
    private val grepreferanse = Fag.relations.first { it.name == "grepreferanse" }

    @Test
    fun `an id value containing slashes survives whole`() {
        val link = person.resolveLink("https://api.felleskomponent.no/utdanning/elev/person/fodselsnummer/ABC/DEF")
        assertEquals("fodselsnummer", link.idField)
        assertEquals("ABC/DEF", link.idValue)
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
    fun `a reference target with no id fields stays unresolved`() {
        val href = "https://data.udir.no/kl06/v201906/fagkoder/FSP01-01"
        val link = grepreferanse.resolveLink(href)
        assertEquals(href, link.unresolved)
        assertNull(link.idField)
        assertNull(link.idValue)
    }

    @Test
    fun `an href naming no id field of the target stays unresolved`() {
        val href = "https://api.felleskomponent.no/utdanning/elev/person"
        val link = person.resolveLink(href)
        assertEquals(href, link.unresolved)
        assertNull(link.idField)
    }

    @Test
    fun `an id field with no value after it stays unresolved`() {
        val href = "utdanning/elev/person/fodselsnummer"
        assertEquals(href, person.resolveLink(href).unresolved)
        assertEquals("$href/", person.resolveLink("$href/").unresolved)
    }

    @Test
    fun `the id field is matched ignoring case and stored lowercase`() {
        val link = elevforhold.resolveLink("https://api.felleskomponent.no/utdanning/elev/elevforhold/SystemId/S-1")
        assertEquals("systemid", link.idField)
        assertEquals("S-1", link.idValue)
    }

    @Test
    fun `the first id field of a multi-id target wins`() {
        val link = elevforhold.resolveLink("utdanning/elev/elevforhold/systemid/S-1")
        assertEquals("systemid", link.idField)
        assertEquals("S-1", link.idValue)
    }

    @Test
    fun `nothing is decoded on the way in`() {
        val link = person.resolveLink("fodselsnummer/12%2034")
        assertEquals("12%2034", link.idValue)
    }

    @Test
    fun `the adapter direction round-trips unchanged`() {
        for (raw in listOf("ABC/DEF", "S 123", "plain", "12%2034")) {
            val link = person.resolveLink("person/fodselsnummer/$raw")
            assertEquals(raw, link.idValue)
            assertEquals(link, person.resolveLink(link.idHref!!))
        }
    }

    @Test
    fun `the whole flow, from what an adapter sends to what each reader gets`() {
        val fromAdapter = "person/fodselsnummer/ABC/DEF"

        val stored = person.resolveLink(fromAdapter)
        assertEquals("fodselsnummer", stored.idField)
        assertEquals("ABC/DEF", stored.idValue)

        assertEquals("fodselsnummer/ABC/DEF", stored.idHref)

        assertEquals(
            "https://api.felleskomponent.no/utdanning/elev/person/fodselsnummer/ABC%2FDEF",
            stored.href("https://api.felleskomponent.no", "utdanning/elev/person"),
        )
    }

    @Test
    fun `a non-ascii id is escaped as its utf-8 bytes`() {
        val link = person.resolveLink("fodselsnummer/Bjørn Æ")
        assertEquals("Bjørn Æ", link.idValue)
        assertEquals(
            "https://x.no/utdanning/elev/person/fodselsnummer/Bj%C3%B8rn%20%C3%86",
            link.href("https://x.no", "utdanning/elev/person"),
        )
    }

    @Test
    fun `href encodes on the way out because the reader has no model`() {
        val link = person.resolveLink("fodselsnummer/ABC/DEF")
        assertEquals(
            "https://api.example.no/utdanning/elev/person/fodselsnummer/ABC%2FDEF",
            link.href("https://api.example.no", "utdanning/elev/person"),
        )
    }
}
