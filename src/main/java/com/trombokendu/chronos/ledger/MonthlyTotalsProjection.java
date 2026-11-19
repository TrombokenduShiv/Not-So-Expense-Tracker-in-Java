package com.trombokendu.chronos.ledger;
import com.trombokendu.chronos.domain.*;import java.time.*;import java.util.*;
public final class MonthlyTotalsProjection implements Projection<MonthlyTotalsProjection.State>{
 public record State(Map<YearMonth,Money> totals,Map<UUID,TransactionPosted> active){public State{totals=Map.copyOf(totals);active=Map.copyOf(active);}}
 public State empty(){return new State(Map.of(),Map.of());}
 public State apply(State state,LedgerEvent event){Map<YearMonth,Money> totals=new TreeMap<>(state.totals());Map<UUID,TransactionPosted> active=new HashMap<>(state.active());switch(event){case TransactionPosted x->{active.put(x.eventId(),x);if(x.kind()==Kind.EXPENSE)totals.merge(YearMonth.from(x.valueDate()),x.amount(),Money::plus);}case TransactionVoided x->{TransactionPosted t=active.remove(x.originalEventId());if(t!=null&&t.kind()==Kind.EXPENSE){YearMonth m=YearMonth.from(t.valueDate());Money next=totals.getOrDefault(m,Money.ZERO).minus(t.amount());if(next.equals(Money.ZERO))totals.remove(m);else totals.put(m,next);}}case BudgetSet x->{}case RecurringRuleDefined x->{}}return new State(totals,active);}
}
