package ru.otus.hw05;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Context {

    @Getter(AccessLevel.PRIVATE)
    private static final Map<Class<? extends TestLoggingInterface>, List<String>> annotatedMethodsMap = new HashMap<>();

    static {
        val reflections = new Reflections("ru.otus.hw05");
        reflections.getSubTypesOf(TestLoggingInterface.class).forEach(tli -> {
            val methods = Arrays.stream(tli.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .map(Context::methodToString)
                    .toList();
            annotatedMethodsMap.put(tli, methods);
        });
    }

    public static TestLoggingInterface createProxy(TestLoggingInterface instance) {
        val handler = new LogInvocationHandler<>(instance);
        return ((TestLoggingInterface) Proxy.newProxyInstance(LogInvocationHandler.class.getClassLoader(), new Class[]{TestLoggingInterface.class}, handler));
    }

    private static String methodToString(Method m) {
        return m.getName() + "(" + Arrays.stream(m.getParameters()).map(p -> p.getParameterizedType().getTypeName()).collect(Collectors.joining(",")) + ")";
    }

    @Slf4j
    @ToString
    private static class LogInvocationHandler<T extends TestLoggingInterface> implements InvocationHandler {

        private final T testLoggingInterface;
        private final Class<T> clazz;

        LogInvocationHandler(T testLoggingInterface) {
            this.testLoggingInterface = testLoggingInterface;
            clazz = (Class<T>) testLoggingInterface.getClass();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            val annotatedMethods = getAnnotatedMethodsMap().get(clazz);
            if (annotatedMethods.contains(methodToString(method))) {
                log.info("Executed method: {}. Params: {}", method.getName(), Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", ")));
            }
            return method.invoke(testLoggingInterface, args);
        }

    }

}
