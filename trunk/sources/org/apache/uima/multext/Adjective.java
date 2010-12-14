

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
public class Adjective extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Adjective.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Adjective() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Adjective(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Adjective(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Adjective(JCas jcas, int begin, int end) {
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
  //* Feature: adjectiveType

  /** getter for adjectiveType - gets 
   * @generated */
  public String getAdjectiveType() {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_adjectiveType == null)
      jcasType.jcas.throwFeatMissing("adjectiveType", "org.apache.uima.multext.Adjective");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_adjectiveType);}
    
  /** setter for adjectiveType - sets  
   * @generated */
  public void setAdjectiveType(String v) {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_adjectiveType == null)
      jcasType.jcas.throwFeatMissing("adjectiveType", "org.apache.uima.multext.Adjective");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_adjectiveType, v);}    
   
    
  //*--------------*
  //* Feature: degree

  /** getter for degree - gets 
   * @generated */
  public String getDegree() {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adjective");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_degree);}
    
  /** setter for degree - sets  
   * @generated */
  public void setDegree(String v) {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adjective");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_degree, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Adjective");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Adjective");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Adjective");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Adjective");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Adjective");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (Adjective_Type.featOkTst && ((Adjective_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Adjective");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adjective_Type)jcasType).casFeatCode_case, v);}    
  }

    