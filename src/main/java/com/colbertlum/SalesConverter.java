package com.colbertlum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.UOM;
import com.colbertlum.entity.UnsableItem;

public class SalesConverter {

    private List<MoveOut> unfoundMoveOuts;

    List<MoveOut> moveOuts;

    List<UOM> uoms;

    public List<MoveOut> getUnfoundMoveOuts() {
        return unfoundMoveOuts;
    }

    public SalesConverter() {
        this.unfoundMoveOuts = new ArrayList<MoveOut>();
    }

    public void convert(List<MoveOut> moveOuts) {

        this.moveOuts = moveOuts;

        moveOuts.sort(new Comparator<MoveOut>() {

            @Override
            public int compare(MoveOut o1, MoveOut o2) {

                if (o1.getProductId().compareTo(o2.getProductId()) == 0) {
                    return o1.getUom().compareTo(o2.getUom());
                }
                return o1.getProductId().compareTo(o2.getProductId());
            }

        });

        uoms.sort(new Comparator<UOM>() {

            @Override
            public int compare(UOM o1, UOM o2) {
                if (o1.getProductId().compareTo(o2.getProductId()) == 0) {
                    return o1.getUom().compareTo(o2.getUom());
                }
                return o1.getProductId().compareTo(o2.getProductId());
            }

        });
    
        UOM uom = null;
        for (MoveOut moveOut : moveOuts) {

            if (uom != null && moveOut.getProductId().equals(uom.getProductId())
                    && moveOut.getUom().equals(uom.getUom())) {
                Double quantity = moveOut.getQuantity();
                moveOut.setUom("");
                moveOut.setProductName(uom.getProductName());
                moveOut.setQuantity(quantity * uom.getRate());

                continue;
            }

            uom = rank(uoms, moveOut);
            if (uom == null) {
                unfoundMoveOuts.add(moveOut);
                continue;
            }

            Double quantity = moveOut.getQuantity();
            moveOut.setUom("");
            moveOut.setProductName(uom.getProductName());
            moveOut.setQuantity(quantity * uom.getRate());

        }

    }

    private static UOM rank(List<UOM> uoms, MoveOut moveOut) {

        int lo = 0;
        int hi = uoms.size()-1;

        while(lo <= hi) {
            int mid = lo + (hi-lo) / 2;
            UOM uom = uoms.get(mid);
            int compareTo = uom.getProductId().compareTo(moveOut.getProductId());
            if(compareTo > 0) hi = mid-1; 
            else if(compareTo < 0) lo = mid+1;
            else{
                if(uom.getUom().compareTo(moveOut.getUom()) > 0) hi = mid-1;
                else if(uom.getUom().compareTo(moveOut.getUom()) < 0) lo = mid+1;
                else return uom;
            }
        }
        return null;
    }

    private static MoveOut rank(List<MoveOut> moveOuts, String id) {

        int lo = 0;
        int hi = moveOuts.size()-1;

        while(lo <= hi) {
            int mid = lo + (hi-lo) / 2;
            MoveOut moveOut = moveOuts.get(mid);
            int compareTo = moveOut.getProductId().compareTo(id);
            if(compareTo > 0) hi = mid-1; 
            else if(compareTo < 0) lo = mid+1;
            else{
                return moveOut;
            }
        }
        return null;
    }

    public List<MoveOut> getResult() {
        return moveOuts;
    }

    public ArrayList<MoveOut> premapping(ArrayList<MoveOut> preMoveOuts) {

        UnUsableItemMapper unUsableItemMapper = new UnUsableItemMapper();

        ArrayList<MoveOut> toRemove = new ArrayList<MoveOut>();
        ArrayList<MoveOut> toAdd = new ArrayList<MoveOut>();
        for(MoveOut moveOut : preMoveOuts){
            List<UnsableItem> foundItems = unUsableItemMapper.findItems(moveOut.getProductId());

            if(foundItems == null) continue;

            for(UnsableItem unsableItem : foundItems){
                MoveOut newMoveOut = new MoveOut();
                newMoveOut.setProductId(unsableItem.getToUseId());
                newMoveOut.setProductName(moveOut.getProductName());
                newMoveOut.setQuantity(moveOut.getQuantity() * unsableItem.getMeasurement());
                if(unsableItem.getUnitPrice() > 0){
                    double pricePercent = unsableItem.getUnitPrice() / unsableItem.getBundlePrice();
                    newMoveOut.setTotalAmount(moveOut.getTotalAmount() * pricePercent);
                } else {
                    newMoveOut.setTotalAmount(moveOut.getTotalAmount());
                }
                newMoveOut.setUom(moveOut.getUom());

                if(unsableItem.isReduceOriginUnitQuantity()){
                    MoveOut rankMoveOut = rank(preMoveOuts, unsableItem.getToUseId());
                    rankMoveOut.setQuantity(rankMoveOut.getQuantity() - newMoveOut.getQuantity());
                }

                toAdd.add(newMoveOut);
                toRemove.add(moveOut);
            }
        }
        preMoveOuts.removeAll(toRemove);
        preMoveOuts.addAll(toAdd);

        // preMoveOuts.sort(new Comparator<MoveOut>() {

        //     @Override
        //     public int compare(MoveOut o1, MoveOut o2) {
        //         o1
        //     }
        
        // });
        return preMoveOuts;
    }

    public void setUOMs(List<UOM> uom) {
        this.uoms = uom;
    }
}
