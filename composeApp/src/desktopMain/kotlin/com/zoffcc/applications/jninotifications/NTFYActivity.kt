package com.zoffcc.applications.jninotifications;

import com.zoffcc.applications.jninotifications.NTFYActivity.TAG
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_loadjni
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_notify
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_version
import java.io.File
import java.nio.file.Paths
import java.util.*

object NTFYActivity {
    internal val TAG = "NTFYActivity"
    val Version: String = "0.99.2"

    external fun jninotifications_version(): String
    external fun jninotifications_notify(application: String?, title: String?,
                                         message: String?, iconFilenameFullpath: String?): Int

    fun jninotifications_loadjni(jnilibPath: String): Int {
        val linuxLibFilename = "$jnilibPath/" + when(OperatingSystem.current) {
            OperatingSystem.LINUX -> "libjni_notifications.so"
            OperatingSystem.RASPI -> "libjni_notifications_raspi.so"
            OperatingSystem.WINDOWS -> "jni_notificationsi.dll"
            OperatingSystem.MACOS -> "libjni_notifications.jnilib"
            OperatingSystem.MACARM -> "libjni_notifications_arm64.jnilib"
            else -> {
                Log.i(TAG, "OS:Unknown operating system: " + OperatingSystem.current)
                return -1
            }
        }

        try {
            System.load(linuxLibFilename)
            Log.i(TAG, "successfully loaded native library path: $linuxLibFilename")
            return 0
        } catch (e: UnsatisfiedLinkError) {
            Log.i(TAG, "loadLibrary libjni_notifications failed! path: $linuxLibFilename")
            e.printStackTrace()
            return -1
        }
    }

    /**
     * Utility class to allow OS determination
     *
     *
     * Created on Mar 11, 2010
     *
     * @author Eugene Ryzhikov
     */
    enum class OperatingSystem(private val tag: String) {
        WINDOWS("windows"), MACOS("mac"), MACARM("silicone"), RASPI("aarm64"), LINUX("linux"), UNIX("nix"), SOLARIS("solaris"),

        UNKNOWN("unknown") {
            override val isReal: Boolean
                get() = false
        };

        val isCurrent: Boolean
            get() = isReal && getName().lowercase(Locale.getDefault()).indexOf(tag) >= 0

        override fun toString(): String = String.format("%s v%s (%s)", getName(), version, architecture)

        protected open val isReal: Boolean
            get() = true

        companion object {
            fun getName(): String = System.getProperty("os.name")

            val version: String
                get() = System.getProperty("os.version")

            val architecture: String
                get() = System.getProperty("os.arch")

            /** @return current operating system or UNKNOWN if not found */
            val current: OperatingSystem
                get() {
                    for (os in entries)
                        if (os.isCurrent)
                            return when {
                                os == MACOS && architecture.equals("aarch64", ignoreCase = true) -> MACARM
                                os == LINUX && architecture.equals("aarch64", ignoreCase = true) -> RASPI
                                else -> os
                            }
                    return UNKNOWN
                }
        }
    }
}

fun main() {
    val loadjniRes = jninotifications_loadjni(Paths.get("").toAbsolutePath().toString())

    val iconFile = File("./icon-linux.png")
    val iconPath = iconFile.absolutePath

    Log.i(TAG, "jninotifications version: " + jninotifications_version())

    val s = 100L
    jninotifications_notify("test application", "title", "message", iconPath)
    Thread.sleep(s)

    jninotifications_notify(null, null, null, null)
    Thread.sleep(s)

    jninotifications_notify(null, "1b", "1c", "1d")
    Thread.sleep(s)

    jninotifications_notify("2a", null, "2c", "2d")
    Thread.sleep(s)

    jninotifications_notify("3a", "3b", null, "3d")
    Thread.sleep(s)

    jninotifications_notify("4a", "4b", "4c", null)
    Thread.sleep(s)

    jninotifications_notify("हिन्दी", "हिन्दी", "हिन्दी", iconPath)
    Thread.sleep(s)

    jninotifications_notify("iconpathhindi", "हिन्दी", "हिन्दी", "हिन्दी")
    // HINT: it should only show 4 notifications (and also not crash)
}
