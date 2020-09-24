package com.alaya.abi.datatypes.generated;

import java.util.List;
import com.alaya.abi.datatypes.StaticArray;
import com.alaya.abi.datatypes.Type;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray3<T extends Type> extends StaticArray<T> {
    public StaticArray3(List<T> values) {
        super(3, values);
    }

    public StaticArray3(T... values) {
        super(3, values);
    }
}
