package de.quinscape.spring.jsview.loader;


import de.quinscape.spring.jsview.JsViewException;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * Encapsulates the kind of change that happened to a resource.
 */
public enum ResourceEvent
{
    CREATED, MODIFIED, DELETED;

    public static ResourceEvent forWatchEvent(WatchEvent event)
    {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE))
        {
            return CREATED;
        }
        else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE))
        {
            return DELETED;
        }
        else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY))
        {
            return MODIFIED;
        }
        throw new JsViewException("Cannot convert event into module resource event" + event);
    }
}
