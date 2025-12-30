package com.colbertlum.Utils;

import com.colbertlum.entity.BiztoryOutputResult;
import com.colbertlum.entity.DocSalesConverterResult;

public class BiztoryOutputConverter {
    public static BiztoryOutputResult converting(DocSalesConverterResult docResults){
        docResults.getCashDocGroupByDate();
    }
}
