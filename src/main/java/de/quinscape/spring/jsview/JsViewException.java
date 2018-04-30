package de.quinscape.spring.jsview;

/**
 * Generic js view exception.
 */
public class JsViewException
    extends RuntimeException
{
    private static final long serialVersionUID = -2182329097363811164L;


    public JsViewException(String message)
    {
        super(message);
    }


    public JsViewException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public JsViewException(Throwable cause)
    {
        super(cause);
    }


    public JsViewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
