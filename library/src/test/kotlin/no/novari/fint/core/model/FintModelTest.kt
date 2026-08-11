package no.novari.fint.core.model

import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.utdanning.elev.Elev
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FintModelTest {

    @Test
    fun `byPath resolves a resource from its three path parts`() {
        assertSame(Elev.Metadata, FintModel.byPath("utdanning", "elev", "elev"))
    }

    @Test
    fun `byPath ignores case`() {
        assertSame(Elev.Metadata, FintModel.byPath("Utdanning", "Elev", "Elev"))
    }

    @Test
    fun `byPath returns null for unknown resources`() {
        assertNull(FintModel.byPath("utdanning", "elev", "finnesikke"))
    }

    @Test
    fun `every three-segment resource path round-trips through byPath`() {
        assertTrue(FintModel.resources.isNotEmpty())
        FintModel.resources.forEach { meta ->
            val segments = meta.path?.split("/") ?: return@forEach
            if (segments.size == 3) {
                assertSame(meta, FintModel.byPath(segments[0], segments[1], segments[2]))
                assertSame(
                    meta,
                    FintModel.byPath(segments[0].uppercase(), segments[1].uppercase(), segments[2].uppercase()),
                    "${meta.ref} is only reachable in the case its path happens to be stored in",
                )
            }
        }
    }

    @Test
    fun `resource metadata knows it describes a resource`() {
        val meta = FintModel.byPath("utdanning", "elev", "elev")
        assertNotNull(meta)
        val type: KClass<out FintResource> = meta.type
        assertEquals(Elev::class, type)
    }

    @Test
    fun `every bidirectional relation is answered from the other side`() {
        var pairs = 0
        FintModel.resources.forEach { meta ->
            meta.relations.forEach { relation ->
                val bidirectional = relation.bidirectional ?: return@forEach
                val target = relation.targetMetadata as? FintResourceMetadata
                assertNotNull(target, "${meta.ref}.${relation.name} points at a type that carries no relations")
                val inverse = target.relation(bidirectional.inverseName)
                assertNotNull(inverse, "${meta.ref}.${relation.name}: ${target.ref} has no ${bidirectional.inverseName}")
                assertEquals(
                    relation.name,
                    inverse.bidirectional?.inverseName,
                    "${meta.ref}.${relation.name}: ${target.ref}.${inverse.name} does not point back",
                )
                assertEquals(
                    inverse.multiplicity,
                    bidirectional.inverseMultiplicity,
                    "${meta.ref}.${relation.name}: baked inverseMultiplicity disagrees with ${target.ref}.${inverse.name}",
                )
                pairs++
            }
        }
        assertTrue(pairs > 0)
    }

    @Test
    fun `a relation to the resource's own type still goes both ways`() {
        val overordnet = Organisasjonselement.relations.first { it.name == "overordnet" }
        assertEquals("underordnet", overordnet.bidirectional?.inverseName)
        assertEquals(FintMultiplicity.ZERO_OR_MORE, overordnet.bidirectional?.inverseMultiplicity)
        assertEquals(Organisasjonselement::class, overordnet.target)
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
