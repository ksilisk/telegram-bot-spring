# Security Policy

Thank you for taking the time to help improve the security of **telegram-bot-spring-boot-starter**.

This document describes how to report a security vulnerability and what you can expect from the maintainers.

---

## Supported Versions

Security fixes are generally provided only for the most recent release line.

| Version                 | Supported for security fixes |
|-------------------------|------------------------------|
| Latest released version | ✅ Yes                        |
| Older versions          | ❌ No                         |

If you are using an older version, please upgrade to the latest release before reporting a vulnerability, if possible.

---

## Reporting a Vulnerability

If you discover a security vulnerability, **please do not open a public GitHub issue**.

To report a possible security issue, contact the project maintainer directly:

- Email: **shaliko9@yandex.ru**

Please include as much of the following information as you can:

- A description of the vulnerability and its potential impact
- Steps to reproduce the issue (or a minimal sample project / configuration)
- The version of `telegram-bot-spring-boot-starter` you are using
- The version of Spring Boot and Java
- Any relevant logs or stack traces (with sensitive data removed)

If you prefer, you may also use GitHub's **"Report a vulnerability"** feature to create a private security advisory instead of sending an email.

---

## What to Expect

- We will acknowledge your report as soon as reasonably possible, typically within **7 days**.
- We will investigate the issue and work on a fix or mitigation if the vulnerability is confirmed.
- We may contact you for additional information to reproduce or better understand the impact.
- Once a fix is ready, we will:
    - Release a new version of the library, and
    - Optionally publish a security advisory describing the issue and the resolution.

Where appropriate, and if you agree, we may credit you in the release notes or security advisory.

---

## Responsible Disclosure

We kindly ask that you:

- Give us a reasonable amount of time to investigate and fix the issue before any public disclosure.
- Avoid exploiting the vulnerability beyond what is necessary to demonstrate it.
- Avoid accessing, modifying, or deleting data that does not belong to you.

We are committed to handling security reports seriously and respectfully, and to working with you on a coordinated disclosure.

---

## Scope

This security policy applies to vulnerabilities in:

- The source code and configuration of **telegram-bot-spring-boot-starter**

It does **not** cover:

- Vulnerabilities in the Telegram Bot API itself
- Vulnerabilities in applications built _using_ this starter
- Vulnerabilities in third-party dependencies, unless the starter uses them in an unsafe way

If you are unsure whether an issue is in scope, feel free to contact us anyway.