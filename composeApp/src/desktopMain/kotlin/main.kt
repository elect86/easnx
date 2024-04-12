import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import androidx.compose.ui.window.isTraySupported
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_loadjni
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_notify
import org.ietf.tools.getTOTPCode
import java.awt.Toolkit.getDefaultToolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.net.Inet4Address
import java.nio.file.Paths

val iconPath = File("./vpn-network-icon.webp").absolutePath
val user = System.getenv("HZDR_USER")
val Process.output
    get() = inputStream.readAllBytes().decodeToString()
val totp
    get() = getTOTPCode(System.getenv("HZDR_SECRET_KEY"))
const val alreadyRunning = "Another session of SNX is already running, aborting...\n"
fun notify(message: String, title: String = "snx") = jninotifications_notify("snx", title, message, iconPath)

fun main() {
    val pingPeriodMs = 1_000
    check(isTraySupported)

    val pwd = System.getenv("HZDR_PASSWORD")
    jninotifications_loadjni(Paths.get("").toAbsolutePath().toString())
    application {

        var time = 0L
        val dnsReachable = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            while (true)
                withFrameMillis {
                    if (it - time > pingPeriodMs) {
                        time = it
                        dnsReachable.value = Inet4Address.getByName(secondaryDNS).isReachable(500)
                    }
                }
        }

        Tray(icon = if (dnsReachable.value) green else red,
             menu = {
                 Item("Connect", onClick = {
                     val snx = ProcessBuilder("snx").start()
                     if (snx.output == alreadyRunning) {
                         notify(alreadyRunning)
                         snx.destroyForcibly()
                     } else {
                         ProcessBuilder("byobu-tmux", "new-session", "-d", "echo $pwd$totp | nohup snx -s cp.hzdr.de -u $user").start()
                         val output = ProcessBuilder("snx").start().output
//                         val connected = output == alreadyRunning || output == "Failed to init terminal!\n"
                         println(output)
                         notify("trying to connect..")
                     }
                 })
                 Item("Disconnect", onClick = {
                     val output = ProcessBuilder("snx", "-d").start().output
                     dnsReachable.value = false
                     // we get the same string begin, either successful or not
                     val title = "SNX - Disconnecting...\n"
                     notify(output.substringAfter(title), title)
                 })
                 Item("copy TOTP", onClick = { getDefaultToolkit().systemClipboard.setContents(StringSelection(totp), null) })
                 Item("Exit", onClick = ::exitApplication)
             })
    }
}

val red = TrayIcon(Color.Red)
val green = TrayIcon(Color.Green)
const val secondaryDNS = "149.220.4.2"

class TrayIcon(val color: Color) : Painter() {
    override val intrinsicSize = Size(256f, 256f)
    override fun DrawScope.onDraw() = drawRect(color)
}