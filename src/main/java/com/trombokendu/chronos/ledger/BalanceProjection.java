package com.trombokendu.chronos.ledger;
import com.trombokendu.chronos.domain.*;
import java.util.*;
public final class BalanceProjection implements Projection<BalanceProjection.State> {
 public record State(Money balance,Map<UUID,TransactionPosted> active){public State{active=Map.copyOf(active);}}
 public State empty(){return new State(Money.ZERO,Map.of());}
 public State apply(State state,LedgerEvent event){Map<UUID,TransactionPosted> active=new HashMap<>(state.active());Money balance=state.balance();switch(event){case TransactionPosted x->{active.put(x.eventId(),x);balance=balance.plus(x.kind()==Kind.INCOME?x.amount():x.amount().negate());}case TransactionVoided x->{TransactionPosted original=active.remove(x.originalEventId());if(original!=null)balance=balance.plus(original.kind()==Kind.INCOME?original.amount().negate():original.amount());}case BudgetSet x->{}case RecurringRuleDefined x->{}}return new State(balance,active);}
}
