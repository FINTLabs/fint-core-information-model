package no.novari.fint.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LinkTest {

    @Test
    fun parseDecomposesFintHref() {
        val link = Link.parse("https://api.felleskomponent.no/utdanning/elev/elev/systemId/S-123")
        assertEquals("systemid", link.idField)
        assertEquals("S-123", link.idValue)
        assertNull(link.unresolved)
    }

    @Test
    fun parseHandlesRelativeHref() {
        val link = Link.parse("/utdanning/vurdering/elevfravar/systemid/42")
        assertEquals("systemid", link.idField)
        assertEquals("42", link.idValue)
    }

    @Test
    fun parseDecodesIdValue() {
        val link = Link.parse("https://api.example.no/felles/person/fodselsnummer/12%2034")
        assertEquals("12 34", link.idValue)
    }

    @Test
    fun parseKeepsExternalHrefVerbatim() {
        val href = "https://grep.udir.no/KL06/REF123"
        val link = Link.parse(href)
        assertEquals(href, link.unresolved)
        assertNull(link.idField)
    }

    @Test
    fun hrefRebuildsWireForm() {
        val link = Link(idField = "systemid", idValue = "S 123")
        assertEquals(
            "https://api.example.no/utdanning/elev/elev/systemid/S%20123",
            link.href("https://api.example.no/", "utdanning/elev/elev"),
        )
    }

    @Test
    fun hrefEmitsUnresolvedVerbatim() {
        val href = "https://grep.udir.no/KL06/REF123"
        assertEquals(href, Link(unresolved = href).href("https://api.example.no", "utdanning/timeplan/fag"))
    }
}
