package hederahack;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaPreCheckStatusException;
import com.hedera.hashgraph.sdk.HederaReceiptStatusException;

import hederahack.pages.DonationFormPage;
import hederahack.pages.DonationResultPage;
import hederahack.pages.LoginPage;
import hederahack.pages.PageTemplate;
import io.github.cdimascio.dotenv.Dotenv;

@Controller
public class CharityRestServer {
    private static final Logger logger = LoggerFactory.getLogger(CharityRestServer.class);

    @Autowired
    HederaService hederaService;
    @Autowired
    CharityRepository repository;

    @RequestMapping("/")
    public ResponseEntity<?> index() throws IOException {

        HtmlCanvas html = new HtmlCanvas();

        Renderable loginPage = new PageTemplate(new LoginPage());
        html.render(loginPage);

        return ResponseEntity.ok(html.toHtml());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String loginProcess(@RequestBody MultiValueMap<String, String> fields, HttpSession session)
            throws IOException, TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        session.setAttribute("userId", fields.getFirst("user"));
        logger.info("Sesion initialized");

        return "redirect:/donate";
    }

    @RequestMapping(value = "/donate", method = RequestMethod.GET)
    public ResponseEntity<?> donateForm(Map<String, String> fields) throws IOException {

        HtmlCanvas html = new HtmlCanvas();

        Renderable donateForm = new PageTemplate(new DonationFormPage());
        html.render(donateForm);

        return ResponseEntity.ok(html.toHtml());
    }

    public AccountId getAccountId(String charityId)
            throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        AccountId charityAccountId = repository.get(charityId);
        if (charityAccountId == null) {
            charityAccountId = hederaService.createAccount(0);
            repository.add(charityId, charityAccountId);
        }
        return charityAccountId;
    }

    @RequestMapping(value = "/donate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<?> donateProcessing(@RequestBody MultiValueMap<String, String> fields,
                                              HttpSession session)
            throws IOException, TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        logger.info("Received {} fields", fields.size());
        for (String key : fields.keySet()) {
            logger.info("{} -> {}", key, fields.get(key));
        }

        String userId = (String) session.getAttribute("userId");


        Client client = hederaService.createClient();
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        long amount = Long.parseLong(fields.getFirst("amount"));

        String charityId = fields.getFirst("charity-id");
        AccountId newAccountId = getAccountId(charityId);
        System.out.println("The new account ID is: " + newAccountId);

        hederaService.makeDonation(myAccountId, newAccountId, amount);

        //Request the cost of the query
        Hbar queryCost = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .getCost(client);

        System.out.println("The cost of this query is: " + queryCost);

        AccountBalance accountBalanceNew = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);

        System.out.println("The new account balance is: " + accountBalanceNew.hbars);

        AccountBalance myAccountBalance = new AccountBalanceQuery()
                .setAccountId(myAccountId)
                .execute(client);

        System.out.println("My account balance is: " + myAccountBalance.hbars);


        HtmlCanvas html = new HtmlCanvas();

        Renderable donateResults = new PageTemplate(
                new DonationResultPage(userId, "b", amount, myAccountBalance.hbars.toString()));
        html.render(donateResults);

        return ResponseEntity.ok(html.toHtml());
    }
}
