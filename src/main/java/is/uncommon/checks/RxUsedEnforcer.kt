package `is`.uncommon.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*

class RxUsedEnforcer : Detector(), Detector.UastScanner {

  companion object {

    private val RX_PRIMITIVE_CANONICAL_NAMES = listOf(
        "io.reactivex.Observable",
        "io.reactivex.Single",
        "io.reactivex.Completable",
        "io.reactivex.Maybe",
        "io.reactivex.Flowable"
    )

    internal val ISSUE = Issue.create(
        id = RxUsedEnforcer::class.simpleName!!,
        briefDescription = "Make sure function returning Rx type is being used (typically `subscribe()`)",
        explanation = "",
        category = Category.CORRECTNESS,
        priority = 10,
        severity = Severity.ERROR,
        implementation = Implementation(RxUsedEnforcer::class.java, Scope.JAVA_FILE_SCOPE)
    )
  }

  override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler? {
    println(context.uastFile!!.asRecursiveLogString())

    return object : UElementHandler() {

      override fun visitCallExpression(node: UCallExpression) {
        if (node.returnType?.canonicalText in RX_PRIMITIVE_CANONICAL_NAMES &&
            node.uastParent !is UQualifiedReferenceExpression &&
            node.uastParent !is UDeclarationsExpression &&
            node.uastParent !is ULocalVariable &&
            node.uastParent !is UReturnExpression) {

          context.report(
              ISSUE,
              context.getCallLocation(node, includeReceiver = false, includeArguments = false),
              "Make use of the returned Rx type"
          )
        }
      }
    }
  }
}
