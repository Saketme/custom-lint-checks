package com.olacabs.checks.lint;

import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: Doc.
 */
public class RxCheckResultAnnotationEnforcer extends Detector implements Detector.JavaPsiScanner {

    private static final String ISSUE_ID = RxCheckResultAnnotationEnforcer.class.getSimpleName();
    private static final String ISSUE_TITLE = "Use @CheckResult";
    private static final String ISSUE_DESCRIPTION =
            "It's easy to forget calling subscribe() on methods that return Rx primitives like Observable, Single, etc. Annotate this method with "
                    + "@CheckReturn so that AndroidStudio shows a warning when the return value is not used.";
    private static final int ISSUE_PRIORITY = 10;

    private static final Set<String> RX_PRIMITIVES = new HashSet<>();

    static {
        RX_PRIMITIVES.add("io.reactivex.ObservableSource");
        RX_PRIMITIVES.add("io.reactivex.Observable");
        RX_PRIMITIVES.add("io.reactivex.SingleSource");
        RX_PRIMITIVES.add("io.reactivex.Single");
        RX_PRIMITIVES.add("io.reactivex.CompletableSource");
        RX_PRIMITIVES.add("io.reactivex.Completable");
        RX_PRIMITIVES.add("io.reactivex.MaybeSource");
        RX_PRIMITIVES.add("io.reactivex.Maybe");
        RX_PRIMITIVES.add("org.reactivestreams.Publisher");
        RX_PRIMITIVES.add("io.reactivex.Flowable");
    }

    public static final Issue ISSUE = Issue.create(
            ISSUE_ID,
            ISSUE_TITLE,
            ISSUE_DESCRIPTION,
            Category.CORRECTNESS,
            ISSUE_PRIORITY,
            Severity.ERROR,
            new Implementation(RxCheckResultAnnotationEnforcer.class, Scope.JAVA_FILE_SCOPE)
    );

    public RxCheckResultAnnotationEnforcer() {
    }

    @Override
    public EnumSet<Scope> getApplicableFiles() {
        return Scope.JAVA_FILE_SCOPE;
    }

    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.singletonList(PsiMethod.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                boolean isRxPrimitiveReturnValue = isReturnValueRxPrimitive(method.getReturnType());
                if (!isRxPrimitiveReturnValue) {
                    return;
                }

                boolean hasCheckReturnAnnotation = false;
                for (PsiAnnotation methodAnnotation : context.getEvaluator().getAllAnnotations(method, true)) {
                    //noinspection ConstantConditions
                    if (methodAnnotation.getQualifiedName().equals("android.support.annotation.CheckResult")) {
                        hasCheckReturnAnnotation = true;
                        break;
                    }
                }

                if (!hasCheckReturnAnnotation) {
                    context.report(ISSUE, method, context.getLocation(method), "Should annotate return value with @CheckResult");
                }
            }

            private boolean isReturnValueRxPrimitive(@Nullable PsiType returnValueType) {
                if (returnValueType != null) {
                    if (RX_PRIMITIVES.contains(returnValueType.getPresentableText())) {
                        return true;
                    }

                    // TODO: Check super values by migrating to Android Gradle plugin v3. Useful for InitialValueObservable, etc.
                    //PsiType[] returnTypeSuperTypes = returnValueType.getSuperTypes();
                    ////noinspection ForLoopReplaceableByForEach Don't want to create and throw Iterators when running lint on every method.
                    //for (int i = 0; i < returnTypeSuperTypes.length; i++) {
                    //    if (RX_PRIMITIVES.contains(returnTypeSuperTypes[i].getPresentableText())) {
                    //        return true;
                    //    }
                    //}

                    // TODO: Integrate this lint check with Android Studio. Should show warnings.
                }

                return false;
            }
        };
    }
}
