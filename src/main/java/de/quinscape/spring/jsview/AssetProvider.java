package de.quinscape.spring.jsview;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Renders the assets for an entry point as HTML code.
 */
public interface AssetProvider
{
    /**
     * Renders the assets for the given entry point
     * @param request           HTTP servlet request
     * @param entryPointName    entry point name
     * @return HTML
     */
    String renderAssets(HttpServletRequest request, String entryPointName);

    /**
     * Returns true if the given entry point name exists.
     * @param entryPointName    entry point name
     * @return true if entry point exists
     */
    boolean hasEntryPoint(String entryPointName);


    String CSS_EXTENSION = ".css";

    String JS_EXTENSION = ".js";

    default void renderAsset(StringBuilder buff, String uri)
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
}
