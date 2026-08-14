package no.novari.fint.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class LinkTest {

    @Test
    fun `href rebuilds the wire form from baseUrl and target path`() {
        val link = Link(idField = "systemid", idValue = "S 123")
        assertEquals(
            "https://api.example.no/utdanning/elev/elev/systemid/S%20123",
            link.href("https://api.example.no/", "utdanning/elev/elev"),
        )
    }

    @Test
    fun `href keeps an id value in one segment`() {
        val link = Link(idField = "fodselsnummer", idValue = "ABC/DEF")
        assertEquals(
            "https://api.example.no/utdanning/elev/person/fodselsnummer/ABC%2FDEF",
            link.href("https://api.example.no", "utdanning/elev/person"),
        )
    }

    @Test
    fun `href emits unresolved links verbatim`() {
        val href = "https://grep.udir.no/KL06/REF123"
        assertEquals(href, Link(unresolved = href).href("https://api.example.no", "utdanning/timeplan/fag"))
    }
}
