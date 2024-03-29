/*
 * @(#)ConfigurationBuilder.java     4 Dec 2010
 *
 * Copyright © 2010 Andrew Phillips.
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package com.qrmedia.commons.multispi.config;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Supplier;
import com.qrmedia.commons.multispi.MultiSpi;
import com.qrmedia.commons.multispi.provider.AnnotationScanningProvider;
import com.qrmedia.commons.multispi.provider.MetaInfServicesProvider;
import com.qrmedia.commons.multispi.provider.ServiceClassnameAttributeProvider;
import com.qrmedia.commons.multispi.provider.ServiceImplementationProvider;

/**
 * Convenience helper to construct a {@link MultiSpi} instance with a
 * desired configuration.
 * 
 * @author aphillips
 * @since 4 Dec 2010
 *
 */
@NotThreadSafe
public final class MultiSpiBuilder implements Supplier<MultiSpi> {
    private final Set<ServiceImplementationProvider> providers = newHashSet();
    
    /**
     * Adds default SPI support, i.e. {@link #withMetaInfServicesScanning() &quot;vanilla&quot; META-INF/services} scanning,
     * to the {@link MultiSpi} instance being built.
     * 
     * @return this builder
     */
    public MultiSpiBuilder withDefaults() {
        return this.withMetaInfServicesScanning();
    }

    /**
     * Adds support for {@linkplain MetaInfServicesProvider &quot;vanilla&quot; META-INF/services} scanning
     * to the {@link MultiSpi} instance being built.
     * 
     * @return this builder
     */
    public MultiSpiBuilder withMetaInfServicesScanning() {
        providers.add(new MetaInfServicesProvider());
        return this;
    }

    /**
     * Adds support for {@linkplain AnnotationScanningProvider marker annotation} scanning
     * to the {@link MultiSpi} instance being built.
     * 
     * @return this builder
     */
    public MultiSpiBuilder withAnnotationScanning(@Nonnull Class<? extends Annotation> markerAnnotation,
            @Nonnull String basePackage) {
        providers.add(new AnnotationScanningProvider(markerAnnotation, basePackage));
        return this;
    }

    /**
     * Adds support for {@linkplain ServiceClassnameAttributeProvider manifest service attribute} scanning
     * to the {@link MultiSpi} instance being built.
     * 
     * @return this builder
     */
    public MultiSpiBuilder withManifestServiceClassnameAttributeScanning() {
        providers.add(new ServiceClassnameAttributeProvider());
        return this;
    }
    
    /**
     * Adds the given providers to the providers for the {@link MultiSpi} instance
     * being built.
     * <p>
     * Implementation note: as the providers are stored in a set, the implementation
     * of {@link Object#hashCode() hashCode()} and {@link Object#equals(Object) equals(Object)}
     * will determine whether duplicate providers are present.
     *  
     * @param providers the providers to be added
     * @return this builder
     */
    public MultiSpiBuilder withProviders(ServiceImplementationProvider... providers) {
        this.providers.addAll(asList(providers));
        return this;
    }
    
    /**
     * Builds the prepared {@code MultiSpi} instance.
     * 
     * @return the prepared {@code MultiSpi} instance
     */
    public MultiSpi build() { 
        return new MultiSpi(providers);
    }

    /**
     * Gets the prepared {@code MultiSpi} instance.
     * 
     * @return see {@link #build()}
     */
    public MultiSpi get() {
        return build();
    }
}
