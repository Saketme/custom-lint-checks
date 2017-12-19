# Lint Checks

1. Flags methods returning Rx primitives (Observable, Single, etc.) that aren't annotated with @CheckResult annotation.
```java
    @CheckResult
    public Observable<T> streamTextChanges() {
      ...
    }
```
2. Providers that return primitive types should be named
```java
    @Provides
    @Named(FLAG_FOR_CHECKS)
    static int flagForChecks() {
      return 0xFE;
    }
```

## How to run
Run `buildAndInstall.sh` in this repo.
Run `./gradlew lint` on target project to get a lint report.

## Gotchas

- Target project's Gradle Version should be greater than 3
- LintIssueRegistry - Full package name has to be specified
- Having the gradle dependencies won't affect the code snippet in tests
