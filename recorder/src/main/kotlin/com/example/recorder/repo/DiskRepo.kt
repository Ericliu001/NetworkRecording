package com.example.recorder.repo

import com.example.model.RequestModel
import com.example.model.ResponseModel
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

    fun writeRecords(records: Map<RequestModel, List<ResponseModel>>) {
        for ((request, responses) in records) {
            writeToFile(request, responses)
        }
    }

    private fun writeToFile(requestModel: RequestModel, responses: List<ResponseModel>) {

        val encodedRecords = Json {
            allowStructuredMapKeys = true
        }.encodeToString(Pair(requestModel, responses))

        val outputFile = getFileByRequestUrl(requestModel)
        if (outputFile.exists()) {
            outputFile.delete()
        }

        outputFile.createNewFile()

        val stream = RandomAccessFile(outputFile, "rw")
        val channel: FileChannel = stream.getChannel()

        var lock: FileLock?
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

    private fun getFileByRequestUrl(requestModel: RequestModel): File {
        // TODO: 7/13/21 read all responses with different suffices
        val path = root.absolutePath + requestModel.url
        File(path).mkdirs()
        val suffix = requestModel.hashCode()
        val outputFile = File(path, FILENAME + "_" + suffix)
        return outputFile
    }

    fun read(
        requestModel: RequestModel,
    ): MutableList<ResponseModel> {
        val inputFile = getFileByRequestUrl(requestModel)
        if (!inputFile.exists()) {
            return mutableListOf()
        }
        val outputString = String(inputFile.readBytes())
        val pair =
            Json.decodeFromString<Pair<RequestModel, MutableList<ResponseModel>>>(outputString)
        return pair.second
    }


}