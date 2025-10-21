# 🧩 Spring Boot Dynamic Filterable Pagination Library

A **lightweight**, **generic**, and **type-safe** utility for adding **dynamic filtering, sorting, and pagination** to your Spring Data JPA repositories — without writing boilerplate code or dozens of query methods.

---

## 🚀 Features

✅ Dynamic **filtering** for any entity field  
✅ Supports **multiple match modes** (`EQUALS`, `CONTAINS`, `GREATER_THAN`, `IN`, etc.)  
✅ Supports **pagination and sorting** via `FilterablePage`  
✅ Integrates seamlessly with **Spring Data JPA**  
✅ Fully **type-safe**, no raw SQL or unsafe casting  
✅ Easily **extendable** for custom filters

---

## 🧱 Architecture Overview
```text5
com.maninasrolahi.spring.pagination.dynamic.filter
└── base
    ├── model
    │   ├── FilterablePage.java              ← Encapsulates filters, sorting, and pagination data
    │   ├── MatchModes.java                  ← Enum defining all supported comparison operations
    │   └── SortOrders.java                  ← Enum for sorting direction (ASC / DESC)
    │
    ├── repository
    │   └── FilterablePageRepository.java    ← Base repository interface with dynamic filter method
    │
    └── repository.support
        ├── CustomSimpleJpaRepository.java   ← custom implementation of SimpleJpaRepository.java
        ├── MatchModeSpecificationResolver.java ← Resolves the correct Specification for each match mode
        └── FilterSpecificationParser.java   ← Converts filters into JPA Specifications
```

## 🧩 How It Works

This library introduces a **unified filtering model** called `FilterablePage`, which encapsulates:

- **Page request info** : `page`, `rows`

- **Sorting info** : `sortField`, `sortOrder`

- **List of filters** : each containing `fieldName`, `fieldValue`, `matchMode`

Then, `MatchModeSpecificationResolver` converts these into a JPA Specification dynamically.

---

## 🧰 Example Usage

#### 1️⃣ Define Your Repository

```java
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>, FilterablePageRepository<Users> {
}
```
  ⚠️ Make sure your repository is picked up by Spring and uses CustomSimpleJpaRepository as the base implementation.

###### You can do that in your configuration:

```java
@EnableJpaRepositories(
  repositoryBaseClass =  CustomSimpleJpaRepository.class
)
```

#### 2️⃣ Create a Filterable Page

```java
FilterablePage filterablePage = FilterablePage.pageOf(0, 10)
    .witSort("firstName", SortOrders.ASC)
    .addFilter("firstName", "John", MatchModes.STARTS_WITH)
    .addFilter("age", 25, MatchModes.GREATER_THAN);
```

#### 3️⃣ Call the Method

```java
Page<User> users = userRepository.findAllPageWithFilters(filterablePage);
```
This will execute a query equivalent to:

```sql
SELECT * FROM users 
WHERE firstName LIKE 'John%' 
AND age > 25 
ORDER BY firstName ASC 
LIMIT 10 OFFSET 0;
```
## 🔍 Supported Match Modes

| Match Mode                 | Description          | Example                      |
| -------------------------- | -------------------- | ---------------------------- |
| `EQUALS`                   | Exact equality       | `name = 'John'`              |
| `NOT_EQUALS`               | Inequality           | `status != 'INACTIVE'`       |
| `CONTAINS`                 | Substring match      | `name LIKE '%ohn%'`          |
| `NOT_CONTAINS`             | Negated substring    | `name NOT LIKE '%test%'`     |
| `STARTS_WITH`              | Prefix match         | `name LIKE 'Jo%'`            |
| `ENDS_WITH`                | Suffix match         | `name LIKE '%hn'`            |
| `LESS_THAN`                | Numeric compare      | `price < 10`                 |
| `LESS_THAN_OR_EQUAL_TO`    | ≤ comparison         | `price <= 10`                |
| `GREATER_THAN`             | > comparison         | `price > 10`                 |
| `GREATER_THAN_OR_EQUAL_TO` | ≥ comparison         | `price >= 10`                |
| `IN`                       | List membership      | `status IN ('A','B')`        |
| `NOT_IN`                   | Not in list          | `category NOT IN ('X','Y')`  |
| `DATE_IS`                  | Exact date           | `created_at = '2025-10-21'`  |
| `DATE_IS_NOT`              | Not equal            | `updated_at != '2025-10-21'` |
| `DATE_BEFORE`              | Date before or equal | `created_at <= '2025-10-21'` |
| `DATE_AFTER`               | Date after or equal  | `updated_at >= '2025-10-21'` |

## 🧩 Example JSON Request (API)

If you’re calling your API from an another service:

```
{
  "page": 0,
  "rows": 10,
  "sortField": "id",
  "sortOrder": "asc",
  "filters": [
    { "filedName": "firstName", "filedValue": "Jo", "matchMode": "STARTS_WITH" },
    { "filedName": "age", "filedValue": 25, "matchMode": "GREATER_THAN" },
    ...
  ]
}
```
controller method:
```java
@PostMapping("/search")
    public ResponseEntity<Page<Users>> searchUsers(@RequestBody FilterablePage request) {
        Page<Users> result = userRepository.findAllPageWithFilters(request);
        return ResponseEntity.ok(result);
    }
```

## 🧩 Extendability
You can extend this framework easily and add your custom logics:

```java
public class CustomFilterParser<T> extends FilterSpecificationParser<T> {

    // custom methods...
}
```

## ⭐ Contribute

Contributions are welcome! Feel free to open issues and pull requests.
