package hederahack;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class HederaService {

    @Autowired Client client;

    public AccountId createAccount(long initialBalance) throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        // Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generate();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

        //Create new account and assign the public key
        TransactionResponse newAccount = new AccountCreateTransaction()
                .setKey(newAccountPublicKey)
                .setInitialBalance( Hbar.fromTinybars(initialBalance))
                .execute(client);

        return newAccount.getReceipt(client).accountId;

    }

    public long makeDonation(AccountId myAccountId, AccountId newAccountId, long amount) throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        //Transfer hbar
        TransactionResponse sendHbar = new TransferTransaction()
                .addHbarTransfer(myAccountId, Hbar.fromTinybars(-1 * amount)) //Sending account
                .addHbarTransfer(newAccountId, Hbar.fromTinybars(amount)) //Receiving account
                .execute(client);

        System.out.println("The transfer transaction was: " +sendHbar.getReceipt(client).status);

        //Request the cost of the query
        Hbar queryCost = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .getCost(client);

        System.out.println("The cost of this query is: " +queryCost);

        AccountBalance myAccountBalance = new AccountBalanceQuery()
                .setAccountId(myAccountId)
                .execute(client);

        System.out.println("The new account balance is: " +myAccountBalance.hbars);
        return myAccountBalance.hbars.toTinybars();
    }
}
