package hederahack.pages;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.id;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;

public class RenderUtils {

    static void renderInputField(HtmlCanvas html, String label, String value) throws IOException {
        renderInputField(html, label, value, "input");
    }

    static void renderInputField(HtmlCanvas html, String label, String value, String type) throws IOException {
        String id = label.toLowerCase().replaceAll(" ", "-");

        html.div(class_("form-group"));


        html.label(for_(id)).content(label)
                .input(id(id).name(id).class_("form-control").type(type).value(value));

        html._div();
    }
}
