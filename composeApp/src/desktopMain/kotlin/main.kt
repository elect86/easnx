import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_loadjni
import com.zoffcc.applications.jninotifications.NTFYActivity.jninotifications_notify
import easnx.composeapp.generated.resources.Res
import easnx.composeapp.generated.resources.vpn
import org.ietf.tools.getTOTPCode
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.awt.Toolkit.getDefaultToolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.nio.file.Paths

val iconPath = File("./vpn-network-icon.webp").absolutePath
val user = System.getenv("HZDR_USER")
val Process.output
    get() = inputStream.readAllBytes().decodeToString()
val totp
    get() = getTOTPCode(System.getenv("HZDR_SECRET_KEY"))
const val alreadyRunning = "Another session of SNX is already running, aborting...\n"
fun notify(message: String, title: String = "snx") = jninotifications_notify("snx", title, message, iconPath)

@OptIn(ExperimentalResourceApi::class)
fun main() {
    val pwd = System.getenv("HZDR_PASSWORD")
    jninotifications_loadjni(Paths.get("").toAbsolutePath().toString())
    application {
        Tray(icon = painterResource(Res.drawable.vpn),
             menu = {
                 Item("Connect", onClick = {
                     val snx = ProcessBuilder("snx").start()
                     if (snx.output == alreadyRunning) {
                         notify(alreadyRunning)
                         snx.destroyForcibly()
                     } else {
                         ProcessBuilder("byobu-tmux", "new-session", "-d", "echo $pwd$totp | nohup snx -s cp.hzdr.de -u $user").start()
                         val output = ProcessBuilder("snx").start().output
                         val connected = output == alreadyRunning || output == "Failed to init terminal!\n"
                         //                     println(output)
                         notify(if (connected) "connected" else "error")
                     }
                 })
                 Item("Disconnect", onClick = {
                     val output = ProcessBuilder("snx", "-d").start().output
                     // we get the same string begin, either successful or not
                     val title = "SNX - Disconnecting...\n"
                     notify(output.substringAfter(title), title)
                 })
                 Item("copy TOTP", onClick = { getDefaultToolkit().systemClipboard.setContents(StringSelection(totp), null) })
                 Item("Exit", onClick = ::exitApplication)
             })
    }
}