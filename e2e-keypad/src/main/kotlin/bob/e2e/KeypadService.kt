package bob.e2e

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class KeypadService {
    private val keypadDir : Path = Paths.get("images")

    init{
        if(!Files.exists(keypadDir)){
            Files.createDirectories(keypadDir)
        }
    }

    fun loadKeypad(filename:String): Resource {
        val keypadPath = keypadDir.resolve(filename)
        return ClassPathResource(keypadPath.toString())
    }
}