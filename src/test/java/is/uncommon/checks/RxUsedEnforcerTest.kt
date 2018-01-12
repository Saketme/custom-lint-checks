package `is`.uncommon.checks

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.jetbrains.uast.UCallExpression
import org.junit.Test

class RxUsedEnforcerTest {

  @Test
  fun `when returned rx type not used, should flag only unused ones, in java`() {
    lint()
        .files(java("""

          package is.uncommon.checks;

          class TestClazz {

            io.reactivex.Observable rxReturningFunction() {}

            io.reactivex.Observable usage() {
              rxReturningFunction().subscribe();
              io.reactivex.Observable value = rxReturningFunction();
              io.reactivex.Observable value = rxReturningFunction().subscribe;

              rxReturningFunction();

              return rxReturningFunction();
            }
          }

        """))
        .issues(RxUsedEnforcerJ.ISSUE)
        .run()
        .expect("""
src/is/uncommon/checks/TestClazz.java:14: Error: Make use of the returned Rx type [RxUsedEnforcerJ]
              rxReturningFunction();
              ~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
        """)

    val node: UCallExpression? = null
    node?.psi
  }

  @Test
  fun `when returned rx type not used, should flag only unused ones, in kotlin`() {
    lint()
        .files(kotlin("""

          package `is`.uncommon.checks

          fun rxReturningFunction(): io.reactivex.Observable {}

          fun usage() {
            rxReturningFunction()
            rxReturningFunction().subscribe()
          }

        """))
        .issues(RxUsedEnforcerJ.ISSUE)
        .run()
        .expect("No warnings.")
  }
}
