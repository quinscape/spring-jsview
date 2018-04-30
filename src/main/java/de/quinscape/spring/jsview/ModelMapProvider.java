package de.quinscape.spring.jsview;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Provides (partial) Spring model data as js view data.
 */
public class ModelMapProvider
    implements JsViewProvider
{
    private final Set<String> keys;

    /**
     * Creates a model map provider that provides all model map values as js view data.
     */
    public ModelMapProvider()
    {
        keys = Collections.emptySet();
    }

    /**
     * Creates a model map provider that provides a selection of model map keys as js view data.
     *
     * @param keys      keys to send to send as js view data, must not be null or empty.
     */
    public ModelMapProvider(Set<String> keys)
    {
        if (keys == null || keys.size() == 0)
        {
            throw new IllegalArgumentException("keys can't be null or empty");
        }

        this.keys = keys;
    }

    @Override
    public void provide(JsViewContext context)
    {
        final Map<String, ?> springModel = context.getSpringModel();

        final Set<String> keysToCopy = keys.size() > 0 ? keys : springModel.keySet();

        for (String key : keysToCopy)
        {
            context.provideViewData(key, springModel.get(key));
        }
    }
}
