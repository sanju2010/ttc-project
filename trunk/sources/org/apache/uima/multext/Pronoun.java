

/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Wed Dec 15 10:23:54 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/models/TTC Document Processor.xml
 * @generated */
public class Pronoun extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Pronoun.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Pronoun() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Pronoun(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Pronoun(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Pronoun(JCas jcas, int begin, int end) {
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
  //* Feature: pronounType

  /** getter for pronounType - gets 
   * @generated */
  public String getPronounType() {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_pronounType == null)
      jcasType.jcas.throwFeatMissing("pronounType", "org.apache.uima.multext.Pronoun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_pronounType);}
    
  /** setter for pronounType - sets  
   * @generated */
  public void setPronounType(String v) {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_pronounType == null)
      jcasType.jcas.throwFeatMissing("pronounType", "org.apache.uima.multext.Pronoun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_pronounType, v);}    
   
    
  //*--------------*
  //* Feature: person

  /** getter for person - gets 
   * @generated */
  public String getPerson() {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.multext.Pronoun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_person);}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(String v) {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.multext.Pronoun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_person, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Pronoun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Pronoun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Pronoun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Pronoun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Pronoun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Pronoun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_case, v);}    
   
    
  //*--------------*
  //* Feature: possessor

  /** getter for possessor - gets 
   * @generated */
  public String getPossessor() {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_possessor == null)
      jcasType.jcas.throwFeatMissing("possessor", "org.apache.uima.multext.Pronoun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_possessor);}
    
  /** setter for possessor - sets  
   * @generated */
  public void setPossessor(String v) {
    if (Pronoun_Type.featOkTst && ((Pronoun_Type)jcasType).casFeat_possessor == null)
      jcasType.jcas.throwFeatMissing("possessor", "org.apache.uima.multext.Pronoun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Pronoun_Type)jcasType).casFeatCode_possessor, v);}    
  }

    