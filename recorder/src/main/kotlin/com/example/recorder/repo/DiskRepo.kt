package com.example.recorder.repo

import com.example.recorder.data.RequestRecord
import com.example.recorder.data.ResponseRecord
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

internal class DiskRepo(private val root: File) {

    fun writeRecords(records: Map<RequestRecord, List<ResponseRecord>>) {
        for ((request, responses) in records) {
            writeToFile(request, responses)
        }
    }

    private fun writeToFile(requestRecord: RequestRecord, responses: List<ResponseRecord>) {

        val encodedRecords = Json {
            allowStructuredMapKeys = true
        }.encodeToString(Pair(requestRecord, responses))

        val path = root.absolutePath + requestRecord.url + "/"
        File(path).mkdirs()
        val outputFile = File(path, "Record")
        if (outputFile.exists()) {
            outputFile.delete()
        }

        outputFile.createNewFile()

        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        try {
            var cursor = 0
            outputStream = FileOutputStream(outputFile)
            inputStream = encodedRecords.byteInputStream()
            inputStream.let {
                while (cursor != -1) {
                    cursor = inputStream.read()
                    outputStream.write(cursor)
                }
            }
        } finally {
            inputStream?.let { inputStream.close() }
            outputStream?.let { outputStream.close() }
        }
    }


}