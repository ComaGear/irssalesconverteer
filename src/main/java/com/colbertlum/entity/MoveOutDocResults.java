package com.colbertlum.entity;

import java.util.List;

public class MoveOutDocResults {

    private List<Doc> docList;
    private List<MoveOut> moveOuts;
    public List<Doc> getDocList() {
        return docList;
    }
    public void setDocList(List<Doc> docList) {
        this.docList = docList;
    }
    public List<MoveOut> getMoveOuts() {
        return moveOuts;
    }
    public void setMoveOuts(List<MoveOut> moveOuts) {
        this.moveOuts = moveOuts;
    }
    public MoveOutDocResults(List<Doc> docList, List<MoveOut> moveOuts) {
        this.docList = docList;
        this.moveOuts = moveOuts;
    }
}

