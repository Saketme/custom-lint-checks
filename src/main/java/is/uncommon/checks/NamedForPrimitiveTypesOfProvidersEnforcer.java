package is.uncommon.checks;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiType;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UMethod;

/**
 * Throws lint errors if methods returning Rx primitives (Observable, Single, etc.) are found without @CheckResult annotation.
 */
public class NamedForPrimitiveTypesOfProvidersEnforcer extends Detector implements Detector.UastScanner {

  static final Severity SEVERITY = Severity.ERROR;
  private static final String ISSUE_ID = NamedForPrimitiveTypesOfProvidersEnforcer.class.getSimpleName();
  private static final String ISSUE_TITLE = "Use @CheckResult";
  private static final String ISSUE_DESCRIPTION =
      "It's easy to forget calling subscribe() on methods that return Rx primitives like Observable, Single, etc. Annotate this method with "
          + "@CheckResult so that AndroidStudio shows a warning when the return value is not used.";
  private static final int ISSUE_PRIORITY = 10;   // Highest.
  static final Issue ISSUE = Issue.create(
      ISSUE_ID,
      ISSUE_TITLE,
      ISSUE_DESCRIPTION,
      Category.CORRECTNESS,
      ISSUE_PRIORITY,
      SEVERITY,
      new Implementation(NamedForPrimitiveTypesOfProvidersEnforcer.class, Scope.JAVA_FILE_SCOPE)
  );
  private final List<String> primitiveTypes = Arrays.asList("int", "boolean", "double");

  @Override
  public EnumSet<Scope> getApplicableFiles() {
    return Scope.JAVA_FILE_SCOPE;
  }

  @Override
  public List<Class<? extends UElement>> getApplicableUastTypes() {
    return Collections.singletonList(UClass.class);
  }

  @Override
  public UElementHandler createUastHandler(JavaContext context) {
    return new UElementHandler() {
      @Override
      public void visitClass(UClass uClass) {
        if (uClass.findAnnotation("Module") == null) {
          return;
        }
        for (UMethod method : uClass.getMethods()) {
          if (method.findAnnotation("Provides") != null) {
            final PsiType returnType = method.getReturnType();

            if (returnType != null && primitiveTypes.contains(returnType.getCanonicalText())) {
              context.report(ISSUE, method, context.getLocation(method),
                  "Should apply @Named for primitives");
            }
          }
        }
      }
    };
  }

}
