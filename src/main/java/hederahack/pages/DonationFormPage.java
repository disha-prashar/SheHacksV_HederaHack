package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.ext.spring.template.Template;
import org.springframework.stereotype.Component;
import static org.rendersnake.HtmlAttributesFactory.*;

@Component
@Template(DefaultTemplate.class)
public class DonationFormPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1()
                .write("Donation")
                ._h1();

        html.form(name("Donate").method("POST").action("donate"));

        html.div();

        html.fieldset();
        html.label(for_("userId")).content("User Id:")
                .input(id("userId").type("input").value(""));
        html._fieldset();

        html.input(type("submit").value("Submit"));

        html._div();

        html._form();
    }
}