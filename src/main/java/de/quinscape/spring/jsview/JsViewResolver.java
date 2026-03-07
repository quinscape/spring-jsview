package de.quinscape.spring.jsview;

import de.quinscape.spring.jsview.loader.ResourceHandle;
import de.quinscape.spring.jsview.loader.ResourceLoader;
import de.quinscape.spring.jsview.loader.ServletResourceLoader;
import de.quinscape.spring.jsview.template.BaseTemplate;
import de.quinscape.spring.jsview.webpack.WebpackAssetProvider;
import de.quinscape.spring.jsview.webpack.WebpackAssets;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Resolves views for webpack build endpoints.
 */
public final class JsViewResolver
    implements ViewResolver, Ordered
{

    private final AssetProvider assetProvider;

    private final ResourceHandle<BaseTemplate> baseTemplateHandle;

    private final Set<JsViewProvider> viewDataProviders;

    private final int order;


    private JsViewResolver(
        Set<JsViewProvider> viewDataProviders,
        ResourceLoader resourceLoader,
        String baseTemplatePath,
        int order,
        AssetProvider assetProvider
    )
    {
        this.assetProvider = assetProvider;
        this.viewDataProviders = viewDataProviders;
        this.order = order;

        baseTemplateHandle =
            resourceLoader.getResourceHandle(
                baseTemplatePath,
                new BaseTemplateConverter()
            );
    }


    @Override
    public @Nullable View resolveViewName( @NonNull String entryPoint, @NonNull Locale locale) throws IOException
    {

        if (assetProvider.hasEntryPoint(entryPoint) || entryPoint.startsWith(WebpackAssetProvider.RESOURCES_PREFIX))
        {
            return new JsView(
                assetProvider,
                baseTemplateHandle,
                viewDataProviders,
                entryPoint,
                locale
            );
        }

        // let other resolvers handle the view
        return null;
    }

    public static Builder newResolver(ServletContext servletContext, String templatePath)
    {
        return new Builder(servletContext, templatePath);
    }


    @Override
    public int getOrder()
    {
        return order;
    }


    public final static class Builder
    {
        private final ServletContext servletContext;

        private Set<JsViewProvider> viewDataProviders = new HashSet<>();

        private ResourceLoader resourceLoader;

        private String baseTemplatePath;

        private AssetProvider assetProvider = null;

        private int order = 0;


        public JsViewResolver build()
        {
            if (baseTemplatePath == null)
            {
                throw new IllegalStateException("baseTemplatePath can't be null");
            }
            if (assetProvider == null)
            {
                assetProvider = new WebpackAssetProvider(this.resourceLoader);
            }

            final ResourceLoader resourceLoader;
            if (this.resourceLoader != null)
            {
                resourceLoader = this.resourceLoader;
            }
            else
            {
                try
                {
                    resourceLoader = new ServletResourceLoader(servletContext, "/", true);
                }
                catch (IOException e)
                {
                    throw new JsViewException("Error creating default resource loader", e);
                }
            }
            return new JsViewResolver(
                Collections.unmodifiableSet(viewDataProviders), resourceLoader, baseTemplatePath, order, assetProvider
            );
        }

        public Builder(ServletContext servletContext, String templatePath)
        {
            if (servletContext == null)
            {
                throw new IllegalArgumentException("servletContext can't be null");
            }

            if (templatePath == null)
            {
                throw new IllegalArgumentException("templatePath can't be null");
            }

            this.servletContext = servletContext;
            this.baseTemplatePath = templatePath;
        }


        /**
         * Configures the given view data provider to use with this view resolver.
         *
         * @param viewDataProvider      view data provider
         *
         * @return  this builder
         */
        public Builder withViewDataProvider(JsViewProvider viewDataProvider)
        {
            viewDataProviders.add(viewDataProvider);
            return this;
        }


        public Set<JsViewProvider> getViewDataProviders()
        {
            return viewDataProviders;
        }

        public int getOrder()
        {
            return order;
        }


        /**
         * Set the order reported by the view resolver following the {@link Ordered} contract.
         *
         * @param order     order value
         *
         * @return  this builder
         */
        public Builder withOrder(int order)
        {
            this.order = order;
            return this;
        }


        public ResourceLoader getResourceLoader()
        {
            return resourceLoader;
        }


        /**
         * Configure the resource loader to use for the view resolver resources.
         *
         * @param resourceLoader        resource loader
         *
         * @return  this builder
         */
        public Builder withResourceLoader(ResourceLoader resourceLoader)
        {
            this.resourceLoader = resourceLoader;
            return this;
        }


        /**
         * Configures the asset provider to use for the view provider. The default is {@link WebpackAssetProvider} which
         * only works with webpack.
         * 
         * @param assetProvider     asset provider to use
         *                          
         * @return  this builder
         */
        public Builder withAssetProvider(AssetProvider assetProvider)
        {
            this.assetProvider = assetProvider;
            return this;
        }


        public String getBaseTemplatePath()
        {
            return baseTemplatePath;
        }
    }
}
