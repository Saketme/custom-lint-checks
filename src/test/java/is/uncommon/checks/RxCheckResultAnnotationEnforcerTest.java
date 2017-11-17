package is.uncommon.checks;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;
import static org.junit.Assert.assertEquals;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

public class RxCheckResultAnnotationEnforcerTest {

  @Language("JAVA")
  private static final String CODE_WITHOUT_CHECKRESULT_ANNOTATION = "package foo;\n"
      + "public class PoopClass {\n"
      + "  private CustomCustomObservable doSomething() {\n"
      + "    return null;\n"
      + "  }\n"
      + "\n"
      + "  public class CustomCustomObservable<T> extends CustomObservable<T> {\n"
      + "  }\n"
      + "\n"
      + "  public class CustomObservable<T> extends io.reactivex.Observable<T> {\n"
      + "  \t@java.lang.Override\n"
      + "    protected void subscribeActual(io.reactivexObserver observer) {\n"
      + "    }\n"
      + "  }\n"
      + "}\n";

  @Language("JAVA")
  private static final String CODE_WITH_CHECKRESULT_ANNOTATION = "package foo;\n"
      + "public class PoopClass {\n"
      + "  @android.support.annotation.CheckResult\n"
      + "  private io.reactivex.Observable doSomething() {\n"
      + "    return null;\n"
      + "  }\n"
      + "\n"
      + "  public class CustomCustomObservable<T> extends CustomObservable<T> {\n"
      + "  }\n"
      + "\n"
      + "  public class CustomObservable<T> extends io.reactivex.Observable<T> {\n"
      + "  \t@java.lang.Override\n"
      + "    protected void subscribeActual(io.reactivexObserver observer) {\n"
      + "    }\n"
      + "  }\n"
      + "}\n";

  @Test
  public void whenCheckResultIsNotPresent_shouldFail() {
    lint()
        .allowMissingSdk()
        .files(java(CODE_WITHOUT_CHECKRESULT_ANNOTATION))
        .allowCompilationErrors()
        .issues(RxCheckResultAnnotationEnforcer.ISSUE)
        .run()
        .expectCount(1, RxCheckResultAnnotationEnforcer.SEVERITY);
  }

  @Test
  public void whenCheckResultIsPresent_shouldPass() {
    lint()
        .allowMissingSdk()
        .files(java(CODE_WITH_CHECKRESULT_ANNOTATION))
        .allowCompilationErrors()
        .issues(RxCheckResultAnnotationEnforcer.ISSUE)
        .run()
        .expectClean();
  }

  @Test
  public void removeTypeFromClassName() {
    String nameSansType1 = RxCheckResultAnnotationEnforcer.removeTypeFromClassName("io.reactivex.Observable<java.lang.Poop>");
    assertEquals(nameSansType1, "io.reactivex.Observable");

    String nameSansType2 = RxCheckResultAnnotationEnforcer.removeTypeFromClassName("io.reactivex.Observable");
    assertEquals(nameSansType2, "io.reactivex.Observable");
  }
}
