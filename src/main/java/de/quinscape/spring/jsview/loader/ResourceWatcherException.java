package de.quinscape.spring.jsview.loader;


import de.quinscape.spring.jsview.JsViewException;

public class ResourceWatcherException
    extends JsViewException
{
    private static final long serialVersionUID = -  1626961630936877528L;


    public ResourceWatcherException(String message)
    {
        super(message);
    }


    public ResourceWatcherException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ResourceWatcherException(Throwable cause)
    {
        super(cause);
    }
}
