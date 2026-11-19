# Chronos Ledger

A Java 21 Swing personal-finance ledger demonstrating event sourcing, append-only corrections, hash-chain integrity, JDBC migrations, background writing and Monte Carlo forecasting.

## Build and run

Requirements: JDK 21 and Maven 3.9+.

```bash
mvn clean package
java -jar target/chronos-ledger.jar
java -cp target/chronos-ledger.jar com.trombokendu.chronos.selftest.SelfTests
```

On Windows use `;` instead of `:` in the classpath. Pattern-matching `switch` is final in Java 21, so no preview flags are required. SQLite driver: `org.xerial:sqlite-jdbc:3.54.1.0`.

## Architecture

`domain` owns immutable value types and the sealed event hierarchy. `ledger` contains append/replay and SHA-256 hash-chain logic. `persistence` contains SQLite configuration, versioned migrations, a bounded connection pool and a transaction scope. `analytics` contains Welford statistics, EWMA, empirical bootstrap/shrinkage/scheduled spend models and a parallel seeded simulation. `scenario`, `ingest`, `concurrency`, `ui`, and `app` separate policy, import, asynchronous coordination, Swing presentation and composition.

```text
 Swing EDT                    Folder Watcher
    |                               |
    +----------- submit ------------+
                    |
             BlockingQueue
                    |
          ledger-writer (single)
                    |
          EventStore / projections
                    |
           type-safe EventBus ------> EDT refresh
                    |
          SQLite WAL persistence

 Forecast UI -> ForkJoinPool chunks -> seeded independent RNG streams
 Scenario comparison -> virtual-thread executor
```

SQLite has a single-writer constraint. Chronos therefore confines writes to one writer thread rather than attempting to solve write races with broad locking. Reader connections are pooled; WAL permits readers to proceed while a writer commits. Swing components are only touched on the EDT. Forecast work belongs off the EDT and should publish completion with `SwingUtilities.invokeLater`.

## Data and integrity

Every ledger correction is another event; voiding references the original event ID. Hashes cover the previous hash plus a canonical UTF-8 event representation. Genesis is 64 zeroes. A chain detects accidental or malicious alteration after an anchor, but a local attacker able to rewrite the whole database can recompute the chain; export anchors to an independently controlled location for stronger evidence.

The composition root migrates the schema, replays JDBC events into the in-memory EventStore, and starts the single writer. The writer inserts each event in a UnitOfWork before publishing it to the in-memory store, so a failed database insert is not exposed as a committed in-memory event. Settings and anchor repositories are available for extension. The UI intentionally keeps the transaction-entry workflow small while the modular services remain independently testable.

## Legacy `.ser` migration caveat

The old stream refers to the binary name `ExpenseTracker$Transaction` in the default package. The project includes a default-package compatibility shim with the same serialVersionUID and serialized field names/types, plus `legacy.LegacyImporter` to translate those rows. Java serialization binds to class names and field descriptors, not merely equivalent source-level types. Keep the shim in place for the one-time import; do not deserialize untrusted `.ser` files. If the source stream came from a materially different historical build and cannot be read, export it using that original build to CSV and import the CSV instead.

## Inbox

Place CSV files in `./inbox`; expected columns: `date,kind,category,amount,note`. The parser uses deterministic UUIDs from row text for idempotent identity. `sample-inbox/sample.csv` is an example. Extend `StatementParser` for bank-specific formats. Production importer should move successfully imported files to an archive and write row-level failures to `rejected.csv`.

## Limitations to address before production

The project is a teaching baseline, not a regulated accounting product or a claim that every line of the requested specification is complete. Remaining work includes the dedicated Forecast/Scenario/Time Machine/Settlement/Anomaly panels; budget-breach probability and category-aware empirical simulation wiring; a correct category-targeted scenario comparator; settlement optimization; Benford soft warnings; CSV row-level rejection output and archive handling; anchor export; writer cancellation/shutdown coordination; and the full acceptance suite for legacy equivalence, Monte Carlo tolerance, and EDT responsiveness. The current simulation is a seeded daily-mean demonstrator, not yet the full category-by-category bootstrap engine. The core event store, JDBC event persistence/replay, hash verification, void-aware projections, Welford statistics and 5,000-event writer stress test are implemented.
