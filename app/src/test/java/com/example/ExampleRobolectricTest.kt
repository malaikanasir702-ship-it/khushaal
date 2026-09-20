package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppTab
import com.example.viewmodel.KhushhaalViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Khushhaal", appName)
  }

  @Test
  fun `verify ViewModel initial state and actions`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = KhushhaalViewModel(app)

    assertEquals(AppTab.HOME, viewModel.currentTab.value)
    assertEquals(62, viewModel.prosperityScore.value.score)
    assertEquals(55000L, viewModel.cashFlow.value.income)
    assertEquals(4, viewModel.envelopes.value.size)
    assertEquals(8, viewModel.availableSkills.size)

    // Select tab
    viewModel.selectTab(AppTab.MONEY)
    assertEquals(AppTab.MONEY, viewModel.currentTab.value)

    // Emergency deposit
    assertFalse(viewModel.emergencyDeposited.value)
    viewModel.depositEmergencyFund()
    assertTrue(viewModel.emergencyDeposited.value)
    assertEquals(65, viewModel.prosperityScore.value.score)
    assertEquals(8000L, viewModel.cashFlow.value.savings)

    // Quick expense log
    val prevExpense = viewModel.cashFlow.value.expenses
    viewModel.logQuickExpense("Ration", 500)
    assertEquals(prevExpense + 500, viewModel.cashFlow.value.expenses)

    // Select skill
    val foodSkill = viewModel.availableSkills.first { it.id == "food" }
    viewModel.selectSkill(foodSkill)
    assertEquals("food", viewModel.selectedSkill.value.id)

    // Toggle roadmap step
    val week2 = viewModel.roadmapSteps.value.first { it.week == 2 }
    assertFalse(week2.isCompleted)
    viewModel.toggleRoadmapStep(2)
    val week2After = viewModel.roadmapSteps.value.first { it.week == 2 }
    assertTrue(week2After.isCompleted)
  }

  @Test
  fun `verify navigation to dedicated destinations and back navigation`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = KhushhaalViewModel(app)

    assertEquals(com.example.model.AppDestination.TabView, viewModel.currentDestination.value)

    viewModel.navigateTo(com.example.model.AppDestination.GoalsAndKameti)
    assertEquals(com.example.model.AppDestination.GoalsAndKameti, viewModel.currentDestination.value)

    viewModel.navigateBack()
    assertEquals(com.example.model.AppDestination.TabView, viewModel.currentDestination.value)

    viewModel.navigateTo(com.example.model.AppDestination.BusinessKhata)
    assertEquals(com.example.model.AppDestination.BusinessKhata, viewModel.currentDestination.value)
    viewModel.navigateBack()
    assertEquals(com.example.model.AppDestination.TabView, viewModel.currentDestination.value)

    viewModel.navigateTo(com.example.model.AppDestination.EnvelopeSplit)
    assertEquals(com.example.model.AppDestination.EnvelopeSplit, viewModel.currentDestination.value)
    viewModel.navigateBack()
    assertEquals(com.example.model.AppDestination.TabView, viewModel.currentDestination.value)
  }

  @Test
  fun `verify Kameti and Emergency Locker actions`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = KhushhaalViewModel(app)

    // Kameti
    val unpaidKameti = viewModel.kametis.value.first { !it.isPaidThisMonth }
    viewModel.markKametiPaid(unpaidKameti.id)
    val afterKameti = viewModel.kametis.value.first { it.id == unpaidKameti.id }
    assertTrue(afterKameti.isPaidThisMonth)

    // Emergency Locker deposit
    val initialBalance = viewModel.emergencyLockerBalance.value
    viewModel.depositLocker(2000L)
    assertEquals(initialBalance + 2000L, viewModel.emergencyLockerBalance.value)

    // Emergency Locker withdraw
    viewModel.withdrawLocker(1000L)
    assertEquals(initialBalance + 1000L, viewModel.emergencyLockerBalance.value)
  }

  @Test
  fun `verify Fraud Academy quiz answer and score boost`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = KhushhaalViewModel(app)

    val initialScore = viewModel.prosperityScore.value.score
    viewModel.answerScam("scam-1", choseSafe = true)
    val scam1 = viewModel.scamSimulations.value.first { it.id == "scam-1" }
    assertTrue(scam1.isCompleted)
    assertEquals(true, scam1.isCorrect)
    assertEquals(initialScore + 2, viewModel.prosperityScore.value.score)
  }

  @Test
  fun `verify Business Khata customer orders management`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = KhushhaalViewModel(app)

    val initialOrderCount = viewModel.customerOrders.value.size
    viewModel.addNewCustomerOrder(
      name = "Zubaida Begum",
      phone = "0333-1122334",
      service = "Fancy Dupatta Piko & Stitching",
      total = 1800L,
      advance = 800L,
      dueDate = "28 Oct 2025"
    )

    assertEquals(initialOrderCount + 1, viewModel.customerOrders.value.size)
    val newOrder = viewModel.customerOrders.value.first()
    assertEquals("Zubaida Begum", newOrder.customerName)
    assertFalse(newOrder.isDelivered)

    viewModel.toggleOrderDelivered(newOrder.id)
    val afterDelivered = viewModel.customerOrders.value.first { it.id == newOrder.id }
    assertTrue(afterDelivered.isDelivered)
  }
}
