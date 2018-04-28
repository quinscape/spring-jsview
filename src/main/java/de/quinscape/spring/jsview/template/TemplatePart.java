package de.quinscape.spring.jsview.template;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Part of a base template
 */
interface TemplatePart
{
    void write(OutputStream os, Map<String, Object> model) throws IOException;
}

