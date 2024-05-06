package ru.otus.appcontainer;

import lombok.SneakyThrows;
import lombok.val;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @SneakyThrows
    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        initContext(configClass);
    }

    private void initContext(Class<?> configClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        val instance = configClass.getConstructor().newInstance();
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(componentOrderComparator())
                .forEachOrdered(createBeanAndPutInContext(instance));
    }

    private Consumer<Method> createBeanAndPutInContext(Object instance) {
        return method -> {
            val bean = createBean(instance, method);
            val beanName = getBeanName(method);
            val previousBean = appComponentsByName.put(beanName, bean);
            if (previousBean != null) {
                throw new IllegalArgumentException("Several beans with name: %s".formatted(beanName));
            }
        };
    }

    private Comparator<Method> componentOrderComparator() {
        return (m1, m2) -> {
            int order1 = m1.getAnnotation(AppComponent.class).order();
            int order2 = m2.getAnnotation(AppComponent.class).order();
            return Integer.compare(order1, order2);
        };
    }

    @SneakyThrows
    private Object createBean(Object instance, Method m) {
        val beansToInject = Arrays.stream(m.getParameterTypes())
                .map(this::getAppComponent)
                .toList();
        if (beansToInject.isEmpty()) {
            return m.invoke(instance);
        }
        return m.invoke(instance, beansToInject.toArray());
    }

    private String getBeanName(Method method) {
        var beanName = method.getAnnotation(AppComponent.class).name();
        if (!beanName.isEmpty()) {
            return beanName;
        }
        return method.getReturnType().getSimpleName();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        val beans = appComponentsByName.values().stream()
                .filter(v -> v.getClass().equals(componentClass) || Arrays.asList(v.getClass().getInterfaces()).contains(componentClass))
                .toList();
        if (beans.isEmpty()) {
            throw new IllegalArgumentException("There is no beans in context with class: %s.".formatted(componentClass));
        } else if (beans.size() > 1) {
            throw new IllegalArgumentException("There are more than one beans in context with class: %s.".formatted(componentClass));
        }
        return (C) beans.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow();
    }

}
