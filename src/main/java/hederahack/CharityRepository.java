package hederahack;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.hedera.hashgraph.sdk.AccountId;

@Component
public class CharityRepository {

    ConcurrentHashMap<String, AccountId> charities = new ConcurrentHashMap<>();

    public void add(String charityId, AccountId charityAccountId) {
        charities.put(charityId, charityAccountId);
    }

    public AccountId get(String charityId) {
        return charities.get(charityId);
    }

}
