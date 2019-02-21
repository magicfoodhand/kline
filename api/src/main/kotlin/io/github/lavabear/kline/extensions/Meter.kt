package io.github.lavabear.kline.extensions

import com.codahale.metrics.Meter

inline fun <T> Meter.mark(block: () -> T) : T {
    this.mark()
    return block()
}
