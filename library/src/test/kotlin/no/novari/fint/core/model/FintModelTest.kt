package no.novari.fint.core.model

import no.novari.fint.core.model.utdanning.elev.Elev
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FintModelTest {

    @Test
    fun `byPath resolves a resource`() {
        assertSame(Elev.Metadata, FintModel.byPath("utdanning/elev/elev"))
    }

    @Test
    fun `byPath normalizes case and surrounding slashes`() {
        assertSame(Elev.Metadata, FintModel.byPath("/Utdanning/Elev/Elev/"))
    }

    @Test
    fun `byPath returns null for unknown resources`() {
        assertNull(FintModel.byPath("utdanning/elev/finnesikke"))
    }

    @Test
    fun `byRef and byType agree`() {
        assertSame(FintModel.byRef("utdanning-elev:Elev"), FintModel.byType(Elev::class))
    }

    @Test
    fun `every resource path round-trips through byPath`() {
        assertTrue(FintModel.resources.isNotEmpty())
        FintModel.resources.forEach { meta ->
            meta.path?.let { assertSame(meta, FintModel.byPath(it)) }
        }
    }

    @Test
    fun `every relation with a targetPath resolves to metadata`() {
        FintModel.resources.flatMap { it.relations }.forEach { relation ->
            if (relation.targetPath != null) {
                assertNotNull(relation.targetMetadata, "unresolvable target for ${relation.name}")
            }
        }
    }
}
