# OmniBank Core

A high-performance, modular core banking simulation engine built in Java. The system models complex retail banking infrastructure, including multi-currency account management, graph-based currency exchange optimization, transaction auditing, automated commercial spending tracking, and an extensible behavioral command workflow.

## Key Architecture & Design Choices

### 1. Unified Ledger & Data Topology
The banking core manages a highly structured data layer within the central `Bank` coordinator to guarantee deterministic state tracking and efficient data lookup.
* **Linked Operations:** Employs a `LinkedHashMap` for the user ledger to preserve the chronological order of user enrollment while maintaining $O(1)$ retrieval speeds.
* **Decoupled Lookups:** Implements granular memory indexes using distinct `HashMap` structures to look up accounts by IBAN, cards by card number, and custom account aliases independently.
* **Encapsulated Relations:** Embeds encapsulated collection arrays within specific `User` and `Account` instances to isolate localized transaction streams and active card pools.

### 2. Behavioral Execution Layer & Command Abstraction
System commands are fully decoupled from core business domains using an extensible behavioral design to ensure modular scalability and audit compliance.
* **Command Pattern:** Encapsulates unique bank activities into isolated command components extending a unified abstract `Command` layout carrying uniform processing timestamps.
* **Event Sourcing:** Every transactional trigger records an immutable, strongly typed `Transaction` entity into the target account's internal ledger.
* **Polymorphic Reporting:** Leverages structural polymorphism inside the `Transaction` hierarchy to handle specialized reporting visibility, filtering events dynamically via properties like `displayedInSpendingReports()`.

### 3. Graph-Theoretic Currency Exchange Pipeline
To manage cross-border currency conversions across complex financial pairs, the backend implements an advanced graph topology powered by the `JGraphT` library.
* **Adjacency Mapping:** Models active fiat types as vertices within a `DefaultDirectedWeightedGraph`, mapping explicitly provided exchange parameters as directional weights.
* **Matrix Integrity:** Automates data completion at initialization by computing and inserting the inverse rate coefficient ($1.0 / \text{directRate}$) for every pair.
* **Arbitrage Resolution:** Employs a Breadth-First Search (`BFSShortestPath`) traversal strategy to identify the path with the minimal edge count between distant foreign tokens, executing continuous edge multiplication across the path to transform balances precisely.

### 4. Categorized Accounting & Financial Reporting
Auditing modules generate time-bounded, detailed financial statements across varying retail criteria.
* **Chronological Auditing:** Evaluates classic transactional records by traversing internal history blocks sequentially, breaking execution lines the moment a timestamp limits threshold is exceeded.
* **Lexicographical Grouping:** Uses an ordered `TreeMap` inside the `Account` layer to map commercial activities, automatically organizing merchant categories alphabetically.
* **Spend Analysis Matrix:** Computes precise expenditure indicators by cross-referencing nested `CommerciantPaymentData` nodes with strict chronological date parameters.

## Project Structure


```

├── bank/           # Core domain topology (User, Bank, Card, CurrencyExchanger)
│   └── accounts/   # Specialized banking product models (Account, SavingsAccount)
├── checker/        # Code style profiles and validation setups
├── commands/       # Pattern command modules and execution wrappers
├── exceptions/     # Domain-specific runtime exception definitions
├── fileio/         # I/O schema layouts for parsing source JSON feeds
└── transactions/   # Strongly typed event ledger components

```

## System Requirements & Build Automation
* **Engine Environment:** Java SDK 21
* **Dependency & Lifecycle Management:** Apache Maven
* **Core Frameworks & Tools:** JGraphT (Directed weighted graphs), Jackson (JSON mapping), Lombok

### Compilation
Build the production artifact using the standard Maven pipeline:

```bash
mvn clean compile
```

### Execution & Testing

The repository includes comprehensive automated checkstyle routines to maintain structural readability and compliance:

```bash
mvn clean install
chmod +x src/main/resources/checkstyle.sh && ./src/main/resources/checkstyle.sh
```

## Acknowledgments
* The automated validation testing suite and foundational `fileio` data-parsing classes were designed and provided by the *Object Oriented Programming Course* team at the *Faculty of Automatic Control and Computer Science, National University of Science and Technology Politehnica Bucharest*.
* This implementation was developed independently as a showcase of production-ready design patterns and algorithmic optimization.
