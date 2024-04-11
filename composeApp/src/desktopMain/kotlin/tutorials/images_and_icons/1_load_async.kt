package tutorials.images_and_icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import easnx.composeapp.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.skia.Image
import org.xml.sax.InputSource
import java.io.IOException
import java.net.URL

@OptIn(ExperimentalResourceApi::class)
fun main() = singleWindowApplication {
    val density = LocalDensity.current
    val bitmapPainter by produceState<BitmapPainter?>(initialValue = null) {
        val bytes = Res.readBytes("files/sample.png")
        value = BitmapPainter(loadImageBitmap(bytes))
    }
    val imageVector by produceState<ImageVector?>(initialValue = null) {
        val bytes = Res.readBytes("files/compose-logo.xml")
        value = loadXmlImageVector(bytes, density)
    }
    Column {
        bitmapPainter?.let {
            Image(painter = it,
                  contentDescription = "Sample",
                  contentScale = ContentScale.Fit,
                  modifier = Modifier.width(200.dp))
        }

        AsyncImage(load = { loadSvgPainter("https://github.com/JetBrains/compose-multiplatform/raw/master/artwork/idea-logo.svg", density) },
                   painterFor = { it },
                   contentDescription = "Idea logo",
                   contentScale = ContentScale.FillWidth,
                   modifier = Modifier.width(200.dp))

        imageVector?.let {
            Image(painter = rememberVectorPainter(it),
                  contentDescription = "Compose logo",
                  contentScale = ContentScale.FillWidth,
                  modifier = Modifier.width(200.dp))
        }
    }
}

@Composable
fun <T> AsyncImage(load: suspend () -> T,
                   painterFor: @Composable (T) -> Painter,
                   contentDescription: String,
                   modifier: Modifier = Modifier,
                   contentScale: ContentScale = ContentScale.Fit) {

    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null)
        Image(painter = painterFor(image!!),
              contentDescription = contentDescription,
              contentScale = contentScale,
              modifier = modifier)
}

/* Loading from file with java.io API */

fun loadImageBitmap(bytes: ByteArray): ImageBitmap =
    Image.makeFromEncoded(bytes).toComposeImageBitmap()

fun loadSvgPainter(bytes: ByteArray, density: Density): Painter =
    bytes.inputStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(bytes: ByteArray, density: Density): ImageVector =
    bytes.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

/* Loading from network with java.net API */

fun loadImageBitmap(url: String): ImageBitmap =
    URL(url).openStream().buffered().use { loadImageBitmap(it.readAllBytes()) }

fun loadSvgPainter(url: String, density: Density): Painter =
    URL(url).openStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(url: String, density: Density): ImageVector =
    URL(url).openStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

/* Loading from network with Ktor client API (https://ktor.io/docs/client.html). */

/*

suspend fun loadImageBitmap(url: String): ImageBitmap =
    urlStream(url).use(::loadImageBitmap)

suspend fun loadSvgPainter(url: String, density: Density): Painter =
    urlStream(url).use { loadSvgPainter(it, density) }

suspend fun loadXmlImageVector(url: String, density: Density): ImageVector =
    urlStream(url).use { loadXmlImageVector(InputSource(it), density) }

@OptIn(KtorExperimentalAPI::class)
private suspend fun urlStream(url: String) = HttpClient(CIO).use {
    ByteArrayInputStream(it.get(url))
}

 */