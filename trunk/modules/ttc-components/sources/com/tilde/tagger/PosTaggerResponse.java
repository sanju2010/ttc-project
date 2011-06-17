
package com.tilde.tagger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PosTaggerResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "posTaggerResult"
})
@XmlRootElement(name = "PosTaggerResponse")
public class PosTaggerResponse {

    @XmlElement(name = "PosTaggerResult")
    protected String posTaggerResult;

    /**
     * Gets the value of the posTaggerResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosTaggerResult() {
        return posTaggerResult;
    }

    /**
     * Sets the value of the posTaggerResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosTaggerResult(String value) {
        this.posTaggerResult = value;
    }

}
