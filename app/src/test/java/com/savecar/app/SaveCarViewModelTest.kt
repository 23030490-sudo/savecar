package com.savecar.app

import com.savecar.app.data.local.entity.MaintenanceRecord
import com.savecar.app.data.local.entity.Vehicle
import com.savecar.app.data.repository.SaveCarRepository
import com.savecar.app.ui.viewmodel.SaveCarViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveCarViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SaveCarRepository
    private lateinit var viewModel: SaveCarViewModel

    private val sampleVehicle = Vehicle(
        id = 1L,
        brand = "Toyota",
        model = "Corolla",
        year = 2020,
        licensePlate = "ABC123"
    )

    private val sampleRecord = MaintenanceRecord(
        id = 1L,
        vehicleId = 1L,
        type = "Cambio de aceite",
        date = System.currentTimeMillis(),
        mileageAtService = 50000
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getAllVehicles() } returns flowOf(listOf(sampleVehicle))
        every { repository.getRecentRecords(any()) } returns flowOf(listOf(sampleRecord))
        every { repository.getVehicleById(any()) } returns flowOf(sampleVehicle)
        every { repository.getRecordsForVehicle(any()) } returns flowOf(listOf(sampleRecord))
        viewModel = SaveCarViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addVehicle calls repository insertVehicle`() = runTest {
        coEvery { repository.insertVehicle(any()) } returns 2L
        viewModel.addVehicle(sampleVehicle)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.insertVehicle(sampleVehicle) }
    }

    @Test
    fun `updateVehicle calls repository updateVehicle`() = runTest {
        viewModel.updateVehicle(sampleVehicle)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.updateVehicle(sampleVehicle) }
    }

    @Test
    fun `deleteVehicle calls repository deleteVehicle`() = runTest {
        viewModel.deleteVehicle(sampleVehicle)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.deleteVehicle(sampleVehicle) }
    }

    @Test
    fun `addMaintenanceRecord calls repository insertRecord`() = runTest {
        coEvery { repository.insertRecord(any()) } returns 1L
        viewModel.addMaintenanceRecord(sampleRecord)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.insertRecord(sampleRecord) }
    }

    @Test
    fun `updateMaintenanceRecord calls repository updateRecord`() = runTest {
        viewModel.updateMaintenanceRecord(sampleRecord)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.updateRecord(sampleRecord) }
    }

    @Test
    fun `deleteMaintenanceRecord calls repository deleteRecord`() = runTest {
        viewModel.deleteMaintenanceRecord(sampleRecord)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.deleteRecord(sampleRecord) }
    }

    @Test
    fun `selectVehicle updates selectedVehicleId`() = runTest {
        viewModel.selectVehicle(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(sampleVehicle, viewModel.selectedVehicle.value)
    }

    @Test
    fun `vehicles state is initialized from repository`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(listOf(sampleVehicle), viewModel.vehicles.value)
    }
}
