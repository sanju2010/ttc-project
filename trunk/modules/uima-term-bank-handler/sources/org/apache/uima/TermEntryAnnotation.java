

/* First created by JCasGen Tue Sep 27 11:47:23 CEST 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Thu Oct 20 14:55:47 CEST 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/modules/uima-term-bank-handler/resources/fr/univnantes/lina/uima/types/TermAnnotation.xml
 * @generated */
public class TermEntryAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermEntryAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermEntryAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermEntryAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermEntryAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermEntryAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: occurrences

  /** getter for occurrences - gets 
   * @generated */
  public FSArray getOccurrences() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_occurrences)));}
    
  /** setter for occurrences - sets  
   * @generated */
  public void setOccurrences(FSArray v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_occurrences, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for occurrences - gets an indexed value - 
   * @generated */
  public TermAnnotation getOccurrences(int i) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_occurrences), i);
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_occurrences), i)));}

  /** indexed setter for occurrences - sets an indexed value - 
   * @generated */
  public void setOccurrences(int i, TermAnnotation v) { 
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_occurrences), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_occurrences), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.apache.uima.TermEntryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.apache.uima.TermEntryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_text, v);}    
  }

    