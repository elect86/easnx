package tutorials.images_and_icons


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.singleWindowApplication
import easnx.composeapp.generated.resources.Res
import easnx.composeapp.generated.resources.sample
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.nio.file.Paths

//@OptIn(ExperimentalResourceApi::class)
//fun main() = singleWindowApplication {
//    Image(painter = painterResource(Res.drawable.sample),
//          contentDescription = "Sample",
//          modifier = Modifier.fillMaxSize())
//}

@OptIn(ExperimentalResourceApi::class)
fun main() = singleWindowApplication {
    Image(painter = androidx.compose.ui.res.painterResource("network-vpn.svg"),
          contentDescription = "Sample",
          modifier = Modifier.fillMaxSize())
}