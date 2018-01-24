package com.moocall.moocall.db;

public class StateHistoryDb {
    private String animal_id;
    private String datetime;
    private Long id;
    private Integer last_state;
    private String last_state_datetime;
    private String weight;

    public StateHistoryDb(Long id) {
        this.id = id;
    }

    public StateHistoryDb(Long id, String animal_id, Integer last_state, String last_state_datetime, String weight, String datetime) {
        this.id = id;
        this.animal_id = animal_id;
        this.last_state = last_state;
        this.last_state_datetime = last_state_datetime;
        this.weight = weight;
        this.datetime = datetime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnimal_id() {
        return this.animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public Integer getLast_state() {
        return this.last_state;
    }

    public void setLast_state(Integer last_state) {
        this.last_state = last_state;
    }

    public String getLast_state_datetime() {
        return this.last_state_datetime;
    }

    public void setLast_state_datetime(String last_state_datetime) {
        this.last_state_datetime = last_state_datetime;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
