package com.example.networkrecording

import org.robolectric.util.TempDirectory
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class NotTempDirectory(val testName: String) : TempDirectory() {
    private val rootPath = Paths.get("src/test/assets")


    override fun createIfNotExists(name: String?): Path {
        val path = rootPath.resolve(testName)
        try {
            Files.createDirectory(path)
        } catch (e: FileAlreadyExistsException) {
            // that's ok
            return path
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return path

    }

    override fun destroy() {
        /* no-op */
    }
}