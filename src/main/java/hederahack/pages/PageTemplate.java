package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import static org.rendersnake.HtmlAttributesFactory.*;

public class PageTemplate implements Renderable {

    Renderable inner;

    public PageTemplate(Renderable r) {
        this.inner = r;

    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.meta(charset("utf-8"))
                .meta(http_equiv("X-UA-Compatible").content("IE=edge"))
                .meta(name("viewport").content("width=device-width, initial-scale=1"))
                .macros().stylesheet("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css");

        html.body();
        html.div(class_("container"));

        inner.renderOn(html);

        html._div();
        html._body();
        html.macros().javascript("https://code.jquery.com/jquery-3.4.1.slim.min.js")
                .macros().javascript("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js");

    }
}