package com.lisa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple on 16/3/10.
 */
public class Bean_business_group implements Serializable {
    private String MC;
    private String BM;
    private List<Bean_business_child> son;

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getBM() {
        return BM;
    }

    public void setBM(String BM) {
        this.BM = BM;
    }

    public List<Bean_business_child> getSon() {
        return son;
    }

    public void setSon(List<Bean_business_child> son) {
        this.son = son;
    }
}
