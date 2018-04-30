package de.quinscape.spring.jsview.template;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mini-Template offering simple placeholder replacements. Is used to output the initial HTML to bootstrap the reactjs
 * application.
 * <p>
 *     A Base template consists of a list of {@link TemplatePart} implementations, either static string or a placeholder
 * </p>
 *
 * @see TextContent
 * @see Placeholder
 */
public final class BaseTemplate
{
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private final List<TemplatePart> parts;

    public BaseTemplate(String template)
    {
        int lastPos = 0;
        int pos;
        final List<TemplatePart> list = new ArrayList<>();

        boolean wasEscaped  = false;

        int len = template.length();

        while ((pos = template.indexOf('$', lastPos)) >= 0)
        {
            final boolean isEscape = pos != 0 && template.charAt(pos - 1) == '\\';

            if (pos > lastPos)
            {
                list.add(
                    new TextContent(
                        template.substring(
                            // if outputting an escaped variable, we have to start one character earlier on the $
                            wasEscaped ? lastPos - 1 : lastPos,
                            // also on the text before that, we have to swallow the backslash
                            isEscape ? pos - 1 : pos
                        )
                    )
                );
                wasEscaped = false;
            }

            if (isEscape)
            {
                wasEscaped = true;
                lastPos = pos + 1;
            }
            else
            {
                int end = pos + 1;
                while (end < len && isValidNameCharacter(template.charAt(end)))
                {
                    end++;
                }

                if (end == pos + 1)
                {
                    wasEscaped = true;
                }

                lastPos = end;
                if (end >= len)
                {
                    list.add(new Placeholder(template.substring(pos + 1)));
                    break;
                }
                else
                {
                    list.add(new Placeholder(template.substring(pos + 1, end)));
                }
            }
        }

        if (lastPos < len)
        {
            list.add(new TextContent(template.substring(wasEscaped ? lastPos - 1 : lastPos)));
        }

        parts = list;
    }


    private boolean isValidNameCharacter(char c)
    {
        return
            c == '_' ||
            (c >= 'A' && c <= 'Z') ||
            (c >= '0' && c <= '9');

    }


    public void write(OutputStream os, Map<String, Object> model) throws IOException
    {
        for (TemplatePart part : parts)
        {
            part.write(os, model);
        }
    }
}
