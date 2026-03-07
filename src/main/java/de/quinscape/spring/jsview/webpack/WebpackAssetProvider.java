package de.quinscape.spring.jsview.webpack;

import de.quinscape.spring.jsview.AssetProvider;
import de.quinscape.spring.jsview.JsViewException;
import de.quinscape.spring.jsview.loader.JSONResourceConverter;
import de.quinscape.spring.jsview.loader.ResourceHandle;
import de.quinscape.spring.jsview.loader.ResourceLoader;
import de.quinscape.spring.jsview.util.Util;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebpackAssetProvider
    implements AssetProvider
{
    private static final String WEBPACK_ASSETS_PATH = "js/webpack-assets.json";


    public static final String RESOURCES_PREFIX = "resources:";

    private final ResourceHandle<WebpackAssets> webpackAssetsHandle;


    public WebpackAssetProvider(ResourceLoader resourceLoader)
    {
        this.webpackAssetsHandle = resourceLoader.getResourceHandle(
            WEBPACK_ASSETS_PATH,
            new JSONResourceConverter<>(WebpackAssets.class)
        );

    }


    @Override
    public String renderAssets(HttpServletRequest request, String entryPointName)
    {

        final Map<String, EntryPoint> entryPoints;
        try
        {
            entryPoints = webpackAssetsHandle.getContent().getEntryPoints();

            final StringBuilder buff = new StringBuilder();
            final String contextPath = request.getContextPath();

            if (entryPointName.startsWith(RESOURCES_PREFIX))
            {
                final List<String> assets = Util.split(entryPointName.substring(RESOURCES_PREFIX.length()), ";");

                for (String asset : assets)
                {
                    renderAsset(
                        buff,
                        contextPath + asset
                    );
                }
            }
            else
            {
                final EntryPoint entryPoint = entryPoints.get(entryPointName);
                if (entryPoint == null)
                {
                    throw new IllegalStateException("Entry point '" +entryPointName + "' not found");
                }
                final List<Map<String,Object>> assets = entryPoint.getAssets();
                for (Map<String, Object> asset : assets)
                {
                    renderAsset(
                        buff,
                        contextPath + "/js/" + asset.get("name")
                    );

                }
            }
            return buff.toString();
        }
        catch (IOException e)
        {
            throw new JsViewException(e);
        }
    }


    @Override
    public boolean hasEntryPoint(String entryPoint)
    {
        final Map<String, EntryPoint> entryPoints;
        try
        {
            entryPoints = webpackAssetsHandle.getContent().getEntryPoints();
            return entryPoints != null && entryPoints.containsKey(entryPoint);
        }
        catch (IOException e)
        {
            throw new JsViewException(e);
        }

    }

}
