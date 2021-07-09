package com.example.recorder

import com.example.recorder.data.RecordedRequest
import com.example.recorder.data.RecordedResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DiskRepo(private val root: File) {

    fun writeRecords(records: Map<RecordedRequest, List<RecordedResponse>>) {
        for ((request, responses) in records) {
            writeToFile(request, responses)
        }
    }

    private fun writeToFile(request: RecordedRequest, responses: List<RecordedResponse>) {
        val path = root.absolutePath + request.url + "/Record"

        val encodedRecords = Json {
            allowStructuredMapKeys = true
        }.encodeToString(Pair(request, responses))

        val outputFile = File(path)
        if (outputFile.exists()) {
            outputFile.delete()
            outputFile.createNewFile()
        } else {
            outputFile.createNewFile()
        }

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