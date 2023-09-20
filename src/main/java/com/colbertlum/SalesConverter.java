package com.colbertlum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.UOM;

public class SalesConverter {

    private List<MoveOut> unfoundMoveOuts;

    ArrayList<MoveOut> moveOuts;

    public List<MoveOut> getUnfoundMoveOuts() {
        return unfoundMoveOuts;
    }

    public SalesConverter() {
        this.unfoundMoveOuts = new ArrayList<MoveOut>();
    }

    public void convert(ArrayList<MoveOut> moveOuts, ArrayList<UOM> uoms) {

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
            moveOut.setQuantity(quantity * uom.getRate());

        }

    }

    private static UOM rank(List<UOM> uoms, MoveOut moveOut) {

        int lo = 0;
        int hi = uoms.size()-1;

        while(lo <= hi) {
            int mid = lo + (hi-lo) / 2;
            if(uoms.get(mid).getProductId().compareTo(moveOut.getProductId()) == -1) hi = mid-1; 
            else if(uoms.get(mid).getProductId().compareTo(moveOut.getProductId()) == 1) lo = mid+1;
            else{
                if(uoms.get(mid).getUom().compareTo(moveOut.getUom()) == -1) hi = mid-1;
                else if(uoms.get(mid).getUom().compareTo(moveOut.getUom()) == -1) lo = mid+1;
                else return uoms.get(mid);
            }
        }
        return null;
    }

    public ArrayList<MoveOut> getResult() {
        return moveOuts;
    }
}
