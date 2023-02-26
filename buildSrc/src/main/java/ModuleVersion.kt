import java.io.File
import java.util.Properties

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ModuleVersion(propertiesFile: File) : PropertiesFile(propertiesFile) {

    companion object {
        private const val VERSION_NAME = "version.name"
        private const val VERSION_CODE = "version.code"
    }

    val versionName: String
        get() = properties.getProperty(VERSION_NAME)
            ?: throw IllegalStateException("$VERSION_NAME property is missing")


    val versionCode: Int
        get() = properties.getProperty(VERSION_CODE)?.toInt()
            ?: throw IllegalStateException("$VERSION_CODE property is missing")


    fun updateVersion() {
        properties.run {
            setProperty(VERSION_CODE, (versionCode + 1).toString())
            val (major, minor, patch) = versionName.split(".").map { it.toInt() }
            setProperty(
                VERSION_NAME, when {
                    patch < 9 -> "$major.$minor.${patch + 1}"
                    minor < 9 -> "$major.${minor + 1}.0"
                    else -> "${major + 1}.0.$patch"
                }
            )
            super.store(null)
        }
    }

}