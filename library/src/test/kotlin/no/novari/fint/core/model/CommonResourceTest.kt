package no.novari.fint.core.model

import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.Kontaktperson
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.utdanning.elev.Elev
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CommonResourceTest {

    @Test
    fun `the felles resources are the common ones`() {
        assertEquals(
            setOf("felles:Person", "felles:Kontaktperson", "felles:Virksomhet"),
            FintModel.resources.filter { it.isCommon }.map { it.ref }.toSet(),
        )
    }

    @Test
    fun `a common resource names no endpoint of its own`() {
        assertNull(Person.path)
        assertTrue(Person.Metadata.isCommon)
        assertEquals("person", Person.Metadata.name)
        assertNull(Elev.relations.first { it.name == "person" }.targetPath)
    }

    @Test
    fun `no resource is left with a two-segment path`() {
        FintModel.resources.forEach { meta ->
            val path = meta.path ?: return@forEach
            assertTrue(path.count { it == '/' } >= 2, "${meta.ref} has path $path")
        }
    }

    @Test
    fun `an owner resolves a common target against its own path`() {
        assertEquals("utdanning/elev/person", Elev.Metadata.relationPath("person"))
        assertEquals("administrasjon/personal/person", Personalressurs.Metadata.relationPath("PERSON"))
    }

    @Test
    fun `a common owner resolves against the path it was reached through`() {
        assertEquals(
            "utdanning/elev/kontaktperson",
            Person.Metadata.relationPath("parorende", "utdanning/elev/person"),
        )
        assertEquals(
            "administrasjon/personal/person",
            Kontaktperson.Metadata.relationPath("kontaktperson", "administrasjon/personal/kontaktperson"),
        )
    }

    @Test
    fun `a common owner has nothing to resolve against on its own`() {
        assertNull(Person.Metadata.relationPath("parorende"))
        assertNull(Person.Metadata.relationPath("parorende", "felles"))
    }

    @Test
    fun `context moves only the resources that have no path`() {
        assertEquals("utdanning/elev/elev", Elev.Metadata.pathIn("administrasjon/personal/person"))
        assertEquals("utdanning/elev/person", Person.Metadata.pathIn("utdanning/elev/elev"))
        assertEquals(
            "utdanning/elev/elevforhold",
            Elev.Metadata.relationPath("elevforhold", "administrasjon/personal/person"),
        )
    }

    @Test
    fun `byPath finds a common resource under every domain and package serving it`() {
        assertSame(Person.Metadata, FintModel.byPath("utdanning", "elev", "person"))
        assertSame(Person.Metadata, FintModel.byPath("administrasjon", "personal", "Person"))
        assertSame(Kontaktperson.Metadata, FintModel.byPath("utdanning", "elev", "KONTAKTPERSON"))
    }
}
