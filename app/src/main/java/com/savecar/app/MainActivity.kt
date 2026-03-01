package com.savecar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.savecar.app.ui.navigation.SaveCarNavGraph
import com.savecar.app.ui.theme.SaveCarTheme
import com.savecar.app.ui.viewmodel.SaveCarViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: SaveCarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as SaveCarApplication).repository
        viewModel = ViewModelProvider(
            this,
            SaveCarViewModel.Factory(repository)
        )[SaveCarViewModel::class.java]

        setContent {
            SaveCarTheme {
                SaveCarNavGraph(viewModel = viewModel)
            }
        }
    }
}
