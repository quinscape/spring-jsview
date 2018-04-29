package de.quinscape.spring.jsview;

import de.quinscape.spring.jsview.loader.ResourceHandle;
import de.quinscape.spring.jsview.template.BaseTemplate;
import de.quinscape.spring.jsview.util.JSONUtil;
import de.quinscape.spring.jsview.asset.WebpackAssets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * React view implementation for spring.
 *
 * Render as HTML base template with an embedded JSON data block.
 *
 */
public class JsView
    implements View
{

    private final static Logger log = LoggerFactory.getLogger(JsView.class);

    private static final String X_REQUESTED_WITH = "x-requested-With";

    private static final String WHATWG_FETCH = "whatwg-fetch";

    private final ResourceHandle<BaseTemplate> baseTemplateHandle;


    private final Set<JsViewDataProvider> viewDataProviders;

    private final String entryPoint;

    private final Locale locale;


    private final WebpackAssets webpackAssets;


    public JsView(
        WebpackAssets webpackAssets,
        ResourceHandle<BaseTemplate> baseTemplateHandle,
        Set<JsViewDataProvider> viewDataProviders,
        String entryPoint,
        Locale locale
    )
    {
        this.viewDataProviders = viewDataProviders;
        this.entryPoint = entryPoint;
        this.locale = locale;
        this.webpackAssets = webpackAssets;
        this.baseTemplateHandle = baseTemplateHandle;
    }

    @Override
    public String getContentType()
    {
        return MediaType.TEXT_HTML_VALUE;
    }


    @Override
    public void render(
        Map<String, ?> map,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception
    {
        Map<String,Object> viewData = new HashMap<>();

        for (JsViewDataProvider provider : viewDataProviders)
        {
            viewData.putAll(provider.provide(entryPoint, map, request));
        }

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding("UTF-8");

        ServletOutputStream os = null;
        try
        {
            // create a new model map for the template
            final Map<String, Object> templateModel = new HashMap<>();
            // containing the context path
            templateModel.put("CONTEXT_PATH", request.getContextPath());
            // and the JSON for our current spring model
            templateModel.put("VIEW_DATA", JSONUtil.DEFAULT_GENERATOR.forValue(viewData));
            templateModel.put("ASSETS", webpackAssets.renderAssets(request, entryPoint));

            // evaluate and write template
            os = response.getOutputStream();
            baseTemplateHandle.getContent().write(os, templateModel);
            os.flush();
        }
        catch (IOException e)
        {
            IOUtils.closeQuietly(os);

            // these are most commonly browser windows being closed before the last request is done
            log.debug("Error sending view", e);
        }
        catch (Exception e)
        {
            IOUtils.closeQuietly(os);
            // log non IO exception
            log.error("Error sending view", e);
        }
    }
}
