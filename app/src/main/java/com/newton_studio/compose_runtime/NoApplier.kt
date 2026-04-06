package com.newton_studio.compose_runtime

import androidx.compose.runtime.Applier

class NoOpApplier : Applier<Unit> {
    override val current: Unit = Unit

    override fun clear() {}
    override fun down(node: Unit) {}
    override fun up() {}
    override fun insertBottomUp(index: Int, instance: Unit) {}
    override fun insertTopDown(index: Int, instance: Unit) {}
    override fun move(from: Int, to: Int, count: Int) {}
    override fun remove(index: Int, count: Int) {}
}