

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Feb 29 11:17:07 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TermAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TermAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermAnnotation() {/* intentionally empty block */}
    
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
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: frequency

  /** getter for frequency - gets 
   * @generated */
  public double getFrequency() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_frequency);}
    
  /** setter for frequency - sets  
   * @generated */
  public void setFrequency(double v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_frequency, v);}    
   
    
  //*--------------*
  //* Feature: specificity

  /** getter for specificity - gets 
   * @generated */
  public double getSpecificity() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_specificity == null)
      jcasType.jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_specificity);}
    
  /** setter for specificity - sets  
   * @generated */
  public void setSpecificity(double v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_specificity == null)
      jcasType.jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_specificity, v);}    
   
    
  //*--------------*
  //* Feature: category

  /** getter for category - gets 
   * @generated */
  public String getCategory() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.TermAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_category);}
    
  /** setter for category - sets  
   * @generated */
  public void setCategory(String v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_category, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "eu.project.ttc.types.TermAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: variants

  /** getter for variants - gets 
   * @generated */
  public FSArray getVariants() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_variants == null)
      jcasType.jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_variants)));}
    
  /** setter for variants - sets  
   * @generated */
  public void setVariants(FSArray v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_variants == null)
      jcasType.jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_variants, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for variants - gets an indexed value - 
   * @generated */
  public TermAnnotation getVariants(int i) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_variants == null)
      jcasType.jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_variants), i);
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_variants), i)));}

  /** indexed setter for variants - sets an indexed value - 
   * @generated */
  public void setVariants(int i, TermAnnotation v) { 
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_variants == null)
      jcasType.jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_variants), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_variants), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: occurrences

  /** getter for occurrences - gets 
   * @generated */
  public int getOccurrences() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "eu.project.ttc.types.TermAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_occurrences);}
    
  /** setter for occurrences - sets  
   * @generated */
  public void setOccurrences(int v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_occurrences, v);}    
   
    
  //*--------------*
  //* Feature: context

  /** getter for context - gets 
   * @generated */
  public FSArray getContext() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_context)));}
    
  /** setter for context - sets  
   * @generated */
  public void setContext(FSArray v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_context, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for context - gets an indexed value - 
   * @generated */
  public TermAnnotation getContext(int i) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_context), i);
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_context), i)));}

  /** indexed setter for context - sets an indexed value - 
   * @generated */
  public void setContext(int i, TermAnnotation v) { 
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_context), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_context), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    