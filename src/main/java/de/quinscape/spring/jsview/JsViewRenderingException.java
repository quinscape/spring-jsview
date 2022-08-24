package de.quinscape.spring.jsview;

/**
 * Throw when an error happens during rendering including the invocation of the registered {@link JsViewProvider}
 * instances.
 */
public class JsViewRenderingException
    extends RuntimeException
{
    private static final long serialVersionUID = 7466726245500377281L;


    public JsViewRenderingException(String message)
    {
        super(message);
    }


    public JsViewRenderingException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public JsViewRenderingException(Throwable cause)
    {
        super(cause);
    }


    public JsViewRenderingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
