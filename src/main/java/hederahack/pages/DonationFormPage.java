package hederahack.pages;

import static org.rendersnake.HtmlAttributesFactory.name;
import static org.rendersnake.HtmlAttributesFactory.type;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

public class DonationFormPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1()
                .write("Donation")
                ._h1();

        html.form(name("donate-form").method("POST").action("donate"));
        html.fieldset();

        RenderUtils.renderInputField(html, "Charity Id", "");
        RenderUtils.renderInputField(html, "Amount (enter number)", "");

        html.button(type("submit").class_("btn btn-primary")).content("Submit");

        html._fieldset();
        html._form();
    }
}
