package ru.otus.hw03_framework.framework;

import ru.otus.hw03_framework.annotation.After;
import ru.otus.hw03_framework.annotation.Before;
import ru.otus.hw03_framework.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TestFramework {

    public static final String BEFORE_AFTER_QTY_EXCEPTION_MESSAGE = "В тестируемом классе аннотация %s может быть только над 1 методом.";
    public static final String BEFORE_AFTER_PARAMETERS_EXCEPTION_MESSAGE = "Метод с аннотацией @Before или @After не должен иметь аргументов.";

    public void runTests(String testClassName) throws Exception {
        Class<?> clazz = Class.forName(testClassName);
        Method[] declaredMethods = clazz.getDeclaredMethods();

        validateBeforeAfterAnnotations(declaredMethods);

        invokeTestsAndPringResult(clazz);

    }

    private void validateBeforeAfterAnnotations(Method[] methods) {
        int beforeAnnotationCount = 0;
        int afterAnnotationCount = 0;
        for (Method method : methods) {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(Before.class)) {
                    if (beforeAnnotationCount == 0 && method.getParameterCount() > 0) {
                        throw new UnsupportedOperationException(BEFORE_AFTER_PARAMETERS_EXCEPTION_MESSAGE);
                    }
                    beforeAnnotationCount++;
                    if (beforeAnnotationCount > 1) {
                        throw new UnsupportedOperationException(BEFORE_AFTER_QTY_EXCEPTION_MESSAGE.formatted(Before.class.getCanonicalName()));
                    }
                } else if (annotationType.equals(After.class)) {
                    if (afterAnnotationCount == 0 && method.getParameterCount() > 0) {
                        throw new UnsupportedOperationException(BEFORE_AFTER_PARAMETERS_EXCEPTION_MESSAGE);
                    }
                    afterAnnotationCount++;
                    if (afterAnnotationCount > 1) {
                        throw new UnsupportedOperationException(BEFORE_AFTER_QTY_EXCEPTION_MESSAGE.formatted(After.class.getCanonicalName()));
                    }
                }
            }
        }
    }

    private void invokeTestsAndPringResult(Class<?> clazz) throws Exception {
        int successfullyTests = 0;
        int failedTests = 0;

        Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
        defaultConstructor.setAccessible(true);
        List<Method> testMethods = getMethodsWithAnnotation(clazz, Test.class);
        for (Method method : testMethods) {
            Object instance = defaultConstructor.newInstance();
            Arrays.stream(instance.getClass().getDeclaredMethods()).forEach(m -> m.setAccessible(true));
            Method beforeMethod = getMethodsWithAnnotation(clazz, Before.class).get(0);
            beforeMethod.invoke(instance);
            try {
                method.invoke(instance);
                successfullyTests++;
            } catch (Exception e) {
                failedTests++;
            } finally {
                Method afterMethod = getMethodsWithAnnotation(clazz, After.class).get(0);
                afterMethod.invoke(instance);
            }
        }

        System.out.printf("Всего тестов: %s%n", successfullyTests + failedTests);
        System.out.printf("Количество успешных тестов: %s%n", successfullyTests);
        System.out.printf("Количество проваленных тестов: %s%n", failedTests);
    }

    private List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> Arrays.stream(m.getDeclaredAnnotations())
                        .anyMatch(a -> a.annotationType().equals(annotation))
                )
                .peek(m -> m.setAccessible(true))
                .toList();
    }

}
