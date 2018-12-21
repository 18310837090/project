package com.ncit.module_02.UC02_01.model;

public class UC02_01_SetParameterInput {

    private String generation_max = "";
    private String population_size = "";
    private String tournament_size = "";
    private String sim_duration = "";
    private String goal = "";

    public String getGeneration_max() {
        return generation_max;
    }

    public void setGeneration_max(String _generation_max) {
        this.generation_max = _generation_max;
    }

    public String getPopulation_size() {
        return population_size;
    }

    public void setPopulation_size(String _population_size) {
        this.population_size = _population_size;
    }

    public String getTournament_size() {
        return tournament_size;
    }

    public void setTournament_size(String _tournament_size) {
        this.tournament_size = _tournament_size;
    }

    public String getSim_duration() {
        return sim_duration;
    }

    public void setSim_duration(String _sim_duration) {
        this.sim_duration = _sim_duration;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String _goal) {
        this.goal = _goal;
    }
}
