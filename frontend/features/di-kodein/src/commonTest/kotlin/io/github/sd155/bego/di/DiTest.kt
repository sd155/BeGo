package io.github.sd155.bego.di

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DiTest {
    @Test
    fun multiModuleResolveTest() {
        val moduleA = diModule(name = "module-a") {
            bindSingleton { Box(value = "root") }
        }
        val moduleB = diModule(name = "module-b") {
            bindSingleton { UsesBox(box = instance()) }
            bindSingleton(tag = "answer") { 42 }
        }

        val diTree = diTree {
            importAll(moduleA, moduleB)
        }

        val box = diTree.instance<Box>()
        val usesBox = diTree.instance<UsesBox>()

        assertEquals("root", box.value)
        assertSame(box, diTree.instance<Box>())
        assertSame(box, usesBox.box)
        assertEquals(42, diTree.instance<Int>(tag = "answer"))
    }

    private data class Box(val value: String)

    private data class UsesBox(val box: Box)
}
