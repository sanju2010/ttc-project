

/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Tue Dec 14 21:42:40 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/TTC Preliminary Linguistic Analysis.xml
 * @generated */
public class Numeral extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Numeral.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Numeral() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Numeral(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Numeral(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Numeral(JCas jcas, int begin, int end) {
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
  //* Feature: numeralType

  /** getter for numeralType - gets 
   * @generated */
  public String getNumeralType() {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_numeralType == null)
      jcasType.jcas.throwFeatMissing("numeralType", "org.apache.uima.multext.Numeral");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_numeralType);}
    
  /** setter for numeralType - sets  
   * @generated */
  public void setNumeralType(String v) {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_numeralType == null)
      jcasType.jcas.throwFeatMissing("numeralType", "org.apache.uima.multext.Numeral");
    jcasType.ll_cas.ll_setStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_numeralType, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Numeral");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Numeral");
    jcasType.ll_cas.ll_setStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Numeral");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Numeral");
    jcasType.ll_cas.ll_setStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Numeral");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (Numeral_Type.featOkTst && ((Numeral_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Numeral");
    jcasType.ll_cas.ll_setStringValue(addr, ((Numeral_Type)jcasType).casFeatCode_case, v);}    
  }

    