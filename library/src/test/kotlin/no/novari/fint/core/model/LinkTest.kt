package no.novari.fint.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class LinkTest {

    @Test
    fun `href joins baseUrl, target path and the id the caller encoded`() {
        val link = Link(idField = "systemid", idValue = "S 123")
        assertEquals(
            "https://api.example.no/utdanning/elev/elev/systemid/S%20123",
            link.href("https://api.example.no/", "utdanning/elev/elev", "S%20123"),
        )
    }

    @Test
    fun `href encodes nothing itself`() {
        val link = Link(idField = "fodselsnummer", idValue = "ABC/DEF")
        assertEquals(
            "https://api.example.no/utdanning/elev/person/fodselsnummer/ABC/DEF",
            link.href("https://api.example.no", "utdanning/elev/person", link.idValue),
        )
    }

    @Test
    fun `an empty baseUrl gives a root-relative href`() {
        val link = Link(idField = "fodselsnummer", idValue = "ABC/DEF")
        assertEquals(
            "/utdanning/elev/person/fodselsnummer/ABC%2FDEF",
            link.href("", "utdanning/elev/person", "ABC%2FDEF"),
        )
    }

    @Test
    fun `href emits unresolved links verbatim and ignores the encoded value`() {
        val href = "https://grep.udir.no/KL06/REF123"
        assertEquals(href, Link(unresolved = href).href("https://api.example.no", "utdanning/timeplan/fag", null))
    }
}
