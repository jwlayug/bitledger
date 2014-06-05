package spc;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class InMemoryLedger implements Ledger {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void add(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
