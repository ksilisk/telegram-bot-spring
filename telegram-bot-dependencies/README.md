# **telegram-bot-dependencies**

Dependency Management BOM for **Telegram Bot Spring Boot Starter**.

This module provides a **Bill of Materials (BOM)** for managing versions of all Telegram Bot Starter modules and their aligned dependencies.

It is intended to be used via Maven <dependencyManagement>.

---

## **Purpose**

telegram-bot-dependencies ensures:
- consistent versions across all Telegram Bot modules
- aligned Spring Boot, Telegram API, and Micrometer dependencies
- simplified dependency declarations for consumers and internal modules

This module contains **no code** — only dependency version definitions.

---

## **Usage**

Import the BOM into your project:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.ksilisk</groupId>
            <artifactId>telegram-bot-dependencies</artifactId>
            <version>${telegram-bot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

After that, you can omit versions for Telegram Bot modules:

```xml
<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-webhook</artifactId>
</dependency>
```

---

## **What is included**

The BOM manages versions for:
- telegram-bot-core
- telegram-bot-long-polling
- telegram-bot-webhook
- telegram-bot-observability
- telegram-bot-spring-boot-autoconfigure
- telegram-bot-spring-boot-starter

It may also align selected third-party dependencies used internally.

---

## **When to use**

Use this BOM if:
- you depend on **multiple Telegram Bot modules**
- you want explicit and reproducible dependency versions
- you manage dependencies manually instead of relying on a parent POM

If you only use the starter and rely on Spring Boot’s dependency management, importing this BOM is optional.

---
