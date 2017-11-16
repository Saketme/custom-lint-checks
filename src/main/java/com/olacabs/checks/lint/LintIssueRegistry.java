package com.olacabs.checks.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import java.util.Collections;
import java.util.List;

/**
 * Usage in OlaChecks-v2/build.gradle. Defines all checks available in this project.
 */
@SuppressWarnings("unused")
public class LintIssueRegistry extends IssueRegistry {

  public LintIssueRegistry() {
  }

  @Override
  public List<Issue> getIssues() {
    return Collections.singletonList(RxCheckResultAnnotationEnforcer.ISSUE);
  }
}
