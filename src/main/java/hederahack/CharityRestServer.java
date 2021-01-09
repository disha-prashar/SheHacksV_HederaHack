package hederahack;

import java.io.IOException;
import java.util.Map;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hederahack.pages.DonationFormPage;
import hederahack.pages.DonationResultPage;
import hederahack.pages.LoginPage;

@Controller
public class CharityRestServer {
    private static final Logger logger = LoggerFactory.getLogger(CharityRestServer.class);

    static private String page(Class<?> clasz) {
        String className = clasz.getSimpleName();
        char c[] = className.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    @RequestMapping("/")
    public String index() throws IOException {
        logger.info("Start rendering the index page");
        return page(LoginPage.class);
    }

    @RequestMapping(value = "/donate", method = RequestMethod.GET)
    public String donateForm(Map<String,String> fields, HtmlCanvas html) {
        return page(DonationFormPage.class);
    }


    @RequestMapping(value = "/donate", method = RequestMethod.POST)
    public String donateProcessing(Map<String,String> fields, HtmlCanvas html) {
        return page(DonationResultPage.class);
    }

}