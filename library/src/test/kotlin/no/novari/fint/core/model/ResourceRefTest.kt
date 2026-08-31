package no.novari.fint.core.model

import no.novari.fint.core.model.arkiv.noark.Journalpost
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kodeverk.iso.Landkode
import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import no.novari.fint.core.model.utdanning.timeplan.Fag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ResourceRefTest {

    private val elevContext = FintResourceRef("utdanning", "elev", "elev")
    private val administrasjonContext = FintResourceRef("administrasjon", "personal", "personalressurs")

    @Test
    fun `a target with a path of its own answers with it, whatever the context`() {
        val elev = Elevforhold.relations.first { it.name == "elev" }

        assertEquals(FintResourceRef("utdanning", "elev", "elev"), elev.targetIn(elevContext))
        assertEquals(FintResourceRef("utdanning", "elev", "elev"), elev.targetIn(administrasjonContext))
    }

    @Test
    fun `a common target is served under the context it was reached through`() {
        val person = Elev.relations.first { it.name == "person" }

        assertEquals(FintResourceRef("utdanning", "elev", "person"), person.targetIn(elevContext))
        assertEquals(
            FintResourceRef("administrasjon", "personal", "person"),
            person.targetIn(administrasjonContext),
        )
    }

    @Test
    fun `a target that is no resource of its own has no ref`() {
        val external = Fag.relations.first { it.targetIdFields.isEmpty() }

        assertEquals("grepreferanse", external.name)
        assertNull(external.targetName)
        assertNull(external.targetIn(elevContext))
    }

    @Test
    fun `a target served inside another resource has no ref`() {
        val relation = FintRelation(
            name = "journalpost",
            target = Journalpost::class,
            targetPath = null,
            multiplicity = FintMultiplicity.ZERO_OR_MORE,
        )

        assertNull(Journalpost.path)
        assertTrue(!Journalpost.isCommon)
        assertNotNull(relation.targetMetadata)
        assertNull(relation.targetIn(elevContext))
    }

    @Test
    fun `refIn on a common resource takes the domain and package it was reached through`() {
        assertTrue(Person.isCommon)
        assertNull(Person.path)

        assertEquals(FintResourceRef("utdanning", "elev", "person"), Person.refIn(elevContext))
        assertEquals(FintResourceRef("administrasjon", "personal", "person"), Person.refIn(administrasjonContext))
    }

    @Test
    fun `refIn on a resource with a path ignores the context`() {
        assertEquals("utdanning/elev/elev", Elev.path)

        assertEquals(FintResourceRef("utdanning", "elev", "elev"), Elev.refIn(elevContext))
        assertEquals(FintResourceRef("utdanning", "elev", "elev"), Elev.refIn(administrasjonContext))
    }

    @Test
    fun `refIn on a resource served inside another one is null`() {
        assertNull(Journalpost.refIn(elevContext))
    }

    @Test
    fun `a target with more path segments than identity answers collapsed`() {
        val statsborgerskap = Person.relations.first { it.name == "statsborgerskap" }

        assertEquals("felles/kodeverk/iso/landkode", statsborgerskap.targetPath)
        assertEquals(FintResourceRef("felles", "kodeverk", "landkode"), statsborgerskap.targetIn(elevContext))
        assertEquals(FintResourceRef("felles", "kodeverk", "landkode"), Landkode.Metadata.refIn(elevContext))

        assertSame(Landkode.Metadata, FintModel.byPath("felles", "kodeverk", "landkode"))
        assertNull(FintModel.byPath("kodeverk", "iso", "landkode"))
    }

    @Test
    fun `the typed answer names the same identity as the string one`() {
        val contextPath = "utdanning/elev/elev"
        var agreed = 0
        var collapsed = 0

        FintModel.types.filterIsInstance<FintResourceMetadata>().forEach { metadata ->
            metadata.relations
                .filter { metadata.relation(it.name) === it }
                .forEach { relation ->
                    val where = "${metadata.ref}.${relation.name}"
                    val asString = metadata.relationPath(relation.name, contextPath)
                    val asRef = relation.targetIn(elevContext)

                    if (asString == null) {
                        assertNull(asRef, where)
                    } else {
                        val segments = asString.split('/')
                        assertEquals(
                            FintResourceRef(segments.first(), segments[1], segments.last()),
                            asRef,
                            where,
                        )
                        if (segments.size == 3) agreed++ else collapsed++
                    }
                }
        }

        assertTrue(agreed > 400)
        assertTrue(collapsed > 0)
    }
}
