package de.quinscape.spring.jsview.template;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

final class TextContent
    implements TemplatePart
{
    private final byte[] content;

    public TextContent(String content)
    {
        if (content == null)
        {
            throw new IllegalArgumentException("content can't be null");
        }
        this.content = content.getBytes(BaseTemplate.UTF_8);
    }

    @Override
    public void write(OutputStream os, Map<String, Object> model) throws IOException
    {
        os.write(content);
    }
}
