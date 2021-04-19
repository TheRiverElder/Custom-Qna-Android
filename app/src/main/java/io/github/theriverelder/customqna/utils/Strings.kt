package io.github.theriverelder.customqna.utils

fun Long.toUidString(): String = this.toString(16).padStart(16, '0')