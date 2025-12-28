package com.colbertlum.entity;

import java.time.LocalDate;
import java.util.List;

public class Doc {
    
    private String id;
    private LocalDate date;
    private List<MoveOut> moveOuts;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<MoveOut> getMoveOuts() {
        return moveOuts;
    }
    public void setMoveOuts(List<MoveOut> moveOuts) {
        this.moveOuts = moveOuts;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    
}
