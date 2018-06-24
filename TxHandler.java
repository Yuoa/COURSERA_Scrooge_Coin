import java.util.ArrayList;
import java.util.HashSet;

public class TxHandler {

    private UTXOPool pool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        pool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {

        int index = 0;
        double sum_Inputs = 0, sum_OutputsByInput = 0;
        ArrayList<Transaction.Input> txInputs = tx.getInputs();
        ArrayList<Transaction.Output> txOutputs = tx.getOutputs();
        HashSet<UTXO> usedUTXOs = new HashSet<>();

        for (Transaction.Input i : txInputs) {

            UTXO targetUTXO = new UTXO(i.prevTxHash, i.outputIndex);

            // (1) all outputs claimed by {@code tx} are in the current UTXO pool
            if (!pool.contains(targetUTXO))
                return false;

            // (2) the signatures on each input of {@code tx} are valid
            Transaction.Output prevO = pool.getTxOutput(targetUTXO);
            if (!Crypto.verifySignature(prevO.address, tx.getRawDataToSign(index++), i.signature))
                return false;

            usedUTXOs.add(targetUTXO); // for (3)

            sum_Inputs += prevO.value; // for (5)

        }

        // (3) no UTXO is claimed multiple times by {@code tx}
        if (tx.getInputs().size() != usedUTXOs.size())
            return false;

        for (Transaction.Output o : txOutputs) {

            // (4) all of {@code tx}s output values are non-negative
            if (o.value < 0)
                return false;

            sum_OutputsByInput += o.value; // for (5)

        }

        // (5) the sum of {@code tx}s input values is greater than or equal to the sum of its
        //     output values
        if (sum_Inputs < sum_OutputsByInput)
            return false;

        return true;

    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {

        HashSet<Transaction> acceptedTxs = new HashSet<>();

        // checking each transaction for correctness
        for (Transaction tx : possibleTxs)
            if (isValidTx(tx)) {

                acceptedTxs.add(tx);

                // updating the current UTXO pool as appropriate
                for (Transaction.Input i : tx.getInputs())
                    pool.removeUTXO(new UTXO(i.prevTxHash, i.outputIndex));
                int index = 0;
                for (Transaction.Output o : tx.getOutputs())
                    pool.addUTXO(new UTXO(tx.getHash(), index++), o);

            }

        // returning a mutually valid array of accepted transactions
        return acceptedTxs.toArray(new Transaction[acceptedTxs.size()]);

    }

}
