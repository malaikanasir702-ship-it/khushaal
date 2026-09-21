package com.example.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface KhushhaalApiService {

    // --- App Config (company-managed, no auth required) ---
    @GET("api/config")
    suspend fun getAppConfig(): Response<AppConfigDto>

    // --- User Profile ---
    @GET("api/users/me")
    suspend fun getProfile(): Response<UserProfileDto>

    @PUT("api/users/me")
    suspend fun updateProfile(
        @Body update: UpdateProfileRequest
    ): Response<UserProfileDto>

    // --- Cash Flow ---
    @GET("api/cashflow/current")
    suspend fun getCashFlow(): Response<CashFlowDto>

    @PUT("api/cashflow/current")
    suspend fun updateCashFlow(
        @Body update: UpdateCashFlowRequest
    ): Response<CashFlowDto>

    // --- Transactions ---
    @GET("api/transactions")
    suspend fun getTransactions(
        @Query("month") month: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<PagedResponse<TransactionDto>>

    @POST("api/transactions")
    suspend fun addTransaction(
        @Body transaction: CreateTransactionRequest
    ): Response<TransactionDto>

    // --- Envelopes ---
    @GET("api/envelopes")
    suspend fun getEnvelopes(): Response<List<EnvelopeDto>>

    @PUT("api/envelopes/{id}")
    suspend fun updateEnvelope(
        @Path("id") envelopeId: String,
        @Body update: UpdateEnvelopeRequest
    ): Response<EnvelopeDto>

    // --- Kametis ---
    @GET("api/kametis")
    suspend fun getKametis(): Response<List<KametiDto>>

    @POST("api/kametis")
    suspend fun addKameti(
        @Body kameti: CreateKametiRequest
    ): Response<KametiDto>

    @PUT("api/kametis/{id}/pay")
    suspend fun markKametiPaid(
        @Path("id") kametiId: String
    ): Response<KametiDto>

    // --- Family Goals ---
    @GET("api/goals")
    suspend fun getGoals(): Response<List<GoalDto>>

    @POST("api/goals")
    suspend fun addGoal(
        @Body goal: CreateGoalRequest
    ): Response<GoalDto>

    @PUT("api/goals/{id}/contribute")
    suspend fun contributeToGoal(
        @Path("id") goalId: String,
        @Body contribution: ContributeRequest
    ): Response<GoalDto>

    // --- Debts ---
    @GET("api/debts")
    suspend fun getDebts(): Response<List<DebtDto>>

    @POST("api/debts")
    suspend fun addDebt(
        @Body debt: CreateDebtRequest
    ): Response<DebtDto>

    @PUT("api/debts/{id}/repay")
    suspend fun repayDebt(
        @Path("id") debtId: String,
        @Body repayment: RepayRequest
    ): Response<DebtDto>

    // --- Utility Bills ---
    @GET("api/bills")
    suspend fun getBills(): Response<List<BillDto>>

    @POST("api/bills")
    suspend fun addBill(
        @Body bill: CreateBillRequest
    ): Response<BillDto>

    @PUT("api/bills/{id}/pay")
    suspend fun markBillPaid(
        @Path("id") billId: String
    ): Response<BillDto>

    // --- Emergency Locker ---
    @GET("api/emergency-locker")
    suspend fun getLockerBalance(): Response<LockerDto>

    @POST("api/emergency-locker/deposit")
    suspend fun depositToLocker(
        @Body req: LockerTransactionRequest
    ): Response<LockerDto>

    @POST("api/emergency-locker/withdraw")
    suspend fun withdrawFromLocker(
        @Body req: LockerTransactionRequest
    ): Response<LockerDto>

    // --- Business Khata (Customer Orders) ---
    @GET("api/orders")
    suspend fun getOrders(): Response<List<OrderDto>>

    @POST("api/orders")
    suspend fun addOrder(
        @Body order: CreateOrderRequest
    ): Response<OrderDto>

    @PUT("api/orders/{id}")
    suspend fun updateOrder(
        @Path("id") orderId: String,
        @Body update: UpdateOrderRequest
    ): Response<OrderDto>

    // --- Prosperity Score ---
    @GET("api/prosperity")
    suspend fun getProsperity(): Response<ProsperityDto>

    // --- Notifications ---
    @GET("api/notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @PUT("api/notifications/{id}/read")
    suspend fun markNotificationRead(
        @Path("id") notifId: String
    ): Response<Unit>

    @DELETE("api/notifications")
    suspend fun clearNotifications(): Response<Unit>

    // --- Coach Messages ---
    @GET("api/coach/messages")
    suspend fun getCoachMessages(): Response<List<CoachMessageDto>>

    @POST("api/coach/messages")
    suspend fun sendCoachMessage(
        @Body message: SendMessageRequest
    ): Response<CoachMessageDto>
}
