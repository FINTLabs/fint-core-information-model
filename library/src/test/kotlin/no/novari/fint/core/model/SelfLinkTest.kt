package no.novari.fint.core.model

import no.novari.fint.core.model.arkiv.noark.Dokumentbeskrivelse
import no.novari.fint.core.model.arkiv.noark.Dokumentobjekt
import no.novari.fint.core.model.arkiv.noark.Journalpost
import no.novari.fint.core.model.arkiv.noark.Sak
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SelfLinkTest {

    private fun self(href: String) = Link(unresolved = href)

    @Test
    fun `the self links go and every other relation stays`() {
        val sak = Sak()
        sak.addLink(FintResource.SELF, self("https://api.felleskomponent.no/arkiv/noark/sak/systemid/S-1"))
        sak.addLink("saksstatus", Link(idField = "systemid", idValue = "B"))
        sak.addLink("part", Link(idField = "systemid", idValue = "P-1"))

        sak.removeSelfLinks()

        assertFalse(sak.links.containsKey(FintResource.SELF))
        assertEquals(setOf("saksstatus", "part"), sak.links.keys)
    }

    @Test
    fun `every self link goes, not only the first`() {
        val sak = Sak()
        sak.addLink(FintResource.SELF, self("https://api.felleskomponent.no/arkiv/noark/sak/mappeid/2026%2F42"))
        sak.addLink(FintResource.SELF, self("https://api.felleskomponent.no/arkiv/noark/sak/systemid/S-1"))
        assertEquals(2, sak.relationLinks(FintResource.SELF).size)

        sak.removeSelfLinks()

        assertTrue(sak.relationLinks(FintResource.SELF).isEmpty())
    }

    @Test
    fun `the strip reaches every level of a nested tree`() {
        val dokumentobjekt = Dokumentobjekt(versjonsnummer = 1)
        val dokumentbeskrivelse = Dokumentbeskrivelse(dokumentobjekt = listOf(dokumentobjekt))
        val journalpost = Journalpost(dokumentbeskrivelse = listOf(dokumentbeskrivelse))
        val sak = Sak(journalpost = listOf(journalpost))
        val tree = listOf(sak, journalpost, dokumentbeskrivelse, dokumentobjekt)

        tree.forEach {
            it.addLink(FintResource.SELF, self("https://api.felleskomponent.no/${it.metadata.name}"))
            it.addLink("skjerming", Link(idField = "systemid", idValue = "SK-1"))
        }

        sak.removeSelfLinks()

        assertTrue(tree.none { FintResource.SELF in it.links })
        assertTrue(tree.all { it.links.keys == setOf("skjerming") })
    }

    @Test
    fun `a resource with no self link is left alone`() {
        val sak = Sak(journalpost = listOf(Journalpost()))
        sak.addLink("saksstatus", Link(idField = "systemid", idValue = "B"))

        sak.removeSelfLinks()

        assertEquals(setOf("saksstatus"), sak.links.keys)
        assertTrue(sak.nestedResources.single().links.isEmpty())
    }

    @Test
    fun `stripped on the way in, built again on the way out`() {
        val sak = Sak(
            mappeId = Identifikator(identifikatorverdi = "2026/42"),
            systemId = Identifikator(identifikatorverdi = "S-1"),
        )
        val fromAdapter = listOf(
            "https://api.felleskomponent.no/arkiv/noark/sak/mappeid/2026%2F42",
            "https://api.felleskomponent.no/arkiv/noark/sak/systemid/S-1",
        )
        fromAdapter.forEach { sak.addLink(FintResource.SELF, self(it)) }

        sak.removeSelfLinks()
        assertTrue(sak.links.isEmpty())

        val rebuilt = buildList {
            sak.visitIdentifikators { field, value ->
                val link = Link(idField = field.lowercase(), idValue = value)
                add(link.href("https://api.felleskomponent.no", sak.metadata.path!!, encode(value)))
            }
        }
        assertEquals(fromAdapter, rebuilt)
    }

    @Test
    fun `no model relation is named self`() {
        val clashes = FintModel.types
            .filterIsInstance<FintResourceMetadata>()
            .flatMap { it.relations }
            .filter { it.name.equals(FintResource.SELF, ignoreCase = true) }

        assertTrue(clashes.isEmpty())
    }

    private fun encode(value: String): String =
        java.net.URLEncoder.encode(value, Charsets.UTF_8).replace("+", "%20")
}
