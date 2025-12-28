package com.colbertlum;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.colbertlum.entity.Doc;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.MoveOutDocResultGroupByDate;
import com.colbertlum.entity.MoveOutDocResults;

public class DocSalesConverter {

    List<String> toExcludeDocList;
    
    public void process(File targetFile){
        MoveOutDocResults moveOutsWithDoc = MoveOutUtils.loadMoveOutsWithDoc(targetFile);

        List<String> excludeDoc = loadExcludeDoc();
        
        SalesConverter salesConverter = loadSalesConverter();
        
        loopDocAndPremapping(moveOutsWithDoc.getDocList());
        loopDocAndConverting(moveOutsWithDoc.getDocList(), salesConverter);

        List<Doc> cashDocList = findCashDocFromExclude(moveOutsWithDoc.getDocList(), excludeDoc);

        splitDocListByDate(cashDocList);
    }

    private MoveOutDocResultGroupByDate splitDocListByDate(List<Doc> cashDocList) {
        MoveOutDocResultGroupByDate groupByDate = new MoveOutDocResultGroupByDate();
        Map<LocalDate, List<Doc>> map = new HashMap<LocalDate, List<Doc>>();
        Set<LocalDate> dates = map.keySet();
        for(Doc doc : cashDocList){
            LocalDate date = doc.getDate();
            if(dates.contains(date)) {
                map.get(date).add(doc);
            } else {
                ArrayList<Doc> list = new ArrayList<Doc>();
                list.add(doc);
                map.put(date, list);
            }
        }

        for(LocalDate date : dates){
            List<Doc> list = map.get(date);
            List<MoveOut> moveOuts = new ArrayList<MoveOut>();
            for(Doc doc : list) {
                moveOuts.addAll(doc.getMoveOuts());
            }
            MoveOutDocResults moveOutDocResults = new MoveOutDocResults(list, moveOuts);
            groupByDate.putMoveOutDocResultByLocalDate(date, moveOutDocResults);
            
        }

        return groupByDate;
    }

    private SalesConverter loadSalesConverter() {
        SalesConverter salesConverter = new SalesConverter();
        salesConverter.setUOMs(IrsSalesConverterApplication.getContext().getUom());
        return salesConverter;
    }

    private void loopDocAndPremapping(List<Doc> docList) {
        for(Doc doc : docList) {
            MoveOutUtils.premapping(doc.getMoveOuts());
        }
    }

    private void loopDocAndConverting(List<Doc> docList, SalesConverter salesConverter){
        for(Doc doc : docList) {
            salesConverter.convert(doc.getMoveOuts());
        }
    }

    private List<String> loadExcludeDoc() {
        return toExcludeDocList;
    }

    public static List<Doc> findCashDocFromExclude(List<Doc> docList, List<String> excludeDoc) {
        return docList.stream()
                .filter(doc -> !excludeDoc.contains(doc.getId()))
                .collect(Collectors.toList());
    }

    public void setToExcludeDocList(List<String> excludeDoc) {
        this.toExcludeDocList = excludeDoc;
    }

}
