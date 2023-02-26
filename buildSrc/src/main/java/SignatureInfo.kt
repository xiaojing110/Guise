import org.gradle.kotlin.dsl.provideDelegate
import java.io.File
import java.nio.file.Files
import java.util.Base64
import kotlin.jvm.Throws

@Suppress("unused")
class SignatureInfo constructor(propertiesFile: File) : PropertiesFile(propertiesFile) {

    companion object {
        private const val SIGNATURE_STORE_FILE_PATH = "signature.store.file.path"
        private const val SIGNATURE_STORE_FILE_BASE64 = "signature.store.file.base64"
        private const val SIGNATURE_STORE_PASSWORD = "signature.store.password"
        private const val SIGNATURE_STORE_TYPE = "signature.store.type"
        private const val SIGNATURE_KEY_ALIAS = "signature.key.alias"
        private const val SIGNATURE_KEY_PASSWORD = "signature.key.password"
    }

    val storeFileFromPath: File
        @Throws(RuntimeException::class)
        get() {
            val path = properties.getProperty(SIGNATURE_STORE_FILE_PATH)
                ?: System.getenv(SIGNATURE_STORE_FILE_PATH)
                ?: throw RuntimeException(
                    "Signature store file path is not set in the properties file and system env. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
            return File(path)
        }


    val storeFileFromBase64: File
        @Throws(RuntimeException::class)
        get() {
            val base64 = properties.getProperty(SIGNATURE_STORE_FILE_BASE64)
                ?: System.getenv(SIGNATURE_STORE_FILE_BASE64)
                ?: throw RuntimeException(
                    "Signature store file base64 is not set in the properties file and system env. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
            val bytes = Base64.getDecoder().decode(base64)
            Files.createTempFile("signature", "jks").toFile().let {
                it.writeBytes(bytes)
                return it
            }
        }


    val storePassword: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(SIGNATURE_STORE_PASSWORD)
                ?: System.getenv(SIGNATURE_STORE_PASSWORD)
                ?: throw RuntimeException(
                    "Signature store password is not set in the properties file and system env. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }


    val storeType: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(SIGNATURE_STORE_TYPE)
                ?: System.getenv(SIGNATURE_STORE_TYPE)
                ?: throw RuntimeException(
                    "Signature store type is not set in the properties file and system env. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }


    val keyAlias: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(SIGNATURE_KEY_ALIAS)
                ?: System.getenv(SIGNATURE_KEY_ALIAS)
                ?: throw RuntimeException(
                    "Signature key alias is not set in the properties file and system env. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }


    val keyPassword: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(SIGNATURE_KEY_PASSWORD)
                ?: System.getenv(SIGNATURE_KEY_PASSWORD)
                ?: throw RuntimeException(
                    "Signature key password is not set in the properties file and system env. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }


}