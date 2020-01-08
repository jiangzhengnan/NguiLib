package com.ng.ui.other.gesture;

public class ZHECGOffLineEcg implements java.io.Serializable {

    private Long id;
    private String mid;
    private String bleMac;
    private String date;
    private String time;
    private String dateTime;
    private String ecgDataArr;
    private String ppgDataArr;
    private Integer ecgHR;
    private Integer ecgSBP;
    private Integer ecgDBP;
    private Integer healthHrvIndex;
    private Integer healthFatigueIndex;
    private Integer healthLoadIndex;
    private Integer healthBodyIndex;
    private Integer healtHeartIndex;

    public ZHECGOffLineEcg() {
    }

    public ZHECGOffLineEcg(Long id) {
        this.id = id;
    }

    public ZHECGOffLineEcg(Long id, String mid, String bleMac, String date, String time, String dateTime, String ecgDataArr, String ppgDataArr, Integer ecgHR, Integer ecgSBP, Integer ecgDBP, Integer healthHrvIndex, Integer healthFatigueIndex, Integer healthLoadIndex, Integer healthBodyIndex, Integer healtHeartIndex) {
        this.id = id;
        this.mid = mid;
        this.bleMac = bleMac;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.ecgDataArr = ecgDataArr;
        this.ppgDataArr = ppgDataArr;
        this.ecgHR = ecgHR;
        this.ecgSBP = ecgSBP;
        this.ecgDBP = ecgDBP;
        this.healthHrvIndex = healthHrvIndex;
        this.healthFatigueIndex = healthFatigueIndex;
        this.healthLoadIndex = healthLoadIndex;
        this.healthBodyIndex = healthBodyIndex;
        this.healtHeartIndex = healtHeartIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getBleMac() {
        return bleMac;
    }

    public void setBleMac(String bleMac) {
        this.bleMac = bleMac;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEcgDataArr() {
        return ecgDataArr;
    }

    public void setEcgDataArr(String ecgDataArr) {
        this.ecgDataArr = ecgDataArr;
    }

    public String getPpgDataArr() {
        return ppgDataArr;
    }

    public void setPpgDataArr(String ppgDataArr) {
        this.ppgDataArr = ppgDataArr;
    }

    public Integer getEcgHR() {
        return ecgHR;
    }

    public void setEcgHR(Integer ecgHR) {
        this.ecgHR = ecgHR;
    }

    public Integer getEcgSBP() {
        return ecgSBP;
    }

    public void setEcgSBP(Integer ecgSBP) {
        this.ecgSBP = ecgSBP;
    }

    public Integer getEcgDBP() {
        return ecgDBP;
    }

    public void setEcgDBP(Integer ecgDBP) {
        this.ecgDBP = ecgDBP;
    }

    public Integer getHealthHrvIndex() {
        return healthHrvIndex;
    }

    public void setHealthHrvIndex(Integer healthHrvIndex) {
        this.healthHrvIndex = healthHrvIndex;
    }

    public Integer getHealthFatigueIndex() {
        return healthFatigueIndex;
    }

    public void setHealthFatigueIndex(Integer healthFatigueIndex) {
        this.healthFatigueIndex = healthFatigueIndex;
    }

    public Integer getHealthLoadIndex() {
        return healthLoadIndex;
    }

    public void setHealthLoadIndex(Integer healthLoadIndex) {
        this.healthLoadIndex = healthLoadIndex;
    }

    public Integer getHealthBodyIndex() {
        return healthBodyIndex;
    }

    public void setHealthBodyIndex(Integer healthBodyIndex) {
        this.healthBodyIndex = healthBodyIndex;
    }

    public Integer getHealtHeartIndex() {
        return healtHeartIndex;
    }

    public void setHealtHeartIndex(Integer healtHeartIndex) {
        this.healtHeartIndex = healtHeartIndex;
    }

}
