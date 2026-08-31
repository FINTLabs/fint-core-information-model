package no.novari.fint.core.model

import no.novari.fint.core.model.felles.kodeverk.iso.Landkode
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class RefsTest {

    @Test
    fun `refs mirrors paths one to one`() {
        assertEquals(FintModel.paths.size, FintModel.refs.size)
        assertEquals(180, FintModel.refs.size)
    }

    @Test
    fun `contains own, projected and collapsed identities`() {
        assertContains(FintModel.refs, FintResourceRef("utdanning", "elev", "elev"))
        assertContains(FintModel.refs, FintResourceRef("utdanning", "elev", "person"))
        assertContains(FintModel.refs, FintResourceRef("utdanning", "elev", "kontaktperson"))
        assertContains(FintModel.refs, FintResourceRef("felles", "kodeverk", "landkode"))
    }

    @Test
    fun `every identity resolves through byPath`() {
        FintModel.refs.forEach { ref ->
            assertNotNull(
                FintModel.byPath(ref.domainName, ref.packageName, ref.resourceName),
                "$ref does not resolve",
            )
        }
    }

    @Test
    fun `byPath finds an iso resource at its identity`() {
        assertSame(Landkode.Metadata, FintModel.byPath("felles", "kodeverk", "landkode"))
        assertSame(Landkode.Metadata, FintModel.byPath("Felles", "KODEVERK", "Landkode"))
    }

    @Test
    fun `refOf answers the identity of a served path`() {
        assertEquals(FintResourceRef("utdanning", "elev", "elev"), FintModel.refOf("utdanning/elev/elev"))
        assertEquals(FintResourceRef("utdanning", "elev", "person"), FintModel.refOf("/Utdanning/Elev/Person/"))
        assertEquals(FintResourceRef("felles", "kodeverk", "landkode"), FintModel.refOf("felles/kodeverk/iso/landkode"))
    }

    @Test
    fun `refOf answers null off the served set`() {
        assertNull(FintModel.refOf("utdanning/elev/finnesikke"))
        assertNull(FintModel.refOf("felles/kodeverk/landkode"))
        assertNull(FintModel.refOf(""))
    }
}
