package no.novari.fint.core.model

import no.novari.fint.core.model.utdanning.elev.Elev
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FintModelTest {

    @Test
    fun byPathResolvesResource() {
        assertSame(Elev.Metadata, FintModel.byPath("utdanning/elev/elev"))
    }

    @Test
    fun byPathNormalizesCaseAndSlashes() {
        assertSame(Elev.Metadata, FintModel.byPath("/Utdanning/Elev/Elev/"))
    }

    @Test
    fun byPathMissesUnknownResource() {
        assertNull(FintModel.byPath("utdanning/elev/finnesikke"))
    }

    @Test
    fun byRefAndByTypeAgree() {
        assertSame(FintModel.byRef("utdanning-elev:Elev"), FintModel.byType(Elev::class))
    }

    @Test
    fun everyResourcePathRoundTripsThroughByPath() {
        assertTrue(FintModel.resources.isNotEmpty())
        FintModel.resources.forEach { meta ->
            meta.path?.let { assertSame(meta, FintModel.byPath(it)) }
        }
    }

    @Test
    fun everyRelationWithTargetPathResolvesToMetadata() {
        FintModel.resources.flatMap { it.relations }.forEach { relation ->
            if (relation.targetPath != null) {
                assertNotNull(relation.targetMetadata, "unresolvable target for ${relation.name}")
            }
        }
    }
}
