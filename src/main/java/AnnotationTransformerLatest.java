import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import retryLogic.RetryAnalyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformerLatest implements IAnnotationTransformer {

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(Retry1.class);
    }
}