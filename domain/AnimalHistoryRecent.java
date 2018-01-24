package com.moocall.moocall.domain;

public class AnimalHistoryRecent {
    private AnimalHistory animalHistory;
    private CowHeat cowHeat;
    private String date;
    private Boolean first;
    private Boolean header;
    private Boolean last;

    public AnimalHistoryRecent(String date, AnimalHistory animalHistory) {
        setHeader(Boolean.valueOf(false));
        setDate(date);
        setAnimalHistory(animalHistory);
        setFirst(Boolean.valueOf(false));
        setLast(Boolean.valueOf(false));
    }

    public AnimalHistoryRecent(String date) {
        setHeader(Boolean.valueOf(true));
        setDate(date);
    }

    public AnimalHistoryRecent(String date, CowHeat cowHeat) {
        setHeader(Boolean.valueOf(false));
        setDate(date);
        setCowHeat(cowHeat);
    }

    public Boolean getHeader() {
        return this.header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AnimalHistory getAnimalHistory() {
        return this.animalHistory;
    }

    public void setAnimalHistory(AnimalHistory animalHistory) {
        this.animalHistory = animalHistory;
    }

    public CowHeat getCowHeat() {
        return this.cowHeat;
    }

    public void setCowHeat(CowHeat cowHeat) {
        this.cowHeat = cowHeat;
    }

    public Boolean getFirst() {
        return this.first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return this.last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
