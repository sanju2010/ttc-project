

/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Tue Dec 14 21:12:06 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/TTC Type System.xml
 * @generated */
public class Conjunction extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Conjunction.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Conjunction() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Conjunction(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Conjunction(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Conjunction(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: conjunctionType

  /** getter for conjunctionType - gets 
   * @generated */
  public String getConjunctionType() {
    if (Conjunction_Type.featOkTst && ((Conjunction_Type)jcasType).casFeat_conjunctionType == null)
      jcasType.jcas.throwFeatMissing("conjunctionType", "org.apache.uima.multext.Conjunction");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Conjunction_Type)jcasType).casFeatCode_conjunctionType);}
    
  /** setter for conjunctionType - sets  
   * @generated */
  public void setConjunctionType(String v) {
    if (Conjunction_Type.featOkTst && ((Conjunction_Type)jcasType).casFeat_conjunctionType == null)
      jcasType.jcas.throwFeatMissing("conjunctionType", "org.apache.uima.multext.Conjunction");
    jcasType.ll_cas.ll_setStringValue(addr, ((Conjunction_Type)jcasType).casFeatCode_conjunctionType, v);}    
  }

    