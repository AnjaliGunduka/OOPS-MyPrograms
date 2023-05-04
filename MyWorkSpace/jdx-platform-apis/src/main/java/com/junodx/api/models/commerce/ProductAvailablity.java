package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.core.DMA;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.State;
import com.junodx.api.models.core.ZipCode;
import com.junodx.api.services.exceptions.JdxServiceException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "product_availability")
public class ProductAvailablity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    protected Product product;

    @Transient
    protected Boolean outOfRegion;
    @Transient
    protected Boolean soldOut;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected Calendar newStockAvailableAt;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected Calendar availableInYourRegionAt;

    @Transient
    protected List<ZipCode> allowedZipCodes;
    @Transient
    protected List<State> allowedStates;
    @Transient
    protected List<DMA> allowedDMAs;

    @JsonIgnore
    protected String allowedZipCodesAsString;

    @JsonIgnore
    protected String allowedStatesAsString;

    @JsonIgnore
    protected String allowedDMAsAsString;

    protected Meta meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ZipCode> getAllowedZipCodes() {

        if(this.allowedZipCodesAsString != null) {
            String[] zips = this.allowedZipCodesAsString.split(",");
            for (String s : zips) {
                if (this.allowedZipCodes == null)
                    this.allowedZipCodes = new ArrayList<>();
                if (!this.allowedZipCodes.stream().anyMatch(x -> x.getZip().equals(s)))
                    this.allowedZipCodes.add(new ZipCode(s));
            }
        }

        return allowedZipCodes;
    }

    public void setAllowedZipCodes(List<ZipCode> allowedZipCodes) {
        this.allowedZipCodes = allowedZipCodes;
        setAllowedZipCodesAsString(zipsAsStrings());
    }

    public void addAllowedZipCode(ZipCode zip) {
        if(this.allowedZipCodes == null)
            this.allowedZipCodes = new ArrayList<>();
        this.allowedZipCodes.add(zip);
        setAllowedZipCodesAsString(zipsAsStrings());
    }

    public List<State> getAllowedStates() {
        if(this.allowedStatesAsString != null) {
            String[] states = this.allowedStatesAsString.split(",");
            for (String s : states) {
                if (this.allowedStates == null)
                    this.allowedStates = new ArrayList<>();
                if (!this.allowedStates.stream().anyMatch(x -> x.getAbbr().equals(s)))
                    this.allowedStates.add(new State(s));
            }
        }
        return allowedStates;
    }

    public void setAllowedStates(List<State> allowedStates) {
        this.allowedStates = allowedStates;
        setAllowedStatesAsString(statesAsString());
    }

    public void addAllowedState(State state) {
        if(this.allowedStates == null)
            this.allowedStates = new ArrayList<>();
        this.allowedStates.add(state);
        setAllowedStatesAsString(statesAsString());
    }

    public List<DMA> getAllowedDMAs() {
        if(this.allowedDMAsAsString != null) {
            String[] dmas = this.allowedDMAsAsString.split(",");
            for (String s : dmas) {
                if (this.allowedDMAs == null)
                    this.allowedDMAs = new ArrayList<>();
                if (!this.allowedDMAs.stream().anyMatch(x -> x.getCode().equals(s)))
                    this.allowedDMAs.add(new DMA(s));
            }
        }
        return allowedDMAs;
    }

    public void setAllowedDMAs(List<DMA> allowedDMAs) {
        this.allowedDMAs = allowedDMAs;
        setAllowedDMAsAsString(dmasAsString());
    }

    public void addAllowedDMA(DMA dma) {
        if(this.allowedDMAs == null)
            this.allowedDMAs = new ArrayList<>();
        this.allowedDMAs.add(dma);
        setAllowedDMAsAsString(dmasAsString());
    }

    public String getAllowedZipCodesAsString() {
        return allowedZipCodesAsString;
    }

    public void setAllowedZipCodesAsString(String allowedZipCodesAsString) {
        this.allowedZipCodesAsString = allowedZipCodesAsString;
        if(this.allowedZipCodesAsString != null) {
            String[] zips = this.allowedZipCodesAsString.split(",");
            for (String s : zips) {
                if (this.allowedZipCodes == null)
                    this.allowedZipCodes = new ArrayList<>();
                if (!this.allowedZipCodes.stream().anyMatch(x -> x.getZip().equals(s)))
                    this.allowedZipCodes.add(new ZipCode(s));
            }
        }
    }

    public String getAllowedStatesAsString() {
        return allowedStatesAsString;
    }

    public void setAllowedStatesAsString(String allowedStatesAsString) {
        this.allowedStatesAsString = allowedStatesAsString;
        if(this.allowedStatesAsString != null) {
            String[] states = this.allowedStatesAsString.split(",");
            for (String s : states) {
                if (this.allowedStates == null)
                    this.allowedStates = new ArrayList<>();
                if (!this.allowedStates.stream().anyMatch(x -> x.getAbbr().equals(s)))
                    this.allowedStates.add(new State(s));
            }
        }
    }

    public String getAllowedDMAsAsString() {
        return allowedDMAsAsString;
    }

    public void setAllowedDMAsAsString(String allowedDMAsAsString) {
        this.allowedDMAsAsString = allowedDMAsAsString;
        if(this.allowedDMAsAsString != null) {
            String[] dmas = this.allowedDMAsAsString.split(",");
            for (String s : dmas) {
                if (this.allowedDMAs == null)
                    this.allowedDMAs = new ArrayList<>();
                if (!this.allowedDMAs.stream().anyMatch(x -> x.getCode().equals(s)))
                    this.allowedDMAs.add(new DMA(s));
            }
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    private String zipsAsStrings(){
        String returnString = "";
        if(this.allowedZipCodes != null) {
            for (int i = 0; i < this.allowedZipCodes.size(); i++) {
                ZipCode code = this.allowedZipCodes.get(i);
                returnString += code.getZip();
                if(this.allowedZipCodes.size() > i+1)
                    returnString += ",";
            }
        }
        return returnString;
    }

    private String statesAsString(){
        String returnString = "";
        if(this.allowedStates != null) {
            for (int i = 0; i < this.allowedStates.size(); i++) {
                State state = this.allowedStates.get(i);
                returnString += state.getAbbr();
                if(this.allowedStates.size() > i+1)
                    returnString += ",";
            }
        }
        return returnString;
    }

    private String dmasAsString(){
        String returnString = "";
        if(this.allowedDMAs != null) {
            for (int i = 0; i < this.allowedDMAs.size(); i++) {
                DMA dma = this.allowedDMAs.get(i);
                returnString += dma.getCode();
                if(this.allowedDMAs.size() > i+1)
                    returnString += ",";
            }
        }
        return returnString;
    }

    public boolean isAvailableInState(State state) {
        if(this.allowedStates != null && this.allowedStates.size() > 0)
            return this.allowedStates.stream().anyMatch(x->x.getAbbr().equals(state.getAbbr()));

        return false;
    }

    public boolean isAvailableInZip(ZipCode zip) {
        if(this.allowedZipCodes != null && this.allowedZipCodes.size() > 0)
            return this.allowedZipCodes.stream().anyMatch(x->x.getZip().equals(zip.getZip()));

        return false;
    }

    public boolean isAvailableInDMA(DMA dma) {
        if(this.allowedDMAs != null && this.allowedDMAs.size() > 0)
            return this.allowedDMAs.stream().anyMatch(x->x.getCode().equals(dma.getCode()));

        return false;
    }

    public Calendar getNewStockAvailableAt() {
        return newStockAvailableAt;
    }

    public void setNewStockAvailableAt(Calendar newStockAvailableAt) {
        this.newStockAvailableAt = newStockAvailableAt;
    }

    public Calendar getAvailableInYourRegionAt() {
        return availableInYourRegionAt;
    }

    public void setAvailableInYourRegionAt(Calendar availableInYourRegionAt) {
        this.availableInYourRegionAt = availableInYourRegionAt;
    }

    public Boolean getOutOfRegion() {
        return outOfRegion;
    }

    public void setOutOfRegion(Boolean outOfRegion) {
        this.outOfRegion = outOfRegion;
    }

    public Boolean getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        this.soldOut = soldOut;
    }
}
