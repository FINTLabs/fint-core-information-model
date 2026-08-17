package no.novari.fint.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
    fun `href escapes a space as a path segment does, not as a form field`() {
        val space = Link(idField = "systemid", idValue = "S 123").href("https://x.no", "p/q/r")
        assertEquals("https://x.no/p/q/r/systemid/S%20123", space)

        val plus = Link(idField = "systemid", idValue = "a+b").href("https://x.no", "p/q/r")
        assertEquals("https://x.no/p/q/r/systemid/a%2Bb", plus)
    }

    @Test
    fun `idHref is the raw pair adapters read`() {
        assertEquals("systemid/S 123", Link(idField = "systemid", idValue = "S 123").idHref)
        assertEquals("fodselsnummer/ABC/DEF", Link(idField = "fodselsnummer", idValue = "ABC/DEF").idHref)
    }

    @Test
    fun `idHref emits unresolved links verbatim and null when there is nothing`() {
        val href = "https://grep.udir.no/KL06/REF123"
        assertEquals(href, Link(unresolved = href).idHref)
        assertNull(Link().idHref)
        assertNull(Link(idField = "systemid").idHref)
    }

    @Test
    fun `href emits unresolved links verbatim`() {
        val href = "https://grep.udir.no/KL06/REF123"
        assertEquals(href, Link(unresolved = href).href("https://api.example.no", "utdanning/timeplan/fag"))
    }
}
