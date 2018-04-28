package de.quinscape.spring.jsview.template;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

final class Placeholder
    implements TemplatePart
{

    private final String name;


    public Placeholder(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("name can't be null");
        }

        this.name = name;
    }


    @Override
    public void write(OutputStream os, Map<String, Object> model) throws IOException
    {
        final Object value = model.get(name);
        if (value != null)
        {
            os.write(value.toString().getBytes(BaseTemplate.UTF_8));
        }
    }
}
