package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import org.springframework.stereotype.Component;

import static org.rendersnake.HtmlAttributesFactory.*;

public class DonationFormPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1()
                .write("Donation")
                ._h1();

        html.form(name("donate-form").method("POST").action("donate"));
        html.fieldset();

        RenderUtils.renderInputField(html, "User Id", "");
        RenderUtils.renderInputField(html, "Charity Id", "");
        RenderUtils.renderInputField(html, "Amount", "");

        html.button(type("submit").class_("btn btn-primary")).content("Submit");

        html._fieldset();
        html._form();
    }
}