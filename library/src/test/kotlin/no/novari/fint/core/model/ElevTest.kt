package no.novari.fint.core.model

import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ElevTest {

    @Test
    fun `visitor visits only id fields that are set`() {
        val elev = Elev(
            systemId = Identifikator(identifikatorverdi = "S-1"),
            elevnummer = Identifikator(identifikatorverdi = "42"),
        )
        val seen = mutableMapOf<String, String?>()
        elev.visitIdentifikators { name, id -> seen[name] = id.identifikatorverdi }
        assertEquals(setOf("elevnummer", "systemId"), seen.keys)
        assertEquals("S-1", seen["systemId"])
        assertEquals("42", seen["elevnummer"])
    }

    @Test
    fun `visitor visits nothing when no id field is set`() {
        var visits = 0
        Elev().visitIdentifikators { _, _ -> visits++ }
        assertEquals(0, visits)
    }

    @Test
    fun `identifikator lookup is case-insensitive`() {
        val elev = Elev(feidenavn = Identifikator(identifikatorverdi = "x@feide.no"))
        assertEquals("x@feide.no", elev.identifikator("FEIDENAVN")?.identifikatorverdi)
        assertNull(elev.identifikator("ukjent"))
    }

    @Test
    fun `addLink stores and relationLinks reads back`() {
        val elev = Elev()
        elev.addLink("elevforhold", Link(idField = "systemid", idValue = "1"))
        assertEquals(1, elev.relationLinks("elevforhold").size)
        assertTrue(elev.relationLinks("person").isEmpty())
    }

    @Test
    fun `static metadata answers routing questions without an instance`() {
        assertEquals("utdanning/elev/elev", Elev.path)
        assertTrue(Elev.Metadata.isIdField("SYSTEMID"))
        assertFalse(Elev.Metadata.isIdField("navn"))
        assertEquals("utdanning/elev/elevforhold", Elev.Metadata.relationPath("elevforhold"))
    }

    @Test
    fun `relations carry baked target data and multiplicity flags`() {
        val elevforhold = Elev.relations.first { it.name == "elevforhold" }
        assertEquals("utdanning/elev/elevforhold", elevforhold.targetPath)
        assertTrue(elevforhold.multiplicity.many)
        assertFalse(elevforhold.multiplicity.required)
        assertTrue(elevforhold.isBidirectional)
        assertEquals(FintMultiplicity.EXACTLY_ONE, elevforhold.bidirectional?.inverseMultiplicity)
        assertSame(Elevforhold.Metadata, elevforhold.targetMetadata)

        val person = Elev.relations.first { it.name == "person" }
        assertTrue(person.multiplicity.required)
        assertFalse(person.multiplicity.many)
    }
}
