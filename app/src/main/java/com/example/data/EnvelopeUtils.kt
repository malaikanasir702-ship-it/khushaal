package com.example.data

import com.example.model.Envelope
import kotlin.math.floor

object EnvelopeUtils {

    /**
     * Calculates the allocated rupee amount for each envelope based on total income and percentage.
     * Guaranteed invariant: sum(returned amounts) <= totalIncome, and totalIncome <= 0 -> all amounts = 0.
     */
    fun calculateEnvelopeAmounts(totalIncome: Long, envelopes: List<Envelope>): List<Envelope> {
        if (totalIncome <= 0L || envelopes.isEmpty()) {
            return envelopes.map { it.copy(amount = 0L) }
        }

        return envelopes.map { envelope ->
            val computedAmount = floor((totalIncome * envelope.percentage).toDouble() / 100.0).toLong()
            envelope.copy(amount = computedAmount)
        }
    }
}
