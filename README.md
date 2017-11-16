# Lint Checks
Flags methods returning Rx primitives (Observable, Single, etc.) that aren't annotated with @CheckResult annotation.

    @CheckResult
    public Observable<T> streamTextChanges() {
      ...
    }
