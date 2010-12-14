

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
public class Determiner extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Determiner.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Determiner() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Determiner(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Determiner(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Determiner(JCas jcas, int begin, int end) {
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
  //* Feature: determinerType

  /** getter for determinerType - gets 
   * @generated */
  public String getDeterminerType() {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_determinerType == null)
      jcasType.jcas.throwFeatMissing("determinerType", "org.apache.uima.multext.Determiner");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_determinerType);}
    
  /** setter for determinerType - sets  
   * @generated */
  public void setDeterminerType(String v) {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_determinerType == null)
      jcasType.jcas.throwFeatMissing("determinerType", "org.apache.uima.multext.Determiner");
    jcasType.ll_cas.ll_setStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_determinerType, v);}    
   
    
  //*--------------*
  //* Feature: person

  /** getter for person - gets 
   * @generated */
  public String getPerson() {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.multext.Determiner");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_person);}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(String v) {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.multext.Determiner");
    jcasType.ll_cas.ll_setStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_person, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Determiner");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Determiner");
    jcasType.ll_cas.ll_setStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Determiner");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Determiner");
    jcasType.ll_cas.ll_setStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Determiner");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Determiner");
    jcasType.ll_cas.ll_setStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_case, v);}    
   
    
  //*--------------*
  //* Feature: possessor

  /** getter for possessor - gets 
   * @generated */
  public String getPossessor() {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_possessor == null)
      jcasType.jcas.throwFeatMissing("possessor", "org.apache.uima.multext.Determiner");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_possessor);}
    
  /** setter for possessor - sets  
   * @generated */
  public void setPossessor(String v) {
    if (Determiner_Type.featOkTst && ((Determiner_Type)jcasType).casFeat_possessor == null)
      jcasType.jcas.throwFeatMissing("possessor", "org.apache.uima.multext.Determiner");
    jcasType.ll_cas.ll_setStringValue(addr, ((Determiner_Type)jcasType).casFeatCode_possessor, v);}    
  }

    