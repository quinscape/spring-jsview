package de.quinscape.spring.jsview;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Encapsulates the context of a JsView invocation and provides method to provide view data and template placeholder
 * values.
 */
public interface JsViewContext
{
    /**
     * Returns the js view this context corresponds to.
     *
     * @return  js view
     */
    JsView getJsView();

    /**
     * Returns the current placeholder values.
     *
     * @return  placeholder values
     */
    Map<String, Object> getPlaceHolderValues();

    /**
     * Returns the spring model map.
     *
     * @return spring model map
     */
    Map<String, ?> getSpringModel();

    /**
     * Returns the current http servlet request.
     *
     * @return current http servlet request
     */
    HttpServletRequest getRequest();

    /**
     * Returns the current view data.
     * 
     * @return current view data
     */
    Map<String, Object> getViewData();

    /**
     * Sets the placeholder with the given name to the given value.
     *
     * @param name      place holder name
     * @param value     place holder value
     */
    void setPlaceholderValue(String name, Object value);

    /**
     * Provides the given view data value under the given name.
     *
     * @param name      view data slice name
     * @param value     view data value
     */
    void provideViewData(String name, Object value);
}
