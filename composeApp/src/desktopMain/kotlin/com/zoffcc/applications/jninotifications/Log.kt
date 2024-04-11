package com.zoffcc.applications.jninotifications

import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

/** Mock com.zoffcc.applications.jninotifications.Log implementation for testing on non android host. */
object Log {
    /** Priority constant for the println method; use com.zoffcc.applications.jninotifications.Log.v. */
    const val VERBOSE: Int = 2

    /** Priority constant for the println method; use com.zoffcc.applications.jninotifications.Log.d. */
    const val DEBUG: Int = 3

    /** Priority constant for the println method; use com.zoffcc.applications.jninotifications.Log.i. */
    const val INFO: Int = 4

    /** Priority constant for the println method; use com.zoffcc.applications.jninotifications.Log.w. */
    const val WARN: Int = 5

    /** Priority constant for the println method; use com.zoffcc.applications.jninotifications.Log.e. */
    const val ERROR: Int = 6

    /** Priority constant for the println method. */
    const val ASSERT: Int = 7

    /**
     * Dummy, does nothing
     *
     * @param tag
     * @param msg
     * @return
     */
    fun D(tag: String?, msg: String?): Int = 0

    /**
     * Send a [.VERBOSE] log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun v(tag: String, msg: String): Int = println(LOG_ID_MAIN, VERBOSE, tag, msg)

    /**
     * Send a [.VERBOSE] log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun v(tag: String, msg: String, tr: Throwable?): Int =
        println(LOG_ID_MAIN, VERBOSE, tag, "$msg\n${getStackTraceString(tr)}")

    /**
     * Send a [.DEBUG] log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun d(tag: String, msg: String): Int = println(LOG_ID_MAIN, DEBUG, tag, msg)

    /**
     * Send a [.DEBUG] log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun d(tag: String, msg: String, tr: Throwable?): Int =
        println(LOG_ID_MAIN, DEBUG, tag, "$msg\n${getStackTraceString(tr)}")

    /**
     * Send an [.INFO] log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun i(tag: String, msg: String): Int = println(LOG_ID_MAIN, INFO, tag, msg)

    /**
     * Send a [.INFO] log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun i(tag: String, msg: String, tr: Throwable?): Int =
        println(LOG_ID_MAIN, INFO, tag, "$msg\n${getStackTraceString(tr)}")

    /**
     * Send a [.WARN] log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun w(tag: String, msg: String): Int = println(LOG_ID_MAIN, WARN, tag, msg)

    /**
     * Send a [.WARN] log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun w(tag: String, msg: String, tr: Throwable?): Int =
        println(LOG_ID_MAIN, WARN, tag, "$msg\n${getStackTraceString(tr)}")

    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    fun w(tag: String, tr: Throwable?): Int = println(LOG_ID_MAIN, WARN, tag, getStackTraceString(tr))

    /**
     * Send an [.ERROR] log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun e(tag: String, msg: String): Int = println(LOG_ID_MAIN, ERROR, tag, msg)

    /**
     * Send a [.ERROR] log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun e(tag: String, msg: String, tr: Throwable?): Int =
        println(LOG_ID_MAIN, ERROR, tag, "$msg\n${getStackTraceString(tr)}")

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null)
            return ""

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException)
                return ""
            t = t.cause
        }

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * Low-level logging call.
     *
     * @param priority The priority/type of this log message
     * @param tag      Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg      The message you would like logged.
     * @return The number of bytes written.
     */
    fun println(priority: Int, tag: String, msg: String): Int = println(LOG_ID_MAIN, priority, tag, msg)

    /**
     * @hide
     */
    const val LOG_ID_MAIN: Int = 0

    /**
     * @hide
     */
    const val LOG_ID_RADIO: Int = 1

    /**
     * @hide
     */
    const val LOG_ID_EVENTS: Int = 2

    /**
     * @hide
     */
    const val LOG_ID_SYSTEM: Int = 3

    /**
     * @hide
     */
    const val LOG_ID_CRASH: Int = 4

    fun println(bufID: Int, priority: Int, tag: String, msg: String): Int {
        val threadid = Thread.currentThread().id
        val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS")
        val now = Date()
        val datetime = sdf.format(now)
        println("$datetime:$threadid:$priority:$tag:$msg")

        return 0
    }
}

