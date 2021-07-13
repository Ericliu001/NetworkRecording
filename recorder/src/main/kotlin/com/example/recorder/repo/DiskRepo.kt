package com.example.recorder.repo

import com.example.Serializer
import com.example.model.BaseRequest
import com.example.model.BaseResponse
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException

private const val FILENAME = "record"

internal class DiskRepo<S : Serializer>(
    private val root: File,
    private val serializer: S
) {

    fun writeRecords(records: Map<BaseRequest, List<BaseResponse>>) {
        for ((request, responses) in records) {
            writeToFile(request, responses)
        }
    }

    private fun writeToFile(baseRequest: BaseRequest, responses: List<BaseResponse>) {

        val strBytes =
            serializer.encodeToByteArray(Pair(baseRequest, responses))

        val outputFile = getFileByRequestUrl(baseRequest)
        if (outputFile.exists()) {
            outputFile.delete()
        }

        outputFile.createNewFile()

        val stream = RandomAccessFile(outputFile, "rw")
        val channel: FileChannel = stream.channel

        val lock: FileLock?
        try {
            lock = channel.tryLock()
        } catch (e: OverlappingFileLockException) {
            stream.close()
            channel.close()
            return
        }

        val buffer: ByteBuffer = ByteBuffer.allocate(strBytes.size)
        buffer.put(strBytes)
        buffer.flip()
        channel.write(buffer)
        lock?.release()
        stream.close()
        channel.close()
    }

    private fun getFileByRequestUrl(baseRequest: BaseRequest): File {
        // TODO: 7/13/21 read all responses with different suffices
        val path = root.absolutePath + baseRequest.url
        File(path).mkdirs()
        val suffix = baseRequest.hashCode()
        return File(path, FILENAME + "_" + suffix)
    }

    fun read(
        baseRequest: BaseRequest,
    ): MutableList<BaseResponse> {
        val inputFile = getFileByRequestUrl(baseRequest)
        if (!inputFile.exists()) {
            return mutableListOf()
        }
        val pair = serializer.decodeFromByteArray(inputFile.readBytes())
        return pair.second
    }


}