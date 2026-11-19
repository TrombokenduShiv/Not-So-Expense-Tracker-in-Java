package com.trombokendu.chronos.domain;
import java.time.*;import java.util.*;
public sealed interface LedgerEvent permits TransactionPosted,TransactionVoided,BudgetSet,RecurringRuleDefined { UUID eventId(); Instant at(); }
