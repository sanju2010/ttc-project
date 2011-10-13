

/* First created by JCasGen Tue Feb 01 14:10:06 CET 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Oct 04 09:29:21 CEST 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/uima-term-bank-handlers/trunk/resources/fr/univnantes/lina/uima/types/TermAnnotation.xml
 * @generated */
public class TermAnnotation extends TermComponentAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: document

  /** getter for document - gets 
   * @generated */
  public String getDocument() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_document == null)
      jcasType.jcas.throwFeatMissing("document", "org.apache.uima.TermAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_document);}
    
  /** setter for document - sets  
   * @generated */
  public void setDocument(String v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_document == null)
      jcasType.jcas.throwFeatMissing("document", "org.apache.uima.TermAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_document, v);}    
   
    
  //*--------------*
  //* Feature: complexity

  /** getter for complexity - gets 
   * @generated */
  public String getComplexity() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_complexity == null)
      jcasType.jcas.throwFeatMissing("complexity", "org.apache.uima.TermAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_complexity);}
    
  /** setter for complexity - sets  
   * @generated */
  public void setComplexity(String v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_complexity == null)
      jcasType.jcas.throwFeatMissing("complexity", "org.apache.uima.TermAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_complexity, v);}    
   
    
  //*--------------*
  //* Feature: frequency

  /** getter for frequency - gets 
   * @generated */
  public double getFrequency() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "org.apache.uima.TermAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_frequency);}
    
  /** setter for frequency - sets  
   * @generated */
  public void setFrequency(double v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "org.apache.uima.TermAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_frequency, v);}    
   
    
  //*--------------*
  //* Feature: specificity

  /** getter for specificity - gets 
   * @generated */
  public double getSpecificity() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_specificity == null)
      jcasType.jcas.throwFeatMissing("specificity", "org.apache.uima.TermAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_specificity);}
    
  /** setter for specificity - sets  
   * @generated */
  public void setSpecificity(double v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_specificity == null)
      jcasType.jcas.throwFeatMissing("specificity", "org.apache.uima.TermAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_specificity, v);}    
  }

    