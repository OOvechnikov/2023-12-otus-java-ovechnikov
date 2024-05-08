package ru.otus.hw05;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Context {

    @Getter(AccessLevel.PRIVATE)
    private static final Map<Class<?>, List<String>> annotatedMethodsMap;

    static {
        val reflections = new Reflections(new ConfigurationBuilder().forPackages("ru.otus.hw05").setScanners(Scanners.MethodsAnnotated));
        annotatedMethodsMap = reflections.getMethodsAnnotatedWith(Log.class).stream()
                .collect(Collectors.groupingBy(
                        Method::getDeclaringClass,
                        Collectors.mapping(
                                Context::methodToString,
                                Collectors.toList()
                        )));
    }

    public static Object createProxy(Object instance) {
        val clazz = instance.getClass();
        if (annotatedMethodsMap.containsKey(clazz)) {
            return Proxy.newProxyInstance(
                    instance.getClass().getClassLoader(),
                    instance.getClass().getInterfaces(),
                    new LogInvocationHandler<>(instance)
            );
        }
        throw new IllegalArgumentException("Создание Proxy для класса %s не поддерживается".formatted(clazz.getName()));
    }

    private static String methodToString(Method m) {
        return m.getName() + "(" + Arrays.stream(m.getParameters()).map(p -> p.getParameterizedType().getTypeName()).collect(Collectors.joining(",")) + ")";
    }

    @Slf4j
    @ToString
    private static class LogInvocationHandler<T> implements InvocationHandler {

        private final T instance;

        LogInvocationHandler(T instance) {
            this.instance = instance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            val clazz = (Class<T>) instance.getClass();
            val annotatedMethods = getAnnotatedMethodsMap().get(clazz);
            if (annotatedMethods.contains(methodToString(method))) {
                log.info("Executed method: {}. Params: {}", method.getName(), Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", ")));
            }
            return method.invoke(instance, args);
        }

    }

}
