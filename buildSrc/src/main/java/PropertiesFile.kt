import java.io.File
import java.util.Properties

open class PropertiesFile constructor(open val propertiesFile: File) {

    protected val properties = Properties().apply {
        propertiesFile.inputStream().use {
            load(it)
        }
    }

    protected fun store(comments: String? = null) {
        propertiesFile.outputStream().use {
            properties.store(it, comments)
        }
    }
}