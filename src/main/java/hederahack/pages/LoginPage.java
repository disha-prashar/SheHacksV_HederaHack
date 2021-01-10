package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.rendersnake.HtmlAttributesFactory.*;

public class LoginPage implements Renderable {

    @Override
    @ResponseBody
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1()
                .write("Login")
                ._h1();

        html.form(name("login").method("GET").action("donate"));

        RenderUtils.renderInputField(html, "User", "", "input");
        RenderUtils.renderInputField(html, "Password", "", "password");

        html.input(type("submit").class_("btn btn-primary").value("Submit"));

        html._form();
    }
}