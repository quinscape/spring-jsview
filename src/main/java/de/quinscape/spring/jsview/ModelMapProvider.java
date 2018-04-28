package de.quinscape.spring.jsview;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides Spring model data as js view data.
 */
public class ModelMapProvider
    implements JsViewDataProvider
{
    private final Set<String> keys;

    /**
     * Creates a model map provider that provides all model map values as js view data.
     */
    public ModelMapProvider()
    {
        this(Collections.emptySet());
    }

    /**
     * Creates a model map provider that provides a selection of model map keys as js view data.
     *
     * @param keys      keys to send to send as js view data
     */
    public ModelMapProvider(Set<String> keys)
    {
        this.keys = keys;
    }

    @Override
    public Map<String, Object> provide(
        String entryPoint,
        Map<String, ?> model,
        HttpServletRequest request
    )
    {
        Map<String, Object> map;

        if (keys.size() == 0)
        {
            map = (Map<String, Object>) model;
        }
        else
        {
            map = new HashMap<>();
            for (String key : keys)
            {
                map.put(key, model.get(key));
            }
        }

        return map;
    }
}
