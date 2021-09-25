package com.ap.greenpole.transactioncomponent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "shareholder_kin")
public class Holderkin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shareholder_id")
    @JsonIgnore
    ShareHolder shareHolder;

    @JsonProperty("kin_email")
    @Column(name = "kin_email")
    String kinEmail;

    @JsonProperty("kin_name")
    @Column(name = "kin_name")
    String kinName;

    @JsonProperty("kin_phone")
    @Column(name = "kin_phone")
    String kinPhone;

    @JsonProperty("kin_address")
    @Column(name = "kin_address")
    String kinAddress;

    @JsonProperty("kin_country")
    @Column(name = "kin_country")
    String kinCountry;

    @JsonProperty("kin_state")
    @Column(name = "kin_state")
    String kinState;

    @JsonProperty("kin_lga")
    @Column(name = "kin_lga")
    String kinLga;

    String relationship;
    boolean status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getKinEmail() {
        return kinEmail;
    }

    public void setKinEmail(String kinEmail) {
        this.kinEmail = kinEmail;
    }

    public String getKinName() {
        return kinName;
    }

    public void setKinName(String kinName) {
        this.kinName = kinName;
    }

    public String getKinPhone() {
        return kinPhone;
    }

    public void setKinPhone(String kinPhone) {
        this.kinPhone = kinPhone;
    }

    public String getKinAddress() {
        return kinAddress;
    }

    public void setKinAddress(String kinAddress) {
        this.kinAddress = kinAddress;
    }

    public String getKinCountry() {
        return kinCountry;
    }

    public void setKinCountry(String kinCountry) {
        this.kinCountry = kinCountry;
    }

    public String getKinState() {
        return kinState;
    }

    public void setKinState(String kinState) {
        this.kinState = kinState;
    }

    public String getKinLga() {
        return kinLga;
    }

    public void setKinLga(String kinLga) {
        this.kinLga = kinLga;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public ShareHolder getShareHolder() {
        return shareHolder;
    }

    public void setShareHolder(ShareHolder shareHolder) {
        this.shareHolder = shareHolder;
    }
}