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
import org.robolectric.RuntimeEnvironment
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
//@Config(shadows = [ShadowShadowEnvironment::class])
class NetworkRecordingTest {
    private lateinit var mainScope: MainScope
    private lateinit var networkRecorder: NetworkRecorder


    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        mainScope = MainScopeImpl()
        networkRecorder = mainScope.networkRecorder()


        RuntimeEnvironment.setTempDirectory(NotTempDirectory())
        networkRecorder.startRecording(File("src/test/assets"))
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