package hederahack;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaPreCheckStatusException;
import com.hedera.hashgraph.sdk.HederaReceiptStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class HederaService {
    private static final Logger logger = LoggerFactory.getLogger(HederaService.class);


    public Client createClient() {
        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));

        //Create your Hedera testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);
        return client;
    }

    public AccountId createAccount(long initialBalance) throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        // Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generate();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

        Client client = createClient();

        //Create new account and assign the public key
        TransactionResponse newAccount = new AccountCreateTransaction()
                .setKey(newAccountPublicKey)
                .setInitialBalance( Hbar.fromTinybars(initialBalance))
                .execute(client);

        AccountId accountId =  newAccount.getReceipt(client).accountId;
        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(accountId)
                .execute(client);

        logger.info("Account created, initial balance is: " + accountBalance.hbars.getValue().longValue());
        return accountId;

    }

    public long makeDonation(AccountId myAccountId, AccountId newAccountId, long amount) throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {

        Client client = createClient();

        //Transfer hbar
        TransactionResponse sendHbar = new TransferTransaction()
                .addHbarTransfer(myAccountId, Hbar.fromTinybars(-1 * amount)) //Sending account
                .addHbarTransfer(newAccountId, Hbar.fromTinybars(amount)) //Receiving account
                .execute(client);

        logger.info("The transfer transaction status is: " +sendHbar.getReceipt(client).status);

        //Request the cost of the query
        Hbar queryCost = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .getCost(client);

        logger.info("The cost of this query is: " +queryCost);

        AccountBalance myAccountBalance = new AccountBalanceQuery()
                .setAccountId(myAccountId)
                .execute(client);

        logger.info("The new account balance is: " +myAccountBalance.hbars.toString());
        return myAccountBalance.hbars.toTinybars();
    }
}
