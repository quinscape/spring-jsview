package de.quinscape.spring.jsview;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Context for a JsView invocation
 */
final class DefaultJsViewContext
    implements JsViewContext
{
    private final Map<String,Object> model = new HashMap<>();
    private final Map<String,Object> modelRO = Collections.unmodifiableMap(model);

    private final Map<String,Object> viewData = new HashMap<>();
    private final Map<String,Object> viewDataRO = Collections.unmodifiableMap(viewData);

    private final JsView jsView;

    private final Map<String, ?> springModelRO;

    private final HttpServletRequest request;


    DefaultJsViewContext(JsView jsView, Map<String, ?> springModel, HttpServletRequest request)
    {
        this.jsView = jsView;
        this.springModelRO = Collections.unmodifiableMap(springModel);
        this.request = request;
    }

    @Override
    public JsView getJsView()
    {
        return jsView;
    }


    @Override
    public void setPlaceholderValue(String name, Object value)
    {
        model.put(name, value);
    }

    @Override
    public Map<String, Object> getPlaceHolderValues()
    {
        return modelRO;
    }


    @Override
    public Map<String, ?> getSpringModel()
    {
        return springModelRO;
    }


    @Override
    public HttpServletRequest getRequest()
    {
        return request;
    }


    @Override
    public Map<String, Object> getViewData()
    {
        return viewDataRO;
    }


    @Override
    public void provideViewData(String name, Object value)
    {
        viewData.put(name, value);
    }
}
