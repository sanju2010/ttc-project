

/* First created by JCasGen Thu May 26 15:11:56 CEST 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Oct 20 14:55:47 CEST 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/modules/uima-term-bank-handler/resources/fr/univnantes/lina/uima/types/TermAnnotation.xml
 * @generated */
public class TermComponentAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermComponentAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermComponentAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermComponentAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermComponentAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermComponentAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: category

  /** getter for category - gets 
   * @generated */
  public String getCategory() {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "org.apache.uima.TermComponentAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_category);}
    
  /** setter for category - sets  
   * @generated */
  public void setCategory(String v) {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "org.apache.uima.TermComponentAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_category, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "org.apache.uima.TermComponentAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "org.apache.uima.TermComponentAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: language

  /** getter for language - gets 
   * @generated */
  public String getLanguage() {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_language == null)
      jcasType.jcas.throwFeatMissing("language", "org.apache.uima.TermComponentAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_language);}
    
  /** setter for language - sets  
   * @generated */
  public void setLanguage(String v) {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_language == null)
      jcasType.jcas.throwFeatMissing("language", "org.apache.uima.TermComponentAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_language, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.apache.uima.TermComponentAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (TermComponentAnnotation_Type.featOkTst && ((TermComponentAnnotation_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.apache.uima.TermComponentAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermComponentAnnotation_Type)jcasType).casFeatCode_text, v);}    
  }

    