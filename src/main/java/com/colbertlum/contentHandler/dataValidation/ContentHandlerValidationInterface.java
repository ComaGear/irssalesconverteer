package com.colbertlum.contentHandler.dataValidation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContentHandlerValidationInterface<T> {

    boolean supports(ExcelSchema schema);

    public List<String> shouldHaveColumns();

    public ExpectedDataMap<T> shouldContainedExpectedDataForTheseColumns();

    /** 
    * this method do data verify about table column. 
    * this will call subclass implemented ContentHandlerValidationInterface.class's registered
    * column list, to verify data source should have registered columns
    */
    default void validateHeader(Map<String, String> headers) {
        Set<String> present = (Set<String>) headers.values();

        for (String required : shouldHaveColumns()) {
            if (!present.contains(required)) {
                throw new IllegalStateException(
                    "Missing required column: " + required
                );
            }
        }
    }
    
    default void validateCell(String column, int row, String value) {
        if(shouldContainedExpectedDataForTheseColumns().hasExpectDataSetForColumn(column)){
            List<T> expectDataSetForColumn = shouldContainedExpectedDataForTheseColumns().getExpectDataSetForColumn(column);

            if(!expectDataSetForColumn.contains(value)) 
                throw new IllegalStateException("Contained unexpect data : '" + value +"' in column of :" + column);
        }
    }

}
