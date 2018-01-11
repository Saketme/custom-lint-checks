package `is`.uncommon.checks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class LintIssueRegistry : IssueRegistry() {

  override val issues: List<Issue> = listOf(
      NamedForPrimitiveTypesOfProvidersEnforcer.ISSUE,
      ImplementsSerializableDetector.ISSUE,
      RxUsedEnforcer.ISSUE
  )
}
