package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.ext.spring.template.Template;
import org.springframework.stereotype.Component;
import static org.rendersnake.HtmlAttributesFactory.*;

@Component
@Template(DefaultTemplate.class)
public class LoginPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1()
                .write("Login")
                ._h1();

        html.form(name("Login").method("GET").action("donate")); // This goes directly to the donate form. No real authentication

        html.div();

        html.fieldset();
        html.label(for_("userId")).content("User Id:")
                .input(id("userId").type("input").value(""));
        html._fieldset();

        html.fieldset();
        html.label(for_("password")).content("Password:")
                .input(id("password").type("password").value(""));
        html._fieldset();

        html.input(type("submit").value("Login"));

        html._div();

        html._form();
    }
}