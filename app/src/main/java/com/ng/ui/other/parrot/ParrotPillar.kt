package com.ng.ui.other.parrot

/**
 * 描述:
 * @author Jzn
 * @date 2020-05-07
 */

data class ParrotPillar(
        var name: String,
        var value: Number
) : Comparable<ParrotPillar> {

    var length: Float = 0.toFloat()
    var animLength: Float = 0.toFloat()
    var startColor: String = "#ffffff"
    var endColor: String = "#ffffff"
    var alpha: Int = 0
    var strAlpha: Int = 153
    var ratio: Float = 0.toFloat()
    var startAngle : Float = -90f

    override fun compareTo(other: ParrotPillar): Int {
        return other.value.toFloat().compareTo(this.value.toFloat())
    }

    fun equals(other: ParrotPillar): Boolean {
        return this.name == other.name && this.value == other.value
    }

    override fun toString(): String {
        return "ParrotPillar(name='$name', length=$length, animLength=$animLength)"
    }


}

