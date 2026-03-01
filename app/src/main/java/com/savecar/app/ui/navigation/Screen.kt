package com.savecar.app.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddVehicle : Screen("add_vehicle")
    data object EditVehicle : Screen("edit_vehicle/{vehicleId}") {
        fun createRoute(vehicleId: Long) = "edit_vehicle/$vehicleId"
    }
    data object VehicleDetail : Screen("vehicle_detail/{vehicleId}") {
        fun createRoute(vehicleId: Long) = "vehicle_detail/$vehicleId"
    }
    data object AddMaintenanceRecord : Screen("add_maintenance/{vehicleId}") {
        fun createRoute(vehicleId: Long) = "add_maintenance/$vehicleId"
    }
    data object EditMaintenanceRecord : Screen("edit_maintenance/{recordId}") {
        fun createRoute(recordId: Long) = "edit_maintenance/$recordId"
    }
}
