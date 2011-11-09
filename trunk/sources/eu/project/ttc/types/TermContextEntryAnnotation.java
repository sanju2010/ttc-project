

/* First created by JCasGen Tue Oct 25 10:43:56 CEST 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Nov 05 19:54:33 CET 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TermContextEntryAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermContextEntryAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermContextEntryAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermContextEntryAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermContextEntryAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermContextEntryAnnotation(JCas jcas, int begin, int end) {
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
    if (TermContextEntryAnnotation_Type.featOkTst && ((TermContextEntryAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.TermContextEntryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermContextEntryAnnotation_Type)jcasType).casFeatCode_category);}
    
  /** setter for category - sets  
   * @generated */
  public void setCategory(String v) {
    if (TermContextEntryAnnotation_Type.featOkTst && ((TermContextEntryAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.TermContextEntryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermContextEntryAnnotation_Type)jcasType).casFeatCode_category, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (TermContextEntryAnnotation_Type.featOkTst && ((TermContextEntryAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "eu.project.ttc.types.TermContextEntryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermContextEntryAnnotation_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (TermContextEntryAnnotation_Type.featOkTst && ((TermContextEntryAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "eu.project.ttc.types.TermContextEntryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermContextEntryAnnotation_Type)jcasType).casFeatCode_lemma, v);}    
  }

    