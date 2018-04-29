package de.quinscape.spring.jsview.asset;

import de.quinscape.spring.jsview.util.Util;
import org.svenson.JSONParameters;
import org.svenson.JSONTypeHint;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class WebpackAssets
{
    private static final String CSS_EXTENSION = ".css";

    private static final String JS_EXTENSION = ".js";

    public static final String RESOURCES_PREFIX = "resources:";

    private final Map<String,EntryPoint> entryPoints;

    public WebpackAssets(
        @JSONParameters
        @JSONTypeHint(EntryPoint.class)
        Map<String, EntryPoint> entryPoints
    )
    {
        this.entryPoints = entryPoints;
    }


    public Map<String, EntryPoint> getEntryPoints()
    {
        return entryPoints;
    }


    public String renderAssets(HttpServletRequest request, String entryPointName)
    {
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
            final List<String> assets = entryPoint.getAssets();
            for (String asset : assets)
            {
                renderAsset(
                    buff,
                    contextPath + "/js/" + asset
                );

            }
        }


        return buff.toString();
    }

    private void renderAsset(StringBuilder buff, String uri)
    {
        if (uri.endsWith(CSS_EXTENSION))
        {
            buff.append("<link rel=\"stylesheet\" href=\"")
                .append(uri)
                .append("\">\n");
        }
        else if (uri.endsWith(JS_EXTENSION))
        {
            buff.append("<script src=\"")
                .append(uri)
                .append("\"></script>\n");

        }
        else
        {
            buff.append("<!-- ASSET ").append(uri).append("-->\n");
        }
    }

    public boolean hasEntryPoint(String entryPoint)
    {
        return entryPoints != null && entryPoints.containsKey(entryPoint);
    }
}
