package ai.bitflow.bitwise.wallet.converters;

import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.gsonObjects.objects.Transaction;

import java.util.ArrayList;
import java.util.List;


public class TbTransListToTransList {

    public static List<Transaction> convert(List<TbTrans> items) {
        List<Transaction> ret = new ArrayList<>();
        for (TbTrans item : items) {
            ret.add(new Transaction(item));
        }
        return ret;
    }

}
