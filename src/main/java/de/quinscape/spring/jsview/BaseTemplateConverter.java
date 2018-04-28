package de.quinscape.spring.jsview;

import de.quinscape.spring.jsview.loader.ResourceConverter;
import de.quinscape.spring.jsview.template.BaseTemplate;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

class BaseTemplateConverter
    implements ResourceConverter<BaseTemplate>
{
    @Override
    public BaseTemplate readStream(InputStream data)
    {
        try
        {
            final String template = IOUtils.toString(data, BaseTemplate.UTF_8);
            return new BaseTemplate(template);
        }
        catch (IOException e)
        {
            throw new JsViewException(e);
        }
    }


    @Override
    public byte[] toByteArray(BaseTemplate value)
    {
        throw new UnsupportedOperationException();
    }
}
