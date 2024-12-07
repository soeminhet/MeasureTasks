package com.smh.measuretasks.helper

const val COMPUTE_PRIME_SUM_THRESHOLD = 5_000
const val MEASURE_DELAY = 1_500L
const val MEASURE_TIME = 10
const val MEASURE_STEP_TAG = "MEASURE_STEP"

const val PRIME_SUM_RANGE_END = 10_000_000
enum class ThreadType { IO, DEFAULT, FORK_JOIN, EXECUTOR }
val tableHeader = setOf("Type", "Min", "Mid", "Max")