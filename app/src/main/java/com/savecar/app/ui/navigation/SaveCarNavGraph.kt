package com.savecar.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.savecar.app.ui.screen.AddEditMaintenanceScreen
import com.savecar.app.ui.screen.AddEditVehicleScreen
import com.savecar.app.ui.screen.HomeScreen
import com.savecar.app.ui.screen.VehicleDetailScreen
import com.savecar.app.ui.viewmodel.SaveCarViewModel

@Composable
fun SaveCarNavGraph(viewModel: SaveCarViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val vehicles by viewModel.vehicles.collectAsState()
            val recentRecords by viewModel.recentRecords.collectAsState()
            HomeScreen(
                vehicles = vehicles,
                recentRecords = recentRecords,
                onAddVehicle = { navController.navigate(Screen.AddVehicle.route) },
                onVehicleClick = { vehicle ->
                    viewModel.selectVehicle(vehicle.id)
                    navController.navigate(Screen.VehicleDetail.createRoute(vehicle.id))
                }
            )
        }

        composable(Screen.AddVehicle.route) {
            AddEditVehicleScreen(
                vehicle = null,
                onSave = { vehicle ->
                    viewModel.addVehicle(vehicle)
                    navController.navigateUp()
                },
                onCancel = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.EditVehicle.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
        ) {
            val selectedVehicle by viewModel.selectedVehicle.collectAsState()
            AddEditVehicleScreen(
                vehicle = selectedVehicle,
                onSave = { vehicle ->
                    viewModel.updateVehicle(vehicle)
                    navController.navigateUp()
                },
                onCancel = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.VehicleDetail.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: return@composable
            viewModel.selectVehicle(vehicleId)
            val vehicle by viewModel.selectedVehicle.collectAsState()
            val records by viewModel.vehicleRecords.collectAsState()
            VehicleDetailScreen(
                vehicle = vehicle,
                maintenanceRecords = records,
                onEditVehicle = {
                    navController.navigate(Screen.EditVehicle.createRoute(vehicleId))
                },
                onDeleteVehicle = { v ->
                    viewModel.deleteVehicle(v)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onAddRecord = {
                    navController.navigate(Screen.AddMaintenanceRecord.createRoute(vehicleId))
                },
                onEditRecord = { record ->
                    navController.navigate(Screen.EditMaintenanceRecord.createRoute(record.id))
                },
                onDeleteRecord = { record ->
                    viewModel.deleteMaintenanceRecord(record)
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.AddMaintenanceRecord.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: return@composable
            AddEditMaintenanceScreen(
                record = null,
                vehicleId = vehicleId,
                onSave = { record ->
                    viewModel.addMaintenanceRecord(record)
                    navController.navigateUp()
                },
                onCancel = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.EditMaintenanceRecord.route,
            arguments = listOf(navArgument("recordId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getLong("recordId") ?: return@composable
            val records by viewModel.vehicleRecords.collectAsState()
            val record = records.find { it.id == recordId }
            AddEditMaintenanceScreen(
                record = record,
                vehicleId = record?.vehicleId ?: 0L,
                onSave = { updated ->
                    viewModel.updateMaintenanceRecord(updated)
                    navController.navigateUp()
                },
                onCancel = { navController.navigateUp() }
            )
        }
    }
}
