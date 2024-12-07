package com.smh.measuretasks.helper

import android.os.Process
import android.os.SystemClock
import java.io.File

class CPUUsageMonitor {
    private var prevTotalTime = SystemClock.elapsedRealtime()
    private var prevProcessTime = Process.getElapsedCpuTime()

    fun monitorCPULoad(): Float {
        val currentTotalTime = SystemClock.elapsedRealtime()
        val currentProcessTime = Process.getElapsedCpuTime()

        val totalTimeDelta = currentTotalTime - prevTotalTime
        val processTimeDelta = currentProcessTime - prevProcessTime

        prevTotalTime = currentTotalTime
        prevProcessTime = currentProcessTime

        return if (totalTimeDelta > 0) {
            ((100f * processTimeDelta) / totalTimeDelta).coerceIn(0f, 100f)
        } else {
            0f
        }
    }

    fun monitorCPULoadPerCore(): List<String> {
        return (0..<coresCount).map { core ->
            val frequency = getCoreFrequency(core)
            val isOnline = isCoreOnline(core)

            if (isOnline) "Core $core\n$frequency MHz"
            else "Core $core\nOffline"
        }
    }

    private fun getCoreFrequency(core: Int): Int {
        val freqFile = File("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_cur_freq")
        return if (freqFile.exists()) {
            (freqFile.readText().trim().toIntOrNull() ?: 0) / 1000
        } else {
            0
        }
    }

    private fun isCoreOnline(core: Int): Boolean {
        val onlineFile = File("/sys/devices/system/cpu/cpu$core/online")
        return if (onlineFile.exists()) {
            onlineFile.readText().trim() == "1"
        } else {
            true
        }
    }
}


