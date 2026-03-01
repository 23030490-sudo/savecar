package com.savecar.app

import com.savecar.app.data.local.dao.MaintenanceRecordDao
import com.savecar.app.data.local.dao.VehicleDao
import com.savecar.app.data.local.entity.MaintenanceRecord
import com.savecar.app.data.local.entity.Vehicle
import com.savecar.app.data.repository.SaveCarRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveCarRepositoryTest {

    private lateinit var vehicleDao: VehicleDao
    private lateinit var maintenanceRecordDao: MaintenanceRecordDao
    private lateinit var repository: SaveCarRepository

    private val sampleVehicle = Vehicle(
        id = 1L,
        brand = "Honda",
        model = "Civic",
        year = 2021,
        licensePlate = "XYZ789"
    )

    private val sampleRecord = MaintenanceRecord(
        id = 1L,
        vehicleId = 1L,
        type = "Revisión general",
        date = 1_700_000_000_000L,
        mileageAtService = 30000
    )

    @Before
    fun setUp() {
        vehicleDao = mockk(relaxed = true)
        maintenanceRecordDao = mockk(relaxed = true)
        repository = SaveCarRepository(vehicleDao, maintenanceRecordDao)
    }

    @Test
    fun `getAllVehicles returns vehicles from dao`() = runTest {
        every { vehicleDao.getAllVehicles() } returns flowOf(listOf(sampleVehicle))
        val result = repository.getAllVehicles().first()
        assertEquals(listOf(sampleVehicle), result)
    }

    @Test
    fun `getVehicleById returns vehicle from dao`() = runTest {
        every { vehicleDao.getVehicleById(1L) } returns flowOf(sampleVehicle)
        val result = repository.getVehicleById(1L).first()
        assertEquals(sampleVehicle, result)
    }

    @Test
    fun `insertVehicle delegates to dao`() = runTest {
        coEvery { vehicleDao.insertVehicle(sampleVehicle) } returns 1L
        val id = repository.insertVehicle(sampleVehicle)
        assertEquals(1L, id)
        coVerify { vehicleDao.insertVehicle(sampleVehicle) }
    }

    @Test
    fun `updateVehicle delegates to dao`() = runTest {
        repository.updateVehicle(sampleVehicle)
        coVerify { vehicleDao.updateVehicle(sampleVehicle) }
    }

    @Test
    fun `deleteVehicle delegates to dao`() = runTest {
        repository.deleteVehicle(sampleVehicle)
        coVerify { vehicleDao.deleteVehicle(sampleVehicle) }
    }

    @Test
    fun `getRecordsForVehicle returns records from dao`() = runTest {
        every { maintenanceRecordDao.getRecordsForVehicle(1L) } returns flowOf(listOf(sampleRecord))
        val result = repository.getRecordsForVehicle(1L).first()
        assertEquals(listOf(sampleRecord), result)
    }

    @Test
    fun `insertRecord delegates to dao`() = runTest {
        coEvery { maintenanceRecordDao.insertRecord(sampleRecord) } returns 1L
        val id = repository.insertRecord(sampleRecord)
        assertEquals(1L, id)
        coVerify { maintenanceRecordDao.insertRecord(sampleRecord) }
    }

    @Test
    fun `deleteRecord delegates to dao`() = runTest {
        repository.deleteRecord(sampleRecord)
        coVerify { maintenanceRecordDao.deleteRecord(sampleRecord) }
    }
}
