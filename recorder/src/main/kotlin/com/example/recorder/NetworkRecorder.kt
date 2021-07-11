package com.example.recorder

import com.example.model.ResponseRecord
import com.example.recorder.repo.DiskRepo
import com.example.recorder.repo.MemoryRepo
import com.example.recorder.utils.fromHttpRequest
import com.example.recorder.utils.fromHttpResponse
import okhttp3.Request
import okhttp3.Response
import java.io.File

class NetworkRecorder(val interceptor: BaseInterceptor) {
    var matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    private lateinit var memoryRepo: MemoryRepo
    private lateinit var diskRepo: DiskRepo

    fun startRecording(root: File) {
        diskRepo = DiskRepo(root)
        memoryRepo = MemoryRepo()
        interceptor.networkRecorder = this
    }

    fun saveRecordsToFiles() {
        diskRepo.writeRecords(memoryRepo.getAllRecordings())
    }

    fun record(okhttpRequest: Request, okhttpResponse: Response) {
        val requestRecord = fromHttpRequest(okhttpRequest)
        val responseRecord = fromHttpResponse(okhttpResponse)
        memoryRepo.write(requestRecord, responseRecord)
    }

    fun retrieveResponse(
        okhttpRequest: Request
    ): List<ResponseRecord> {
        val requestRecord = fromHttpRequest(okhttpRequest)

        val fromMemory = memoryRepo.read(requestRecord, matchRule)
        if (fromMemory.isNotEmpty()) {
            return fromMemory
        }

        val fromDisk = diskRepo.read(requestRecord)
        memoryRepo.write(requestRecord, fromDisk)

        return memoryRepo.read(requestRecord, matchRule)
    }
}