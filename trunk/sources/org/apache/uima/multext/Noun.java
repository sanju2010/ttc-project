

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
public class Noun extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Noun.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Noun() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Noun(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Noun(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Noun(JCas jcas, int begin, int end) {
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
  //* Feature: nounType

  /** getter for nounType - gets 
   * @generated */
  public String getNounType() {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_nounType == null)
      jcasType.jcas.throwFeatMissing("nounType", "org.apache.uima.multext.Noun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Noun_Type)jcasType).casFeatCode_nounType);}
    
  /** setter for nounType - sets  
   * @generated */
  public void setNounType(String v) {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_nounType == null)
      jcasType.jcas.throwFeatMissing("nounType", "org.apache.uima.multext.Noun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Noun_Type)jcasType).casFeatCode_nounType, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Noun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Noun_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Noun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Noun_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Noun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Noun_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Noun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Noun_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Noun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Noun_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Noun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Noun_Type)jcasType).casFeatCode_case, v);}    
  }

    