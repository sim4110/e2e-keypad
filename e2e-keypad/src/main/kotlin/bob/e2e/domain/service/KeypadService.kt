package bob.e2e.domain.service

import bob.e2e.presentation.dto.KeypadItem
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream

import java.security.DigestException
import java.security.MessageDigest
import java.util.*
import javax.imageio.ImageIO
import javax.imageio.ImageTranscoder


@Service
class KeypadService {

    fun numRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun hashMap(data: String): String {
        val hash: ByteArray
        try {
            val sha = MessageDigest.getInstance("SHA-256")
            sha.update(data.toByteArray())
            hash = sha.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("Couldn't make digest of partial content")
        }
        return bytesToHex(hash)
    }

    fun bytesToHex(hash: ByteArray): String {
        return hash.joinToString("") { byte -> "%02x".format(byte) }
    }

    fun loadImage(key: String): BufferedImage {
        val imagePath = "/img/_$key.png"

        val inputStream = javaClass.getResourceAsStream(imagePath)
            ?: throw IllegalArgumentException("Image not found: $imagePath")

        return ImageIO.read(inputStream)
    }

    fun encodeImage(image: BufferedImage): String {
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "png", outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(imageBytes)
    }

    fun saveKeypadImage(image: BufferedImage, outputPath: String) {
        val outputFile = java.io.File(outputPath)
        outputFile.parentFile?.mkdirs()
        ImageIO.write(image, "png", outputFile)
    }

    fun keyPadHashMap(): List<KeypadItem> {
        val keyList = (0..9).map { it.toString() }.toMutableList().apply {
            add("blank")
            add("blank")
        }

        val keypadItems = mutableListOf<KeypadItem>()
        val images = mutableListOf<BufferedImage>()

        keyList.forEach { key ->
            val randomString = numRandomString(10)
            val hash = hashMap(randomString)
            val image = loadImage(key)
            images.add(image)

            val item = KeypadItem(
                number = key,
                randomString = hash
            )
            keypadItems.add(item)
        }

        // 키패드 항목들을 무작위로 섞음
        Collections.shuffle(keypadItems)
        Collections.shuffle(images)

        val keypadImage = makeKeypadImage(images)
        saveKeypadImage(keypadImage, "output/keypad.png")
        return keypadItems
    }

    fun makeKeypadImage(images: List<BufferedImage>): BufferedImage {
        val width = images[0].width
        val height = images[0].height

        val keyPadWidth = width * 4
        val keyPadHeight = height * 3

        val keypadImage = BufferedImage(keyPadWidth, keyPadHeight, BufferedImage.TYPE_INT_RGB)
        val g: Graphics2D = keypadImage.createGraphics()

        for (i in images.indices) {
            val x = (i % 4) * width
            val y = (i / 4) * height
            g.drawImage(images[i], x, y, null)
        }
        g.dispose()

        return keypadImage
    }
}