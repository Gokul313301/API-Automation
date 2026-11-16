# API Automation Framework (REST Assured + TestNG)

A small, heavily-commented API test framework built for **learning**. It tests a
free public API ([JSONPlaceholder](https://jsonplaceholder.typicode.com)) so you
can run everything immediately with no signup, keys, or setup beyond Java + Maven.

---

## 1. Tech stack

| Purpose            | Tool                    | Why it's here                                   |
|--------------------|-------------------------|-------------------------------------------------|
| HTTP client + DSL  | **REST Assured**        | The `given().when().then()` fluent syntax       |
| Test runner        | **TestNG**              | Annotations, suites, data-driven tests          |
| JSON <-> Java       | **Jackson**             | Auto-converts POJOs to/from JSON                |
| Assertions         | **Hamcrest** + TestNG   | Readable body matchers + object asserts         |
| Build              | **Maven**               | Dependencies + running the suite                |
| Logging            | **SLF4J (simple)**      | Console output                                   |

---

## 2. Project structure

```
API Automation Framework/
├── pom.xml                       # Dependencies + build config
├── testng.xml                    # Which test classes to run
├── README.md
└── src/
    ├── main/java/com/api/
    │   ├── config/ConfigManager.java     # Reads config.properties
    │   ├── constants/Endpoints.java      # API paths in one place
    │   ├── specs/SpecFactory.java        # Reusable request/response specs
    │   └── models/                       # POJOs (Post, User)
    │       ├── Post.java
    │       └── User.java
    └── test/
        ├── java/com/api/tests/
        │   ├── BaseTest.java             # Shared setup + logging switch
        │   ├── PostsTest.java            # GET/POST/PUT/DELETE demos
        │   ├── UsersTest.java            # List parsing + data-driven test
        │   └── NegativeTest.java         # 404s + response-shape checks
        └── resources/
            └── config.properties         # base.url, timeouts, log.all
```

**The layering idea:** tests never hard-code URLs or build request setup inline.
They pull the base URL from `config.properties` (via `ConfigManager`), the paths
from `Endpoints`, and the common request setup from `SpecFactory`. Change any of
those in one place and every test follows.

---

## 3. Prerequisites

You need a **JDK 17+** and **Maven**. Two options:

### Option A — Run inside IntelliJ (easiest, nothing to install)
You already have IntelliJ IDEA Community. It bundles both a JDK and Maven.

1. **File > Open** and select this folder. IntelliJ detects the `pom.xml` and
   imports it as a Maven project (wait for indexing to finish).
2. If prompted for a Project SDK, pick the bundled JetBrains Runtime (or any 17+).
3. Right-click `testng.xml` > **Run**. (Or open any test class and click the green
   arrow next to a method.)

### Option B — Run from the command line
Install these once, then add them to your PATH:
- JDK 17+ — e.g. [Eclipse Temurin](https://adoptium.net/)
- [Apache Maven](https://maven.apache.org/download.cgi)

Verify:
```powershell
java -version
mvn -version
```

---

## 4. Running the tests

From the command line (Option B):
```powershell
# Run the whole suite defined in testng.xml
mvn test

# Run a single test class
mvn test -Dtest=PostsTest

# Run a single method
mvn test -Dtest=PostsTest#getSinglePost
```

Results:
- Console shows pass/fail per test.
- HTML report is generated at `test-output/index.html` (open it in a browser).

### Seeing the request & response (logging)

`config.properties` has a **`log.all`** switch:
- `log.all=true` (default) — every call prints the **full request and response**,
  including the pretty-printed JSON body. Great while learning: you literally see
  the data going back and forth.
- `log.all=false` — quiet console; the request/response is dumped **only when an
  assertion fails**, which is what you'll want once you're comfortable.

Example of what `true` prints:
```
Request method:	GET
Request URI:	https://jsonplaceholder.typicode.com/posts/1
HTTP/1.1 200 OK
{
    "userId": 1,
    "id": 1,
    "title": "sunt aut facere repellat ...",
    "body": "quia et suscipit ..."
}
```

### Negative tests

`NegativeTest.java` checks the **unhappy path** — that the API fails *correctly*:
- `GET /posts/99999` and `GET /users/99999` return **404**.
- An unknown path returns **404**.
- A post has **exactly** its expected fields (catches the API silently adding or
  dropping keys).

Testing failures is as important as testing success — that's where real bugs hide.

---

## 5. How a test reads (the mental model)

Every REST Assured test follows **given / when / then**:

```java
given()                          // setup: base URL, headers, body, params
    .spec(requestSpec)
    .pathParam("id", 1)
.when()                          // action: the HTTP call
    .get(Endpoints.POST_BY_ID)
.then()                          // assertions: on the response
    .statusCode(200)
    .body("id", equalTo(1));
```

Two assertion styles are shown in the code, and you'll use both:
- **Inline matchers:** `.body("id", equalTo(1))` — quick checks on the JSON.
- **Extract then assert:** `.extract().as(Post.class)` — pull the response into a
  POJO, then use normal `assertEquals(...)`. Better for complex logic.

---

## 6. Suggested learning path

Work through the code in this order, running each test as you go:

1. **`config.properties` + `ConfigManager`** — how config stays out of code.
2. **`PostsTest.getSinglePost`** — the simplest GET + inline assertions.
3. **`PostsTest.getAllPosts`** — asserting on a JSON array (`size()`, indexing).
4. **`Post.java` + `PostsTest.createPost`** — POJO serialization both directions.
5. **`PostsTest.updatePost` / `deletePost`** — the other HTTP verbs.
6. **`PostsTest.inspectResponse`** — working with the raw `Response` object.
7. **`UsersTest.getAllUsersAsList`** — deserializing a JSON array into `List<User>`.
8. **`UsersTest.getUserById` + `@DataProvider`** — data-driven testing.
9. **`NegativeTest`** — testing the unhappy path (404s, response shape).
10. **`SpecFactory`** — how specs remove repetition once you've felt the repetition.

> Tip: keep `log.all=true` for steps 1–9 so you can watch each request/response,
> then flip it to `false` to see how a real, quiet test run looks.

Then try these exercises to make it your own:
- Add a `Comment` POJO and tests for `/comments`.
- Add a negative test: `GET /posts/9999` should return **404**.
- Add JSON **schema validation** (`rest-assured` + `json-schema-validator`).
- Add a second environment to `config.properties` and switch via a system property.
- Add reporting with **Allure** or **Extent Reports**.

---

## 7. Notes on JSONPlaceholder

It's a *fake* API: `POST`/`PUT`/`DELETE` return realistic responses (correct
status codes and echoed bodies) but **don't actually persist** data. That's fine
for learning the mechanics. When you're comfortable, point `base.url` at a real
API (e.g. [reqres.in](https://reqres.in), or your own service) and adjust the
assertions.

---

Happy testing!
