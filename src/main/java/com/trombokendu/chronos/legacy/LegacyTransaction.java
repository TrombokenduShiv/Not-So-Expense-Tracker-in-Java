package com.trombokendu.chronos.legacy;
import java.math.BigDecimal;
import java.time.LocalDate;
/** Neutral domain transfer object; ExpenseTracker$Transaction remains the serialization shim. */
public record LegacyTransaction(int id, LocalDate date, String type, String category,
                                BigDecimal amount, String description) { }
