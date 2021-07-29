package com.example.networkrecording

import com.example.recorder.NetworkRecorder
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class NetworkRecordingTest {
    private lateinit var mainScope: MainScope
    private lateinit var networkRecorder: NetworkRecorder
    private val root = "src/test/resources/"


    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler({ Schedulers.trampoline() })
        mainScope = MainScopeImpl()
        networkRecorder = mainScope.networkRecorder()

        File(root).mkdirs()
        networkRecorder.startRecording(File(root))
    }

    @After
    fun tearDown() {
        networkRecorder.saveRecordsToFiles()
    }

    @Test
    fun name() {
        val controller = buildActivity(MainActivity::class.java).setup()
    }
}