package `is`.uncommon.checks

import com.android.tools.lint.client.api.IssueRegistry

class LintIssueRegistry : IssueRegistry() {

  override val issues = listOf(
      NamedForPrimitiveTypesOfProvidersEnforcer.ISSUE,
      ImplementsSerializableDetector.ISSUE,
      RxUsedEnforcerJ.ISSUE
  )
}
