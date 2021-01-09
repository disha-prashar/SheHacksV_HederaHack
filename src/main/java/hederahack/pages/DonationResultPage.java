package hederahack.pages;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.ext.spring.template.Template;
import org.springframework.stereotype.Component;
import static org.rendersnake.HtmlAttributesFactory.*;

@Component
@Template(DefaultTemplate.class)
public class DonationResultPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1()
                .write("Donation Result")
                ._h1();
        html.p().content("We received your dontion of $100 dollars");
    }
}