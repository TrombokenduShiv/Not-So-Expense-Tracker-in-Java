package com.trombokendu.chronos.ledger;
import com.trombokendu.chronos.domain.*;import java.time.*;import java.util.*;import java.util.concurrent.locks.*;
public final class EventStore {private final List<StoredEvent> events=new ArrayList<>();private final ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
 public StoredEvent append(LedgerEvent e){lock.writeLock().lock();try{String prev=events.isEmpty()?HashChain.GENESIS:events.get(events.size()-1).hash();StoredEvent s=new StoredEvent(events.size()+1L,e,prev,HashChain.next(prev,e));events.add(s);return s;}finally{lock.writeLock().unlock();}}
 public <S>S replay(Projection<S> p){return replayUpTo(Instant.MAX,p);}public <S>S replayUpTo(Instant t,Projection<S> p){lock.readLock().lock();try{S s=p.empty();for(StoredEvent e:events)if(!e.event().at().isAfter(t))s=p.apply(s,e.event());return s;}finally{lock.readLock().unlock();}}
 public List<StoredEvent> all(){lock.readLock().lock();try{return List.copyOf(events);}finally{lock.readLock().unlock();}}
}
