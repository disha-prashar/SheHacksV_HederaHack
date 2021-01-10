package hederahack;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.HederaPreCheckStatusException;
import com.hedera.hashgraph.sdk.HederaReceiptStatusException;

import hederahack.pages.DonationFormPage;
import hederahack.pages.DonationResultPage;
import hederahack.pages.LoginPage;
import hederahack.pages.PageTemplate;

@Controller
public class CharityRestServer {
    private static final Logger logger = LoggerFactory.getLogger(CharityRestServer.class);

    @Autowired HederaService hederaService;


    @RequestMapping("/")
    public ResponseEntity<?> index() throws IOException {

        HtmlCanvas html = new HtmlCanvas();

        Renderable loginPage = new PageTemplate(new LoginPage());
        html.render(loginPage);

        return ResponseEntity.ok(html.toHtml());
    }


    @RequestMapping(value = "/", method=RequestMethod.POST)
    public String loginProcess(HttpServletRequest request) throws IOException, TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        HttpSession session = request.getSession(true);
        AccountId myAccountId = (AccountId) session.getAttribute("accountId");
        if (myAccountId == null) {
            myAccountId = hederaService.createAccount(1000);
        }
        return "redirect:/donate";
    }

    @RequestMapping(value = "/donate", method = RequestMethod.GET)
    public ResponseEntity<?> donateForm(Map<String, String> fields) throws IOException {

        HtmlCanvas html = new HtmlCanvas();

        Renderable donateForm = new PageTemplate(new DonationFormPage());
        html.render(donateForm);

        return ResponseEntity.ok(html.toHtml());
    }

    @RequestMapping(value = "/donate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<?> donateProcessing(@RequestBody MultiValueMap<String, String> fields) throws IOException {
        logger.info("Received {} fields", fields.size());
        for (String key : fields.keySet()) {
            logger.info("{} -> {}", key, fields.get(key));
        }

        String userId = fields.getFirst("user-id");
        long amount = Long.parseLong(fields.getFirst("amount"));
        String charityId = fields.getFirst("charity-id");

        long balance = 200 - amount;

        HtmlCanvas html = new HtmlCanvas();

        Renderable donateResults = new PageTemplate(new DonationResultPage(userId, amount, charityId, balance));
        html.render(donateResults);

        return ResponseEntity.ok(html.toHtml());
    }
}
