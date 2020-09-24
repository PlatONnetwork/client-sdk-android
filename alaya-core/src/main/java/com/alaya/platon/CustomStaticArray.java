package com.alaya.platon;

import com.alaya.abi.datatypes.StaticArray;
import com.alaya.rlp.RlpEncoder;
import com.alaya.rlp.RlpList;
import com.alaya.rlp.RlpString;
import com.alaya.rlp.RlpType;

import java.util.ArrayList;
import java.util.List;

public class CustomStaticArray<T extends CustomType> extends StaticArray<T> {

    public CustomStaticArray(List<T> values) {
        super(values);
    }

    public RlpType getRlpEncodeData() {

        List<T> list = getValue();
        if (list != null && !list.isEmpty()) {
            List<RlpType> rlpListList = new ArrayList<>();
            for (T t : list) {
                rlpListList.add(t.getRlpEncodeData());
            }
            return RlpString.create(RlpEncoder.encode(new RlpList(rlpListList)));
        }
        throw new RuntimeException("unsupported types");
    }
}
