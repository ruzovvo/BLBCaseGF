package org.blbcase.core.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
@Entity
public class Bond implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String CUSIP;
    private Double price;
    private Double parValue;
    private Double coupon;
    private Double currentYield;
    private Double yieldToMaturity;
    private Integer quantity;
    private Long clientId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date boughtOn;
    private Integer ratingMoodys;
    private Integer ratingSnp;
    private Long bondId;
    private String issuer;

    private String ratingMoodysString;
    private String ratingSnpString;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date deliveryOn;
    public Bond() {
    }

    public Bond(String CUSIP, Double price, Double parValue, Double coupon, Double currentYield, Double yieldToMaturity, Integer quantity, Long clientId, Date boughtOn, Integer ratingMoodys, Integer ratingSnp, Long bondId, String issuer, String rat1, String rat2) {
        this.CUSIP = CUSIP;
        this.price = price;
        this.parValue = parValue;
        this.coupon = coupon;
        this.currentYield = currentYield;
        this.yieldToMaturity = yieldToMaturity;
        this.ratingMoodys = ratingMoodys;
        this.ratingSnp = ratingSnp;
        this.quantity = quantity;
        this.clientId = clientId;
        this.boughtOn = boughtOn;
        this.bondId = bondId;
        this.issuer = issuer;
        this.ratingMoodysString = rat1;
        this.ratingSnpString = rat2;
    }

    public Date getDeliveryOn() {
        return deliveryOn;
    }

    public void setDeliveryOn(Date deliveryOn) {
        this.deliveryOn = deliveryOn;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getCUSIP() {
        return CUSIP;
    }

    public void setCUSIP(String CUSIP) {
        this.CUSIP = CUSIP;
    }

    public Double getCoupon() {
        return coupon;
    }

    public void setCoupon(Double coupon) {
        this.coupon = coupon;
    }

    public Double getCurrentYield() {
        return currentYield;
    }

    public void setCurrentYield(Double currentYield) {
        this.currentYield = currentYield;
    }

    public Double getParValue() {
        return parValue;
    }

    public void setParValue(Double parValue) {
        this.parValue = parValue;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getYieldToMaturity() {
        return yieldToMaturity;
    }

    public void setYieldToMaturity(Double yieldToMaturity) {
        this.yieldToMaturity = yieldToMaturity;
    }

    public Date getBoughtOn() {
        return boughtOn;
    }

    public void setBoughtOn(Date boughtOn) {
        this.boughtOn = boughtOn;
    }

    public Integer getRatingMoodys() {
        return ratingMoodys;
    }

    public void setRatingMoodys(Integer ratingMoodys) {
        this.ratingMoodys = ratingMoodys;
    }

    public Integer getRatingSnp() {
        return ratingSnp;
    }

    public void setRatingSnp(Integer ratingSnp) {
        this.ratingSnp = ratingSnp;
    }

    public Long getBondId() {
        return bondId;
    }

    public void setBondId(Long bondId) {
        this.bondId = bondId;
    }

    public String getRatingMoodysString() {
        return ratingMoodysString;
    }

    public void setRatingMoodysString(String ratingMoodysString) {
        this.ratingMoodysString = ratingMoodysString;
    }

    public String getRatingSnpString() {
        return ratingSnpString;
    }

    public void setRatingSnpString(String ratingSnpString) {
        this.ratingSnpString = ratingSnpString;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bond)) {
            return false;
        }
        Bond other = (Bond) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public boolean canBeMerged(Bond bond)
    {
        //System.out.println(this.bondId + " " + bond.getBondId() + " " + this.boughtOn + " " + bond.getBoughtOn());
        return (this.bondId == bond.getBondId());
        //return (this.CUSIP.equals(bond.getCUSIP()) && Double.compare(coupon, bond.getCoupon()) == 0
        //        && this.currentYield == bond.getCurrentYield() && this.parValue == bond.getParValue() 
        //        && this.price == bond.getPrice()
        //        && this.yieldToMaturity == bond.getYieldToMaturity());
    }

    @Override
    public String toString() {
        return "org.blbcase.core.jpa.entity.Bond[ id=" + id + " ]";
    }
}
