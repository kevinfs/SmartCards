package com.iris.model;

public class Histo {

    private Long id;
    private String login;
    private String mdp;
    private String sel;
    private String histo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getSel() {
        return sel;
    }

    public void setSel(String sel) {
        this.sel = sel;
    }

    public String getHisto() {
        return histo;
    }

    public void setHisto(String histo) {
        this.histo = histo;
    }
}
