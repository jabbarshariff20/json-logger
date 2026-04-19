# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Deploy

```bash
# Build and deploy to MuleSoft Anypoint Exchange
./deploy-to-exchange.sh <ANYPOINT_ORG_ID>

# Build only (without deploying)
mvn -f json-logger/pom.xml clean package

# Run tests
mvn -f json-logger/pom.xml test

# Run a single test class
mvn -f json-logger/pom.xml test -Dtest=ClassName
```

For EU Control Plane deployments, update `<distributionManagement>` URL in `json-logger/pom.xml` to use `https://maven.eu1.anypoint.mulesoft.com/...`.

## Architecture

This is a **Mule 4 Java SDK Extension** — a drop-in replacement for Mule's default Logger that outputs structured JSON logs. It is built as a Mule connector distributed via Anypoint Exchange.

### Schema-Driven POJO Generation

JSON schemas in `src/main/resources/schema/` are the source of truth for configuration and operation inputs. The `jsonschema2pojo-maven-plugin` generates Java POJOs from them at build time:
- `loggerConfig.json` → global config (app name/version, environment, masking, external destination)
- `loggerProcessor.json` → logger operation parameters (message, content, tracePoint, priority, correlationId)
- `loggerScopeProcessor.json` → scope processor parameters

Do not manually create or edit the generated POJO classes — modify the schemas instead.

### Core Flow

1. **`JsonloggerExtension`** — Entry point; declares configuration and destination types
2. **`JsonloggerConfiguration`** — Parses global config from `loggerConfig.json`-derived POJOs; manages timer state and external destination setup
3. **`JsonloggerOperations`** — 2,600+ line class containing two Mule operations:
   - `logger()` — Builds JSON output, applies data masking, logs via SLF4J, optionally routes to external destination
   - `scopeLogger()` — Wraps a flow segment to capture elapsed time

### External Destinations

`destinations/` contains implementations of the `Destination` interface for routing log events outside SLF4J:
- **JMS** (`JMSDestination`) — JMS queues/topics
- **AMQP** (`AMQPDestination`, `AMQDestination`) — ActiveMQ/AMQP
- **Anypoint MQ** — via REST client in `destinations/amq/client/`

Log events flow through `destinations/events/` (LogEvent → LogEventFactory → LogEventHandler → LogEventProducerWithTranslator).

### Singletons

Three singletons in `singleton/` manage shared state across the extension lifecycle:
- `ObjectMapperSingleton` — Jackson mapper configured with NULL inclusion
- `ConfigsSingleton` — Global logger config, accessible by scope processors
- `LogEventSingleton` — Shared log event instance

### Data Masking

`datamask/JsonMasker.java` redacts fields using JSONPath patterns or key name matching. Masking is configured globally in `loggerConfig.json`.

### DataWeave Module

`src/main/resources/modules/JSONLoggerModule.dwl` provides utility functions (e.g., payload stringification) available to Mule flows using this connector.
