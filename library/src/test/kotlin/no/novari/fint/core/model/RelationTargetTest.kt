package no.novari.fint.core.model

import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.elev.Elevtilrettelegging
import no.novari.fint.core.model.utdanning.timeplan.Fag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RelationTargetTest {

    @Test
    fun `the target name is the resource, not the role the relation plays`() {
        val relation = Elevtilrettelegging.relations.first { it.name == "elev" }
        assertEquals("elev", relation.name)
        assertEquals("elevforhold", relation.targetName)
    }

    @Test
    fun `a relation named after its target answers the same either way`() {
        val relation = Elev.relations.first { it.name == "person" }
        assertEquals("person", relation.name)
        assertEquals("person", relation.targetName)
    }

    @Test
    fun `one relation name covers four different resources`() {
        val targets = FintModel.types
            .filterIsInstance<FintResourceMetadata>()
            .flatMap { it.relations }
            .filter { it.name == "gruppemedlemskap" }
            .mapNotNull { it.targetName }

        assertEquals(
            setOf(
                "kontaktlarergruppemedlemskap",
                "undervisningsgruppemedlemskap",
                "programomrademedlemskap",
                "eksamensgruppemedlemskap",
            ),
            targets.toSet(),
        )
    }

    @Test
    fun `a target that is no resource of its own has no name`() {
        val grepreferanse = Fag.relations.first { it.name == "grepreferanse" }
        assertNull(grepreferanse.targetName)
        assertTrue(grepreferanse.targetIdFields.isEmpty())
    }

    @Test
    fun `a target has a name exactly when it declares id fields`() {
        val relations = FintModel.types
            .filterIsInstance<FintResourceMetadata>()
            .flatMap { it.relations }
        assertTrue(relations.size > 400)

        val nameless = relations.filter { it.targetName == null }
        assertEquals(
            setOf("grepreferanse", "vigoreferanse"),
            nameless.map { it.target.simpleName!!.lowercase() }.toSet(),
        )
        assertTrue(nameless.all { it.targetIdFields.isEmpty() })
        assertTrue(relations.filter { it.targetName != null }.all { it.targetIdFields.isNotEmpty() })
    }

    @Test
    fun `the target name matches the last segment of the target path when there is one`() {
        val withPath = FintModel.types
            .filterIsInstance<FintResourceMetadata>()
            .flatMap { it.relations }
            .filter { it.targetPath != null }
        assertTrue(withPath.isNotEmpty())

        withPath.forEach {
            assertNotNull(it.targetName)
            assertEquals(it.targetName, it.targetPath!!.substringAfterLast('/'))
        }
    }
}
