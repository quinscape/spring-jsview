package de.quinscape.spring.jsview;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Implemented by classes providing preloaded data to js views.
 */
public interface JsViewDataProvider
{
    /**
     * Provides a map object that will be jsonified for the client side.
     *
     * @param entryPoint        webpack entry point name
     * @param model             spring's model map
     * @param request           HTTP servlet request
     *
     * @return  data map
     * 
     * @throws Exception    if things go wrong
     */
    Map<String,Object> provide(String entryPoint, Map<String, ?> model, HttpServletRequest request) throws Exception;
}
