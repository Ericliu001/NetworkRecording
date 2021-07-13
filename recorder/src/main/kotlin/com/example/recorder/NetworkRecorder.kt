package com.example.recorder

import com.example.KTSSerializer
import com.example.Serializer
import com.example.model.BaseResponse
import com.example.model.ProtoSerializer
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
    private lateinit var diskRepo: DiskRepo<out Serializer>

    fun startRecording(root: File) {
        diskRepo = DiskRepo(root, ProtoSerializer()) // TODO: 7/13/21 need to be able to configure this.
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
    ): List<BaseResponse> {
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