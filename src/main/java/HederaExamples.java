import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import io.github.cdimascio.dotenv.Dotenv;
import com.hedera.hashgraph.sdk.HederaPreCheckStatusException;
import com.hedera.hashgraph.sdk.HederaReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.TransferTransaction;

import java.util.concurrent.TimeoutException;

public class HederaExamples {

    public static void main(String[] args) throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {

        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));

        //Create your Hedera testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);

        //Create your Hedera testnet client
        //Client client = Client.forTestnet();
        //client.setOperator(myAccountId, myPrivateKey)
        //-----------------------<enter code below>--------------------------------------

        // Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generate();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

        //Create new account and assign the public key
        TransactionResponse newAccount = new AccountCreateTransaction()
                .setKey(newAccountPublicKey)
                .setInitialBalance( Hbar.fromTinybars(1000))
                .execute(client);

        // Get the new account ID
        AccountId newAccountId = newAccount.getReceipt(client).accountId;

        System.out.println("The new account ID is: " +newAccountId);

        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);

        System.out.println("The new account balance is: " +accountBalance);

        //System.out.println("The new account balance is: " +accountBalance.hbars);
        //-----------------------<enter code below>--------------------------------------

        //Transfer hbar
        TransactionResponse sendHbar = new TransferTransaction()
                .addHbarTransfer(myAccountId, Hbar.fromTinybars(-1000)) //Sending account
                .addHbarTransfer(newAccountId, Hbar.fromTinybars(1000)) //Receiving account
                .execute(client);

        System.out.println("The transfer transaction was: " +sendHbar.getReceipt(client).status);

        //Request the cost of the query
        Hbar queryCost = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .getCost(client);

        System.out.println("The cost of this query is: " +queryCost);

        AccountBalance accountBalanceNew = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);

        System.out.println("The new account balance is: " +accountBalanceNew.hbars);
    }
    // returns the remaining account balance after transferring money
    public AccountBalance donate(AccountId userId, AccountId charityId, int amount, Client testNetClient) throws TimeoutException, HederaPreCheckStatusException{
        //Transfer hbar
        TransactionResponse sendHbar = new TransferTransaction()
                .addHbarTransfer(userId, Hbar.fromTinybars(amount)) //Sending account
                .addHbarTransfer(charityId, Hbar.fromTinybars(amount)) //Receiving account
                .execute(testNetClient);

        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(userId)
                .execute(testNetClient);

        return accountBalance;
    }
    // creates an account for user
    public void createAccount(){

        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));

        //Create your Hedera testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);
    }

    public AccountBalance viewAccountBalance(Client testNetClient, AccountId userId) throws TimeoutException, HederaPreCheckStatusException{

        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(userId)
                .execute(testNetClient);

        return accountBalance;
    }




}

