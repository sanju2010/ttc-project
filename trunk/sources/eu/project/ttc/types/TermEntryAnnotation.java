

/* First created by JCasGen Thu Oct 20 17:03:42 CEST 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Nov 05 19:54:33 CET 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
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
  //* Feature: frequency

  /** getter for frequency - gets 
   * @generated */
  public double getFrequency() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermEntryAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_frequency);}
    
  /** setter for frequency - sets  
   * @generated */
  public void setFrequency(double v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermEntryAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_frequency, v);}    
   
    
  //*--------------*
  //* Feature: specificity

  /** getter for specificity - gets 
   * @generated */
  public double getSpecificity() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_specificity == null)
      jcasType.jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermEntryAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_specificity);}
    
  /** setter for specificity - sets  
   * @generated */
  public void setSpecificity(double v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_specificity == null)
      jcasType.jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermEntryAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_specificity, v);}    
   
    
  //*--------------*
  //* Feature: complexity

  /** getter for complexity - gets 
   * @generated */
  public String getComplexity() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_complexity == null)
      jcasType.jcas.throwFeatMissing("complexity", "eu.project.ttc.types.TermEntryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_complexity);}
    
  /** setter for complexity - sets  
   * @generated */
  public void setComplexity(String v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_complexity == null)
      jcasType.jcas.throwFeatMissing("complexity", "eu.project.ttc.types.TermEntryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_complexity, v);}    
   
    
  //*--------------*
  //* Feature: category

  /** getter for category - gets 
   * @generated */
  public String getCategory() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.TermEntryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_category);}
    
  /** setter for category - sets  
   * @generated */
  public void setCategory(String v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.TermEntryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_category, v);}    
   
    
  //*--------------*
  //* Feature: term

  /** getter for term - gets 
   * @generated */
  public String getTerm() {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TermEntryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_term);}
    
  /** setter for term - sets  
   * @generated */
  public void setTerm(String v) {
    if (TermEntryAnnotation_Type.featOkTst && ((TermEntryAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TermEntryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermEntryAnnotation_Type)jcasType).casFeatCode_term, v);}    
  }

    