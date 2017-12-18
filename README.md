# Lint Checks
Flags methods returning Rx primitives (Observable, Single, etc.) that aren't annotated with @CheckResult annotation.

    @CheckResult
    public Observable<T> streamTextChanges() {
      ...
    }


## Gotchas

- Target project's Gradle Version should be greater than 3
- LintIssueRegistry - Full packaage name has to be specified
- Having the gradle dependencies won't affect the code snippet in tests
