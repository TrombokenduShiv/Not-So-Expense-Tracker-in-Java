package com.trombokendu.chronos.ledger;
import com.trombokendu.chronos.domain.*;import java.util.*;
public final class CategoryStatsProjection implements Projection<CategoryStatsProjection.State>{
 public record State(Map<Category,Money> totals,Map<UUID,TransactionPosted> active){public State{totals=Map.copyOf(totals);active=Map.copyOf(active);}}
 public State empty(){return new State(Map.of(),Map.of());}
 public State apply(State state,LedgerEvent event){Map<Category,Money> totals=new EnumMap<>(Category.class);totals.putAll(state.totals());Map<UUID,TransactionPosted> active=new HashMap<>(state.active());switch(event){case TransactionPosted x->{active.put(x.eventId(),x);if(x.kind()==Kind.EXPENSE)totals.merge(x.category(),x.amount(),Money::plus);}case TransactionVoided x->{TransactionPosted t=active.remove(x.originalEventId());if(t!=null&&t.kind()==Kind.EXPENSE){Money next=totals.getOrDefault(t.category(),Money.ZERO).minus(t.amount());if(next.equals(Money.ZERO))totals.remove(t.category());else totals.put(t.category(),next);}}case BudgetSet x->{}case RecurringRuleDefined x->{}}return new State(totals,active);}
}
