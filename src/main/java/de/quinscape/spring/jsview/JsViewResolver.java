package de.quinscape.spring.jsview;

import de.quinscape.spring.jsview.loader.JSONResourceConverter;
import de.quinscape.spring.jsview.loader.ResourceHandle;
import de.quinscape.spring.jsview.loader.ResourceLoader;
import de.quinscape.spring.jsview.template.BaseTemplate;
import de.quinscape.spring.jsview.util.WebpackAssets;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

/**
 * Resolves views for webpack build endpoints.
 */
public class JsViewResolver
    implements ViewResolver
{
    private static final String WEBPACK_ASSETS_PATH = "js/webpack-assets.json";

    private final ResourceHandle<WebpackAssets> webpackAssetsHandle;

    private final ResourceHandle<BaseTemplate> baseTemplateHandle;

    private final Set<JsViewDataProvider> viewDataProviders;

    public JsViewResolver(
        Set<JsViewDataProvider> viewDataProviders,
        ResourceLoader resourceLoader,
        String baseTemplatePath
    )
    {


        webpackAssetsHandle =
            resourceLoader.getResourceHandle(
                WEBPACK_ASSETS_PATH,
                new JSONResourceConverter<>(WebpackAssets.class)
            );
        this.viewDataProviders = viewDataProviders;

        baseTemplateHandle =
            resourceLoader.getResourceHandle(
                baseTemplatePath,
                new BaseTemplateConverter()
            );
    }


    @Override
    public View resolveViewName( String entryPoint, Locale locale) throws IOException
    {
        final WebpackAssets assets = webpackAssetsHandle.getContent();

        if (assets.hasEntryPoint(entryPoint) || entryPoint.startsWith(WebpackAssets.RESOURCES_PREFIX))
        {
            return new JsView(
                assets,
                baseTemplateHandle,
                viewDataProviders,
                entryPoint,
                locale
            );
        }

        // let other resolvers handle the view
        return null;
    }
}
