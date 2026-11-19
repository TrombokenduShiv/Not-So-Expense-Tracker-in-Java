# OOP map

| Concept | Implementation |
|---|---|
| Encapsulation / immutability | `domain/Money`, event records; private state in services |
| Inheritance / abstract classes | `JdbcRepository<T,ID>`, `StatementParser` |
| Polymorphism | `SpendModel`, `Projection<S>`, `Policy`, `AnomalyRule` |
| Interfaces | `Repository`, `Migration`, `Projection`, `SpendModel` |
| Generics | `Repository<T,ID>`, `Projection<S>`, `EventBus.subscribe` |
| Sealed hierarchy / exhaustive pattern switch | `LedgerEvent`; `HashChain`, projections and table model |
| Nested classes / lambdas | `ForecastRequest.Builder`, pool lease, migration object, event callbacks |
| Exceptions | `ChronosException` and validation/integrity/persistence subclasses |
| Collections | `EnumMap`, `TreeMap`, `PriorityQueue` extension point, concurrent maps and blocking queues |
| Comparable / equality | `Money implements Comparable`; records provide value equality |
| Strategy | `SpendModel`, `AnomalyRule` |
| Decorator | `Policy.wrap` |
| Observer | `EventBus` |
| Template Method | `JdbcRepository` |
| Builder | `ForecastRequest.builder()` |
| Unit of Work | `UnitOfWork` |
| Repository | `Repository`, `JdbcRepository` |
| Proxy-like pool lease | `ConnectionPool.Lease` returns the delegate on close |
| Singleton | No global singleton is used. Clock should be injected by the composition root; a singleton would complicate deterministic tests. |

The brief's custom replay iterator, Benford rule, full settlement optimizer, and annotation-driven rule discovery are deliberate extension points rather than implemented features in this teaching baseline.
