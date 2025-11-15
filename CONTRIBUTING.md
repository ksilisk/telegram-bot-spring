# Contributing to Telegram Bot Spring Boot Starter

Thank you for your interest in contributing! 💙
This project aims to provide a clean, production-ready Spring Boot Starter for building Telegram bots easily and idiomatically.

We welcome pull requests, feature ideas, and issue reports from the community.

---

## 🧭 Project Structure

- **telegram-bot-core/** — Core classes and Pengrad API adapter
- **telegram-bot-autoconfigure/** — Spring Boot AutoConfiguration module
- **telegram-bot-starter/** — The actual Starter used by end-user Spring Boot apps

---

## 🚀 Getting Started

1. **Fork** this repository and clone your fork locally:
   ```bash
   git clone https://github.com/ksilisk/telegram-bot-spring.git
   cd telegram-bot-spring
   ```

2. **Create a new branch** for your change:
   ```bash
   git checkout -b feature/my-feature
   ```

3. **Build the project** to make sure everything compiles:
   ```bash
   mvn clean verify
   ```

4. **Make your changes** and include tests when possible.

5. **Run all tests** before submitting:
   ```bash
   mvn test
   ```

6. **Commit using Conventional Commits** style (short-description):
   ```
   added fallback dispatcher
   fixed polling auto-configuration
   ```

7. **Push and open a Pull Request (PR)** against the `master` branch.

---

## 🌿 Working with Issues and Branches

If you are contributing from a **fork**, GitHub will not automatically create a linked branch inside this repository.
Instead, follow this process:

1. **Create your fork** of the repository (if not already).
2. **Create a branch in your fork**, ideally named to reference the related issue:
   ```
   feature/<issue-number>-<short-description>
   ```
   Examples:
   ```
   feature/8-auto-config-base
   fix/12-nullpointer-router
   docs/25-update-readme
   ```
   Use lowercase letters and hyphens for readability.

3. Keep the **branch name short and descriptive** — it helps reviewers understand the context at a glance.

4. When you open your **Pull Request**, include a link to the issue in the description:
   ```
   Closes #8
   ```
   or
   ```
   Fixes #8
   ```
   This automatically links the PR to the issue and closes it once the PR is merged.

5. If you're still working on your contribution, mark your PR as a **Draft**.
   It allows maintainers to follow your progress and provide early feedback.

6. Always check the box **“Allow edits from maintainers”** in your PR — it lets maintainers make small fixes (e.g., formatting or CI tweaks) directly in your branch.

---

## ✅ Pull Request Guidelines

- Keep PRs focused — one change per PR.
- Ensure your branch is up to date with `master`.
- All PRs must pass CI checks before merge.
- Include tests for new functionality or bug fixes.
- Use clear commit messages and PR titles.

---

## 🧪 Code Style

- **Java 17**
- **Spring Boot 3.3**
- Follow the existing code style and conventions from the `core` module.

---

## 🧰 Development Tips

- Run `mvn verify` to build and test all modules.
- Integration with the Telegram Bot API is handled via [Pengrad/java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api).
- You can run sample bots locally by adding `spring-boot-starter` to a test project.

---

## 💬 Communication

- Use **GitHub Issues** to report bugs or request features.
- Tag issues with appropriate labels (`bug`, `enhancement`, `help wanted`, etc.).
- For architecture discussions, open a **Discussion** thread.

---

## 🏷 Labels and Issue Types

| Label | Purpose                                         |
|--------|-------------------------------------------------|
| `good first issue` | Great for first-time contributors and beginners |
| `help wanted` | We need community input or assistance           |
| `bug` | Fixes for existing functionality                |
| `enhancement` | New features or improvements                    |
| `documentation` | Docs and examples                               |
| `discussion` | Architecture or design talks                    |

---

## 🔒 Review and Merge Policy

- All PRs require at least one approval.
- Merges use **Squash and Merge** for clean history.
- Only maintainers can push directly to protected branches.

---

## 🌱 Becoming a Contributor

After several high-quality PRs, you may be invited as a **Collaborator**:
- Initially with **Triage** access (can manage issues and PRs).
- Later with **Write** access if you become an active maintainer.

---

## ❤️ Thank You

Your contributions help make this starter better for everyone.
Whether you fix a typo, add a feature, or improve tests — it all matters!
