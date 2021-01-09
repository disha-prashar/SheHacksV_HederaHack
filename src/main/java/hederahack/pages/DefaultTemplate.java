package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.ext.spring.template.TemplateDescriptor;
import static org.rendersnake.HtmlAttributesFactory.*;

public class DefaultTemplate implements TemplateDescriptor {

    // Define the default title of your page use in <title></title>
    @Override
    public String getDefaultTitle() {
        return "RenderSnake";
    }

    // Html write in <head></head>
    @Override
    public void renderHeaderOn(HtmlCanvas html) throws IOException {
        html.meta(charset("utf-8"))
                .meta(http_equiv("X-UA-Compatible").content("IE=edge"))
                .meta(name("viewport").content("width=device-width, initial-scale=1"))

                // Import Bootstrap + Font-awesome libraries and css
                .macros().stylesheet("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css")
                .macros().javascript("https://code.jquery.com/jquery-3.4.1.slim.min.js")
                .macros().javascript("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js");
    }

    // Html write just after <body>
    @Override
    public void renderBodyStartOn(HtmlCanvas html) throws IOException {
        html.div(class_("container"));
    }

    // Html write just before </body>
    @Override
    public void renderBodyEndOn(HtmlCanvas html) throws IOException {
        html._div(); // end container
    }
}