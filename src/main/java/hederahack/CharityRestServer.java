package hederahack;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharityRestServer {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Charity Rest Server!";
    }


    @RequestMapping(value = "/donate", method = RequestMethod.POST)
    public String donate() {
        return "ABC";
    }

}