package no.novari.fint.core.model

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PathsTest {

    @Test
    fun `contains every resource's own path`() {
        assertContains(FintModel.paths, "utdanning/elev/elev")
        assertContains(FintModel.paths, "felles/kodeverk/iso/landkode")
    }

    @Test
    fun `a common resource is served under whoever links to it`() {
        assertContains(FintModel.paths, "utdanning/elev/person")
    }

    @Test
    fun `the walk follows relations between common resources`() {
        assertContains(FintModel.paths, "utdanning/elev/kontaktperson")
        assertContains(FintModel.paths, "administrasjon/personal/kontaktperson")
    }

    @Test
    fun `every three-segment path resolves through byPath`() {
        FintModel.paths.forEach { path ->
            val segments = path.split("/")
            if (segments.size == 3) {
                assertNotNull(FintModel.byPath(segments[0], segments[1], segments[2]), "$path does not resolve")
            }
        }
    }

    @Test
    fun `kontaktperson is projected into every package that can reach it`() {
        assertEquals(
            setOf(
                "utdanning/elev/kontaktperson",
                "utdanning/larling/kontaktperson",
                "utdanning/ot/kontaktperson",
                "administrasjon/personal/kontaktperson",
                "arkiv/personal/kontaktperson",
                "okonomi/regnskap/kontaktperson",
                "personvern/samtykke/kontaktperson",
            ),
            FintModel.paths.filter { it.endsWith("/kontaktperson") }.toSet(),
        )
    }

    @Test
    fun `the current model serves 180 paths`() {
        assertEquals(180, FintModel.paths.size)
    }
}
