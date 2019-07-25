package com.wysong.billcoinconvert.config;


import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.types.ResolvedArrayType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.schema.DefaultTypeNameProvider;
import springfox.documentation.schema.ModelNameContext;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.DefaultsProviderPlugin;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spring.web.SpringGroupingStrategy;
import springfox.documentation.spring.web.plugins.DefaultConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.schema.Collections.containerType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.Types.typeNameFor;

/**
 * @since July 25, 2019.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Collections.singletonList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build()
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Bill to Coin Converter Api")
                .description("Converts BIlls into Coins")
                .version("0.0.1")
                .build();
    }

    private Predicate<String> paths() {
        return or(
                ant("/convert/**"),
                ant("/initialize"),
                ant("/inventory")
        );
    }

//    ***********  Copied the below fix from https://github.com/springfox/springfox/issues/2932  ********

    @Primary
    @Component
    public class DocumentationPluginsManagerBootAdapter extends DocumentationPluginsManager {
        @Autowired
        @Qualifier("resourceGroupingStrategyRegistry")
        private PluginRegistry<ResourceGroupingStrategy, DocumentationType> resourceGroupingStrategies;

        @Autowired
        @Qualifier("defaultsProviderPluginRegistry")
        private PluginRegistry<DefaultsProviderPlugin, DocumentationType> defaultsProviders;

        @Override
        public ResourceGroupingStrategy resourceGroupingStrategy(DocumentationType documentationType) {
            return resourceGroupingStrategies.getPluginOrDefaultFor(documentationType, new SpringGroupingStrategy());
        }

        @Override
        public DocumentationContextBuilder createContextBuilder(DocumentationType documentationType,
                                                                DefaultConfiguration defaultConfiguration) {
            return defaultsProviders.getPluginOrDefaultFor(documentationType, defaultConfiguration)
                    .create(documentationType).withResourceGroupingStrategy(resourceGroupingStrategy(documentationType));
        }

    }

    @Primary
    @Component
    public class TypeNameExtractorBootAdapter extends TypeNameExtractor {
        private final TypeResolver typeResolver;
        private final PluginRegistry<TypeNameProviderPlugin, DocumentationType> typeNameProviders;
        private final EnumTypeDeterminer enumTypeDeterminer;

        @Autowired
        public TypeNameExtractorBootAdapter(TypeResolver typeResolver,
                                            PluginRegistry<TypeNameProviderPlugin, DocumentationType> typeNameProviders,
                                            EnumTypeDeterminer enumTypeDeterminer) {
            super(typeResolver, typeNameProviders, enumTypeDeterminer);
            this.typeResolver = typeResolver;
            this.typeNameProviders = typeNameProviders;
            this.enumTypeDeterminer = enumTypeDeterminer;
        }

        public String typeName(ModelContext context) {
            ResolvedType type = asResolved(context.getType());
            if (isContainerType(type)) {
                return containerType(type);
            }
            return innerTypeName(type, context);
        }

        private ResolvedType asResolved(Type type) {
            return typeResolver.resolve(type);
        }

        private String genericTypeName(ResolvedType resolvedType, ModelContext context) {
            Class<?> erasedType = resolvedType.getErasedType();
            GenericTypeNamingStrategy namingStrategy = context.getGenericNamingStrategy();
            ModelNameContext nameContext = new ModelNameContext(resolvedType.getErasedType(),
                    context.getDocumentationType());
            String simpleName = Optional.ofNullable(typeNameFor(erasedType)).orElse(typeName(nameContext));
            StringBuilder sb = new StringBuilder(String.format("%s%s", simpleName, namingStrategy.getOpenGeneric()));
            boolean first = true;
            for (int index = 0; index < erasedType.getTypeParameters().length; index++) {
                ResolvedType typeParam = resolvedType.getTypeParameters().get(index);
                if (first) {
                    sb.append(innerTypeName(typeParam, context));
                    first = false;
                } else {
                    sb.append(String.format("%s%s", namingStrategy.getTypeListDelimiter(),
                            innerTypeName(typeParam, context)));
                }
            }
            sb.append(namingStrategy.getCloseGeneric());
            return sb.toString();
        }

        private String innerTypeName(ResolvedType type, ModelContext context) {
            if (type.getTypeParameters().size() > 0 && type.getErasedType().getTypeParameters().length > 0) {
                return genericTypeName(type, context);
            }
            return simpleTypeName(type, context);
        }

        private String simpleTypeName(ResolvedType type, ModelContext context) {
            Class<?> erasedType = type.getErasedType();
            if (type instanceof ResolvedPrimitiveType) {
                return typeNameFor(erasedType);
            } else if (enumTypeDeterminer.isEnum(erasedType)) {
                return "string";
            } else if (type instanceof ResolvedArrayType) {
                GenericTypeNamingStrategy namingStrategy = context.getGenericNamingStrategy();
                return String.format("Array%s%s%s", namingStrategy.getOpenGeneric(),
                        simpleTypeName(type.getArrayElementType(), context), namingStrategy.getCloseGeneric());
            } else if (type instanceof ResolvedObjectType) {
                String typeName = typeNameFor(erasedType);
                if (typeName != null) {
                    return typeName;
                }
            }
            return typeName(new ModelNameContext(type.getErasedType(), context.getDocumentationType()));
        }

        private String typeName(ModelNameContext context) {
            TypeNameProviderPlugin selected = typeNameProviders.getPluginOrDefaultFor(context.getDocumentationType(),
                    new DefaultTypeNameProvider());
            return selected.nameFor(context.getType());
        }

    }
}
