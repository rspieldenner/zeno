package com.netflix.zeno.fastblob.lazy.hollow;

import com.netflix.zeno.fastblob.lazy.LazyStateEngine;
import com.netflix.zeno.fastblob.record.ByteData;
import com.netflix.zeno.fastblob.record.VarInt;

public abstract class HollowCollection extends HollowContainer {

    protected String elementType;

    protected void position(LazyStateEngine stateEngine, String elementType, ByteData data, long position) {
        this.stateEngine = stateEngine;
        this.elementType = elementType;
        this.data = data;
        this.position = position;
    }

    public boolean contains(int ordinal) {
        HollowOrdinalIterator iter = ordinalIterator();
        int nextOrdinal = iter.next();
        while(nextOrdinal != HollowOrdinalIterator.NO_MORE_ORDINALS) {
            if(ordinal == nextOrdinal)
                return true;
            nextOrdinal = iter.next();
        }
        return false;
    }

    public int size() {
        int numBytes = VarInt.readVInt(data, position);
        long position = this.position + VarInt.sizeOfVInt(numBytes);

        return VarInt.countVarIntsInRange(data, position, numBytes);
    }

    public abstract HollowOrdinalIterator ordinalIterator();
}
