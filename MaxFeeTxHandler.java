import java.util.ArrayList;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Collections;

public class MaxFeeTxHandler implements Comparator<Transaction> {

    private UTXOPool pool;

    /** Same with TxHandler class */
    public MaxFeeTxHandler(UTXOPool utxoPool) {
        pool = new UTXOPool(utxoPool);
    }

    /** Same with TxHandler class */
    public boolean isValidTx(Transaction tx) {

        int index = 0;
        double sum_Inputs = 0, sum_OutputsByInput = 0;
        ArrayList<Transaction.Input> txInputs = tx.getInputs();
        ArrayList<Transaction.Output> txOutputs = tx.getOutputs();
        HashSet<UTXO> usedUTXOs = new HashSet<>();

        for (Transaction.Input i : txInputs) {

            UTXO targetUTXO = new UTXO(i.prevTxHash, i.outputIndex);

            if (!pool.contains(targetUTXO))
                return false;

            Transaction.Output prevO = pool.getTxOutput(targetUTXO);
            if (!prevO.address.verifySignature(tx.getRawDataToSign(index++), i.signature))
                return false;

            usedUTXOs.add(targetUTXO);

            sum_Inputs += prevO.value;

        }

        if (tx.getInputs().size() != usedUTXOs.size())
            return false;

        for (Transaction.Output o : txOutputs) {

            if (o.value < 0)
                return false;

            sum_OutputsByInput += o.value;

        }

        if (sum_Inputs < sum_OutputsByInput)
            return false;

        return true;

    }

    /**
     * Same with TxHandler class, but finds a set of txs whose fee is maximized.
     * But how? The only possible way is "same, but different fee" txs.
     * So, by sorting {@code possibleTxs}, tried to find the answer.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {

        // sorting {@code possibleTxs} to find a set of txs whose fee is maximized.
        //Arrays.sort(possibleTxs, new MaxFeeTxHandler(pool));
        TreeSet<Transaction> possibleTxsSet = new TreeSet<>((Transaction a, Transaction b) -> {

            double aFee = getTotalTxInputValue(a) - getTotalTxOutputValue(a);
            double bFee = getTotalTxInputValue(b) - getTotalTxOutputValue(b);

            return bFee - aFee > 0 ? +1 : bFee == aFee ? 0 : -1;

        });//(Arrays.asList(possibleTxs));
        Collections.addAll(possibleTxsSet, possibleTxs);

        // from below, the code is same with TxHandler class
        HashSet<Transaction> acceptedTxs = new HashSet<>();

        for (Transaction tx : possibleTxsSet)
            if (isValidTx(tx)) {

                acceptedTxs.add(tx);

                for (Transaction.Input i : tx.getInputs())
                    pool.removeUTXO(new UTXO(i.prevTxHash, i.outputIndex));
                int index = 0;
                for (Transaction.Output o : tx.getOutputs())
                    pool.addUTXO(new UTXO(tx.getHash(), index++), o);

            }

        return acceptedTxs.toArray(new Transaction[acceptedTxs.size()]);

    }

    private double getTotalTxInputValue(Transaction tx) {

        double total = 0;

        for (Transaction.Input i : tx.getInputs()) {

            UTXO targetUTXO = new UTXO(i.prevTxHash, i.outputIndex);

            if (pool.contains(targetUTXO) && isValidTx(tx))
                total += pool.getTxOutput(targetUTXO).value;

        }

        return total;

    }

    private double getTotalTxOutputValue(Transaction tx) {

        double total = 0;

        for (Transaction.Output o : tx.getOutputs())
            //if (isValidTx(tx))
                total += o.value;

        return total;

    }

    @Override
    public int compare(Transaction a, Transaction b) {

        double aFee = getTotalTxInputValue(a) - getTotalTxOutputValue(a);
        double bFee = getTotalTxInputValue(b) - getTotalTxOutputValue(b);

        return bFee - aFee > 0 ? +1 : bFee == aFee ? 0 : -1;

    }

}
