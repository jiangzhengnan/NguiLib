package com.ng.ui.other.parrot

/**
 * 描述:
 * @author Jzn
 * @date 2020-05-07
 */

data class ParrotPillar(
        var name: String,
        var value: Number,
        var ratio: Float = 0.toFloat()
) : Comparable<ParrotPillar> {

    var length: Float = 0.toFloat()
    var animLength: Float = 0.toFloat()
    var startColor: String = "#ffffff"
    var endColor: String = "#ffffff"
    var alpha: Int = 0

    override fun compareTo(other: ParrotPillar): Int {
        return other.value.toFloat().compareTo(this.value.toFloat())
    }

}

