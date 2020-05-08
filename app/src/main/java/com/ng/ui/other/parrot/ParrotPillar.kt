package com.webull.webulltv.webulldata.parrot

/**
 * 描述:
 * @author Jzn
 * @date 2020-05-07
 */

data class ParrotPillar(
        var name: String,
        var value: Number,
        var ratio: Float = 0.toFloat(),
        var color: Int = 0
) : Comparable<ParrotPillar> {

    var length: Float = 0.toFloat()
    var animLength: Float = 0.toFloat()


    override fun compareTo(other: ParrotPillar): Int {
        return other.value.toFloat().compareTo(this.value.toFloat())
    }

}
