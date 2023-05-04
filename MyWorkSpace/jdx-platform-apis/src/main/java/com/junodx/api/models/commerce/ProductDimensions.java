package com.junodx.api.models.commerce;

import com.junodx.api.models.core.LengthUnit;
import com.junodx.api.models.core.WeightUnit;

import javax.persistence.Embeddable;

@Embeddable
public class ProductDimensions {
    protected float height;
    protected float width;
    protected float length;
    protected float weight;
    protected LengthUnit lengthUnits;
    protected WeightUnit weightUnits;

    
    
    
    public ProductDimensions() {
		super();
	}

	public ProductDimensions(float height, float width, float length, float weight, LengthUnit lengthUnits,
			WeightUnit weightUnits) {
		super();
		this.height = height;
		this.width = width;
		this.length = length;
		this.weight = weight;
		this.lengthUnits = lengthUnits;
		this.weightUnits = weightUnits;
	}

	public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


    public LengthUnit getLengthUnits() {
        return lengthUnits;
    }

    public void setLengthUnits(LengthUnit lengthUnits) {
        this.lengthUnits = lengthUnits;
    }

    public WeightUnit getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(WeightUnit weightUnits) {
        this.weightUnits = weightUnits;
    }
}
