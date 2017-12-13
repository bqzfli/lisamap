package com.lisa.bean;

import java.io.Serializable;

/**
 * Created by apple on 16/3/10.
 */
public class Bean_business_child implements Serializable {
    private String BM;
    private String MC;
    private boolean renderShow;
    public String getBM() {
        return BM;
    }

    public void setBM(String BM) {
        this.BM = BM;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public boolean isRenderShow() {
        return renderShow;
    }

    public void setRenderShow(boolean render) {
        this.renderShow = render;
    }
}
