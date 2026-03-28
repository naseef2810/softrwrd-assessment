# SauceDemo Test Automation Framework

![CI Build Status](https://github.com/USER/softrwrd-assessment/actions/workflows/ci.yml/badge.svg)

## 1. Framework Choice & Rationale

- **Language:** Java 11+
  - *Rationale:* Java offers strong typing, a mature ecosystem, and is widely the industry standard for enterprise test automation.
- **UI Automation:** Selenium WebDriver 4.x
  - *Rationale:* Robust, reliable, and deeply integrated into Java. Version 4 provides enhanced mechanisms like native tools and better modern browser support.
- **Test Runner:** TestNG
  - *Rationale:* Preferred over JUnit for e2e testing because of built-in features like `DataProviders`, parallel execution, dependency management (`dependsOnMethods`), and grouping.
- **Reporting:** Allure
  - *Rationale:* Generates aesthetic, detailed, and interactive HTML reports containing automated screenshots on failures out of the box.
- **Build Tool:** Maven
  - *Rationale:* Simplified dependency management and lifecycle execution. Easy CI/CD integration (`mvn clean test`).

*Alternatives considered but rejected:*
- *Playwright/Cypress:* While historically excellent for modern SPAs, Selenium remains extremely versatile across browsers. Given the assessment's focus on architectural design and standard OOP concepts like POM, a Java/Selenium/TestNG stack provides a classical and highly visible demonstration of Software Engineering principles (SOLID, DRY).

## 2. Architecture Overview

The framework adopts a traditional Page Object Model (POM) combined with central Configuration Management and smart waits.

```text
src/
├── main/java/com/softwrdassessment/
│   ├── config/       # ConfigManager (Reads config.properties)
│   ├── pages/        # Page Object classes (LoginPage, InventoryPage, etc.)
│   └── utils/        # Shared Utilities (WaitUtils, JsonReader, WebDriverFactory)
└── test/java/com/softwrdassessment/
    ├── base/         # BaseTest (Driver setup & teardown via @BeforeMethod/@AfterMethod)
    ├── listeners/    # TestListener (Allure screenshot capturing on failure)
    └── tests/        # Test classes separated by feature
```

- **Separation of Concerns:** Test logic lives in `tests/`, UI logic in `pages/`. Wait complexities are hidden securely inside `utils/WaitUtils.java`.
- **Test Data Management:** Complex test data (like various user accounts) is extracted to `src/test/resources/testdata/users.json` and parsed via Gson, keeping tests clean and data-driven (`@DataProvider`).
- **Configuration Management:** Environment values (`baseUrl`, `browser`, `headless`) are loaded dynamically from `config.properties`, overriding via system properties natively supported.

## 3. Setup & Run Instructions

### Prerequisites
- Java JDK 21
- Maven 3.8+
- Google Chrome installed

### Local Execution
1. Clone the repository natively:
   ```bash
   git clone <repository_url>
   cd softrwrd-assessment
   ```
2. Run tests directly using Maven:
   ```bash
   mvn clean test
   ```

*(Optional)* Run tests with a specific browser/header config by passing properties:
```bash
mvn clean test -Dbrowser=firefox -Dheadless=false
```

## 4. CI/CD Pipeline

This repository uses GitHub Actions (`.github/workflows/ci.yml`). The pipeline is triggered automatically on `push` and `pull_request` to the `main` branch.

- **Pipeline View:** Go to the "Actions" tab in the GitHub repository to view executions.
- **Reports:** After tests finish out the pipeline run, an **allure-report** artifact will be generated. You can download this `.zip`, extract it, and open `index.html` to view the full test execution log with screenshots intelligently hooked to failures.

## 5. Test Coverage Summary

- **Authentication:** Covers successful login, exact validations for invalid credentials, locked-out state, session persistence, and logout flow.
- **Product Catalog:** Asserts product loading, validates mathematical sorting (A-Z, Z-A, Price Low-High, Price High-Low), and actively tests visual resilience for the `problem_user`.
- **Shopping Cart:** Evaluates adding single/multiple items, visual cart badge counting, removing items, and checks context persistence across site navigation.
- **Checkout Flow:** Executes a complete order loop verifying confirmation message alongside validation of mathematical sum correctness (Subtotal + Tax = Total). Edge cases like form validation blocks are comprehensively addressed.
- **Performance & Resilience:** Explicit tests assess the system under `performance_glitch_user` (timing delays mitigated by automated smart waits) and `error_user` (correctly identifying and asserting a known blocked state during checkout validation).
