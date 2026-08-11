package no.novari.fint.core.model

import no.novari.fint.core.model.arkiv.noark.Journalpost
import no.novari.fint.core.model.arkiv.noark.Korrespondansepart
import no.novari.fint.core.model.arkiv.noark.Skjerming
import no.novari.fint.core.model.arkiv.personal.Personalmappe
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.elev.Elev
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class NestedResourceTest {

    private fun visit(resource: FintResource): List<Pair<String, FintResource>> =
        buildList { resource.visitNested { field, nested -> add(field to nested) } }

    @Test
    fun `the walk reaches list and single fields and skips the unset ones`() {
        val journalpost = Journalpost(tittel = "Vedtak")
        val skjerming = Skjerming()
        val mappe = Personalmappe(
            journalpost = listOf(journalpost),
            skjerming = skjerming,
            systemId = Identifikator(identifikatorverdi = "S-1"),
        )

        val seen = visit(mappe)
        assertEquals(listOf("journalpost", "skjerming"), seen.map { it.first })
        assertSame(journalpost, seen[0].second)
        assertSame(skjerming, seen[1].second)
    }

    @Test
    fun `every element of a list is visited under the field's own name`() {
        val mappe = Personalmappe(journalpost = listOf(Journalpost(tittel = "a"), Journalpost(tittel = "b")))
        assertEquals(listOf("journalpost", "journalpost"), visit(mappe).map { it.first })
    }

    @Test
    fun `an empty list visits nothing`() {
        assertTrue(visit(Personalmappe(journalpost = emptyList())).isEmpty())
    }

    @Test
    fun `a resource with nothing nested visits nothing`() {
        assertTrue(visit(Journalpost(tittel = "no children")).isEmpty())
        assertTrue(visit(Elev(systemId = Identifikator(identifikatorverdi = "S-1"))).isEmpty())
    }

    @Test
    fun `the walk stops at one level and the caller recurses`() {
        val korrespondansepart = Korrespondansepart(korrespondansepartNavn = "Fylket")
        val journalpost = Journalpost(korrespondansepart = listOf(korrespondansepart))
        val mappe = Personalmappe(journalpost = listOf(journalpost))

        assertEquals(listOf("journalpost"), visit(mappe).map { it.first })

        val deep = buildList {
            fun walk(resource: FintResource) {
                resource.visitNested { field, nested ->
                    add(field)
                    walk(nested)
                }
            }
            walk(mappe)
        }
        assertEquals(listOf("journalpost", "korrespondansepart"), deep)
    }

    @Test
    fun `a resource-typed field on a non-arkiv resource is reached too`() {
        val elev = Elev(hybeladresse = Adresse(postnummer = "0150"))
        assertEquals(listOf("hybeladresse"), visit(elev).map { it.first })
    }
}
