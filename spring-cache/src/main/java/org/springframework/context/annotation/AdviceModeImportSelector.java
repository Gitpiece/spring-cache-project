package org.springframework.context.annotation;


import org.springframework.core.GenericTypeResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Convenient base class for {@link ImportSelector} implementations that select imports
 * based on an {@link AdviceMode} value from an annotation (such as the {@code @Enable*}
 * annotations).
 *
 * @param <A> Annotation containing {@linkplain #getAdviceModeAttributeName() AdviceMode
 * attribute}
 *
 * @author Chris Beams
 * @since 3.1
 */
public abstract class AdviceModeImportSelector<A extends Annotation> {//implements ImportSelector {

    public static final String DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME = "mode";

    /**
     * The name of the {@link AdviceMode} attribute for the annotation specified by the
     * generic type {@code A}. The default is {@value #DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME},
     * but subclasses may override in order to customize.
     */
    protected String getAdviceModeAttributeName() {
        return DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation resolves the type of annotation from generic metadata and
     * validates that (a) the annotation is in fact present on the importing
     * {@code @Configuration} class and (b) that the given annotation has an
     * {@linkplain #getAdviceModeAttributeName() advice mode attribute} of type
     * {@link AdviceMode}.
     *
     * <p>The {@link #selectImports(AdviceMode)} method is then invoked, allowing the
     * concrete implementation to choose imports in a safe and convenient fashion.
     *
     * @throws IllegalArgumentException if expected annotation {@code A} is not present
     * on the importing {@code @Configuration} class or if {@link #selectImports(AdviceMode)}
     * returns {@code null}
     */
    public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annoType = GenericTypeResolver.resolveTypeArgument(getClass(), AdviceModeImportSelector.class);

        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(annoType.getName());
        Assert.notNull(attributes, String.format(
                "@%s is not present on importing class '%s' as expected",
                annoType.getSimpleName(), importingClassMetadata.getClassName()));

        String modeAttrName = getAdviceModeAttributeName();
        Assert.hasText(modeAttrName);

        Object adviceMode = attributes.get(modeAttrName);
        Assert.notNull(adviceMode, String.format(
                "Advice mode attribute @%s#%s() does not exist",
                annoType.getSimpleName(), modeAttrName));

        Assert.isInstanceOf(AdviceMode.class, adviceMode, String.format(
                "Incorrect type for advice mode attribute '@%s#%s()': ",
                annoType.getSimpleName(), modeAttrName));

        String[] imports = selectImports((AdviceMode) adviceMode);
        Assert.notNull(imports, String.format("Unknown AdviceMode: '%s'", adviceMode));

        return imports;
    }

    /**
     * Determine which classes should be imported based on the given {@code AdviceMode}.
     *
     * <p>Returning {@code null} from this method indicates that the {@code AdviceMode} could
     * not be handled or was unknown and that an {@code IllegalArgumentException} should
     * be thrown.
     *
     * @param adviceMode the value of the {@linkplain #getAdviceModeAttributeName()
     * advice mode attribute} for the annotation specified via generics.
     *
     * @return array containing classes to import; empty array if none, {@code null} if
     * the given {@code AdviceMode} is unknown.
     */
    protected abstract String[] selectImports(AdviceMode adviceMode);

}
