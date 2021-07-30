package com.example.networkrecording

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.networkrecording.databinding.ActivityMainBinding
import com.example.recorder.NetworkRecorder
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainScope: MainScope
    private lateinit var networkRecorder: NetworkRecorder

    // Using the viewModels() Kotlin property delegate from the activity-ktx
    // artifact to retrieve the ViewModel in the activity scope
    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainScope = MainScopeImpl()

        networkRecorder = mainScope.networkRecorder()

        getExternalFilesDir(null)?.let { root ->
            networkRecorder.startRecording(root)
        }
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener
        { controller, destination, arguments ->

            if (destination.label == "Home") {
                fetchRepo("Leland-Takamine")
            }

            if (destination.label == "Dashboard") {
                fetchRepo("ericliu001")
            }


            if (destination.label == "Notifications") {
                saveRecords()
            }
        })

    }

    fun fetchRepo(repoName: String) {
        mainScope.githubService().repos(repoName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.body()?.let { repos ->
                    viewModel.populateRepos(repos)
                }

                saveRecords() // TODO: 7/29/21 need to remove.
            }
    }

    fun saveRecords() {
        Completable.create {
            networkRecorder.saveRecordsToFiles()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
