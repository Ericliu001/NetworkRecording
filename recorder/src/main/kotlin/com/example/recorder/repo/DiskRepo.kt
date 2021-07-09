package com.example.recorder.repo

import com.example.recorder.data.RequestRecord
import com.example.recorder.data.ResponseRecord
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException

private const val FILENAME = "record"

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

        val outputFile = getFileByRequestUrl(requestRecord)
        if (outputFile.exists()) {
            outputFile.delete()
        }

        outputFile.createNewFile()

        val stream = RandomAccessFile(outputFile, "rw")
        val channel: FileChannel = stream.getChannel()

        var lock: FileLock? = null
        try {
            lock = channel.tryLock()
        } catch (e: OverlappingFileLockException) {
            stream.close()
            channel.close()
            return
        }

        val strBytes: ByteArray = encodedRecords.toByteArray()
        val buffer: ByteBuffer = ByteBuffer.allocate(strBytes.size)
        buffer.put(strBytes)
        buffer.flip()
        channel.write(buffer)
        lock?.release()
        stream.close()
        channel.close()
    }

    private fun getFileByRequestUrl(requestRecord: RequestRecord): File {
        val path = root.absolutePath + requestRecord.url
        File(path).mkdirs()
        val outputFile = File(path, FILENAME)
        return outputFile
    }

    fun read(
        requestRecord: RequestRecord,
    ): MutableList<ResponseRecord> {
        val inputFile = getFileByRequestUrl(requestRecord)
        if (!inputFile.exists()) {
            return mutableListOf()
        }
        val outputString = String(inputFile.readBytes())
        val pair =
            Json.decodeFromString<Pair<RequestRecord, MutableList<ResponseRecord>>>(outputString)
        return pair.second
    }


}