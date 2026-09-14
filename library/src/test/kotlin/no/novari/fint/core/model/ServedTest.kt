package no.novari.fint.core.model

import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kodeverk.iso.Landkode
import no.novari.fint.core.model.utdanning.vurdering.Karakterverdi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ServedTest {

    @Test
    fun `served mirrors paths and refs one to one, in path order`() {
        assertEquals(180, FintModel.served.size)
        assertEquals(FintModel.paths.size, FintModel.served.size)
        assertEquals(FintModel.refs.size, FintModel.served.size)
        assertEquals(FintModel.paths.toList(), FintModel.served.map { it.path })
        assertEquals(FintModel.refs, FintModel.served.map { it.ref }.toSet())
    }

    @Test
    fun `every entry agrees with refOf, byPath and the last segment of its path`() {
        FintModel.served.forEach { (path, ref, metadata) ->
            assertEquals(ref, FintModel.refOf(path), "$path does not answer its own ref")
            assertSame(
                metadata,
                FintModel.byPath(ref.domainName, ref.packageName, ref.resourceName),
                "$ref does not answer its own metadata",
            )
            assertEquals(path.substringAfterLast('/'), metadata.name, "$path does not end in its resource name")
        }
    }

    @Test
    fun `resourcesIn on utdanning vurdering lists the package's own resources`() {
        val vurdering = FintModel.resourcesIn("utdanning", "vurdering")

        assertEquals(18, vurdering.size)
        assertTrue(vurdering.none { it.metadata.isCommon })
        assertEquals(
            listOf(
                "aktivitetsfravar",
                "anmerkninger",
                "eksamensgruppe",
                "eksamensgruppemedlemskap",
                "eksamensvurdering",
                "elevfravar",
                "elevvurdering",
                "fravarsoversikt",
                "fravarsregistrering",
                "halvarsfagvurdering",
                "halvarsordensvurdering",
                "karakterhistorie",
                "karakterverdi",
                "sensor",
                "sluttfagvurdering",
                "sluttordensvurdering",
                "underveisfagvurdering",
                "underveisordensvurdering",
            ),
            vurdering.map { it.ref.resourceName }.sorted(),
        )

        val karakterverdi = vurdering.single { it.path == "utdanning/vurdering/karakterverdi" }
        assertEquals(FintResourceRef("utdanning", "vurdering", "karakterverdi"), karakterverdi.ref)
        assertSame(Karakterverdi.Metadata, karakterverdi.metadata)
        assertEquals(listOf("systemId"), karakterverdi.metadata.idFields)
    }

    @Test
    fun `resourcesIn on utdanning elev includes the common resources reached from it`() {
        val elev = FintModel.resourcesIn("utdanning", "elev")

        assertEquals(14, elev.size)

        val person = elev.single { it.path == "utdanning/elev/person" }
        assertEquals(FintResourceRef("utdanning", "elev", "person"), person.ref)
        assertSame(Person.Metadata, person.metadata)
        assertTrue(person.metadata.isCommon)
        assertEquals(listOf("fodselsnummer"), person.metadata.idFields)

        assertNotNull(elev.singleOrNull { it.path == "utdanning/elev/kontaktperson" })
        assertEquals(
            listOf("brukernavn", "elevnummer", "feidenavn", "systemId"),
            elev.single { it.path == "utdanning/elev/elev" }.metadata.idFields,
        )
    }

    @Test
    fun `resourcesIn on felles kodeverk keeps the iso segment in the path and drops it from the ref`() {
        val kodeverk = FintModel.resourcesIn("felles", "kodeverk")

        assertEquals(6, kodeverk.size)

        val landkode = kodeverk.single { it.path == "felles/kodeverk/iso/landkode" }
        assertEquals(FintResourceRef("felles", "kodeverk", "landkode"), landkode.ref)
        assertSame(Landkode.Metadata, landkode.metadata)
        assertNull(kodeverk.firstOrNull { it.path == "felles/kodeverk/landkode" })
    }

    @Test
    fun `resourcesIn answers empty off the served set`() {
        assertEquals(emptyList(), FintModel.resourcesIn("foo", "bar"))
    }

    @Test
    fun `resourcesIn ignores case`() {
        assertEquals(
            FintModel.resourcesIn("utdanning", "vurdering"),
            FintModel.resourcesIn("Utdanning", "Vurdering"),
        )
    }

    @Test
    fun `resourcesIn keeps the order of served`() {
        val vurdering = FintModel.resourcesIn("utdanning", "vurdering")

        assertEquals(FintModel.served.filter { it in vurdering }, vurdering)
    }
}
