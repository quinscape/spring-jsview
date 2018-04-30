package de.quinscape.spring.jsview;

/**
 * Implemented by classes providing preloaded data or template placeholders to the js views.
 */
public interface JsViewProvider
{
    /**
     * Uses the received context object to declare view data or template placeholder content.
     *
     * @param context       js view context
     *
     * @throws Exception    if something goes wrong
     */
    void provide(JsViewContext context) throws Exception;
}
