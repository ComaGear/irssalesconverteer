package com.colbertlum.Utils;

import java.util.List;
import com.colbertlum.entity.UOM;

public class UOMUtils {
    
    // this method was used for autoCount moveOut it determine which has mutli uom?
    // if had, prodouct id have to add uom into ({uom})
    public static final boolean hasMutliUom(List<UOM> uomList, String productId) {
        return uomList.stream().filter((e) -> {
            return e.getProductId().equals(productId);
        }).count() > 1;
    }
}
