

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
public class Verb extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Verb.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Verb() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Verb(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Verb(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Verb(JCas jcas, int begin, int end) {
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
  //* Feature: verbType

  /** getter for verbType - gets 
   * @generated */
  public String getVerbType() {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_verbType == null)
      jcasType.jcas.throwFeatMissing("verbType", "org.apache.uima.multext.Verb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Verb_Type)jcasType).casFeatCode_verbType);}
    
  /** setter for verbType - sets  
   * @generated */
  public void setVerbType(String v) {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_verbType == null)
      jcasType.jcas.throwFeatMissing("verbType", "org.apache.uima.multext.Verb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Verb_Type)jcasType).casFeatCode_verbType, v);}    
   
    
  //*--------------*
  //* Feature: mood

  /** getter for mood - gets 
   * @generated */
  public String getMood() {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_mood == null)
      jcasType.jcas.throwFeatMissing("mood", "org.apache.uima.multext.Verb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Verb_Type)jcasType).casFeatCode_mood);}
    
  /** setter for mood - sets  
   * @generated */
  public void setMood(String v) {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_mood == null)
      jcasType.jcas.throwFeatMissing("mood", "org.apache.uima.multext.Verb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Verb_Type)jcasType).casFeatCode_mood, v);}    
   
    
  //*--------------*
  //* Feature: tense

  /** getter for tense - gets 
   * @generated */
  public String getTense() {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_tense == null)
      jcasType.jcas.throwFeatMissing("tense", "org.apache.uima.multext.Verb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Verb_Type)jcasType).casFeatCode_tense);}
    
  /** setter for tense - sets  
   * @generated */
  public void setTense(String v) {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_tense == null)
      jcasType.jcas.throwFeatMissing("tense", "org.apache.uima.multext.Verb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Verb_Type)jcasType).casFeatCode_tense, v);}    
   
    
  //*--------------*
  //* Feature: person

  /** getter for person - gets 
   * @generated */
  public String getPerson() {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.multext.Verb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Verb_Type)jcasType).casFeatCode_person);}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(String v) {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.multext.Verb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Verb_Type)jcasType).casFeatCode_person, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Verb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Verb_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Verb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Verb_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Verb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Verb_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Verb_Type.featOkTst && ((Verb_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Verb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Verb_Type)jcasType).casFeatCode_gender, v);}    
  }

    