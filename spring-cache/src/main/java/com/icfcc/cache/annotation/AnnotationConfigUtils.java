package com.icfcc.cache.annotation;


//import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
//import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility class that allows for convenient registration of common
 * {@link org.springframework.beans.factory.config.BeanPostProcessor} and
 * {@link org.springframework.beans.factory.config.BeanFactoryPostProcessor}
 * definitions for annotation-based configuration.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 2.5
 */
public class AnnotationConfigUtils {

    /**
     * The bean name of the internally managed Configuration annotation processor.
     */
    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";

    /**
     * The bean name of the internally managed Autowired annotation processor.
     */
    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";

    /**
     * The bean name of the internally managed Required annotation processor.
     */
    public static final String REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalRequiredAnnotationProcessor";

    /**
     * The bean name of the internally managed JSR-250 annotation processor.
     */
    public static final String COMMON_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalCommonAnnotationProcessor";

    /**
     * The bean name of the internally managed Scheduled annotation processor.
     */
    public static final String SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalScheduledAnnotationProcessor";

    /**
     * The bean name of the internally managed Async annotation processor.
     */
    public static final String ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalAsyncAnnotationProcessor";

    /**
     * The bean name of the internally managed AspectJ async execution aspect.
     */
    public static final String ASYNC_EXECUTION_ASPECT_BEAN_NAME =
            "org.springframework.scheduling.config.internalAsyncExecutionAspect";

    /**
     * The class name of the AspectJ async execution aspect.
     */
    public static final String ASYNC_EXECUTION_ASPECT_CLASS_NAME =
            "org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect";

    /**
     * The name of the AspectJ async execution aspect @{@code Configuration} class.
     */
    public static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME =
            "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";

    /**
     * The bean name of the internally managed cache advisor.
     */
    public static final String CACHE_ADVISOR_BEAN_NAME =
            "com.icfcc.cache.config.internalCacheAdvisor";

    /**
     * The bean name of the internally managed cache aspect.
     */
    public static final String CACHE_ASPECT_BEAN_NAME =
            "org.springframework.cache.config.internalCacheAspect";

    /**
     * The class name of the AspectJ caching aspect.
     */
    public static final String CACHE_ASPECT_CLASS_NAME =
            "org.springframework.cache.aspectj.AnnotationCacheAspect";

    /**
     * The name of the AspectJ caching aspect @{@code Configuration} class.
     */
    public static final String CACHE_ASPECT_CONFIGURATION_CLASS_NAME =
            "org.springframework.cache.aspectj.AspectJCachingConfiguration";

    /**
     * The bean name of the internally managed JPA annotation processor.
     */
    public static final String PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalPersistenceAnnotationProcessor";


    private static final String PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME =
            "org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor";


    private static final boolean jsr250Present =
            ClassUtils.isPresent("javax.annotation.Resource", AnnotationConfigUtils.class.getClassLoader());

    private static final boolean jpaPresent =
            ClassUtils.isPresent("javax.persistence.EntityManagerFactory", AnnotationConfigUtils.class.getClassLoader()) &&
                    ClassUtils.isPresent(PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME, AnnotationConfigUtils.class.getClassLoader());


    /**
     * Register all relevant annotation post processors in the given registry.
     * @param registry the registry to operate on
     */
    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
        registerAnnotationConfigProcessors(registry, null);
    }

    /**
     * Register all relevant annotation post processors in the given registry.
     * @param registry the registry to operate on
     * @param source the configuration source element (already extracted)
     * that this registration was triggered from. May be <code>null</code>.
     * @return a Set of BeanDefinitionHolders, containing all bean definitions
     * that have actually been registered by this call
     */
    public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
            BeanDefinitionRegistry registry, Object source) {

        Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<BeanDefinitionHolder>(4);

        if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            throw new RuntimeException(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME+" not found.");
//            RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
//            def.setSource(source);
//            beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            throw new RuntimeException(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME+" not found.");
//            RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
//            def.setSource(source);
//            beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        if (!registry.containsBeanDefinition(REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            RootBeanDefinition def = new RootBeanDefinition(RequiredAnnotationBeanPostProcessor.class);
            def.setSource(source);
            beanDefs.add(registerPostProcessor(registry, def, REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        // Check for JSR-250 support, and if present add the CommonAnnotationBeanPostProcessor.
        if (jsr250Present && !registry.containsBeanDefinition(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            throw new RuntimeException(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME+" not found.");

//            RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
//            def.setSource(source);
//            beanDefs.add(registerPostProcessor(registry, def, COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        // Check for JPA support, and if present add the PersistenceAnnotationBeanPostProcessor.
        if (jpaPresent && !registry.containsBeanDefinition(PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            RootBeanDefinition def = new RootBeanDefinition();
            try {
                ClassLoader cl = AnnotationConfigUtils.class.getClassLoader();
                def.setBeanClass(cl.loadClass(PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME));
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException(
                        "Cannot load optional framework class: " + PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME, ex);
            }
            def.setSource(source);
            beanDefs.add(registerPostProcessor(registry, def, PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        return beanDefs;
    }

    private static BeanDefinitionHolder registerPostProcessor(
            BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName) {

        definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(beanName, definition);
        return new BeanDefinitionHolder(definition, beanName);
    }

    // fixme
//    static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd) {
//        if (abd.getMetadata().isAnnotated(Primary.class.getName())) {
//            abd.setPrimary(true);
//        }
//        if (abd.getMetadata().isAnnotated(Lazy.class.getName())) {
//            Boolean value = (Boolean) abd.getMetadata().getAnnotationAttributes(Lazy.class.getName()).get("value");
//            abd.setLazyInit(value);
//        }
//        if (abd.getMetadata().isAnnotated(DependsOn.class.getName())) {
//            String[] value = (String[]) abd.getMetadata().getAnnotationAttributes(DependsOn.class.getName()).get("value");
//            abd.setDependsOn(value);
//        }
//        if (abd instanceof AbstractBeanDefinition) {
//            if (abd.getMetadata().isAnnotated(Role.class.getName())) {
//                int value = (Integer) abd.getMetadata().getAnnotationAttributes(Role.class.getName()).get("value");
//                ((AbstractBeanDefinition)abd).setRole(value);
//            }
//        }
//    }

    // fixme
//    static BeanDefinitionHolder applyScopedProxyMode(
//            ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
//
//        ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
//        if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
//            return definition;
//        }
//        boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
//        return ScopedProxyCreator.createScopedProxy(definition, registry, proxyTargetClass);
//    }

}
