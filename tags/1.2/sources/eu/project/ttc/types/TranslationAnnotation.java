

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Mar 01 23:11:13 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TranslationAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TranslationAnnotation.class);
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
  protected TranslationAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TranslationAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TranslationAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TranslationAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: term

  /** getter for term - gets 
   * @generated */
  public TermAnnotation getTerm() {
    if (TranslationAnnotation_Type.featOkTst && ((TranslationAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TranslationAnnotation");
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TranslationAnnotation_Type)jcasType).casFeatCode_term)));}
    
  /** setter for term - sets  
   * @generated */
  public void setTerm(TermAnnotation v) {
    if (TranslationAnnotation_Type.featOkTst && ((TranslationAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TranslationAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TranslationAnnotation_Type)jcasType).casFeatCode_term, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: language

  /** getter for language - gets 
   * @generated */
  public String getLanguage() {
    if (TranslationAnnotation_Type.featOkTst && ((TranslationAnnotation_Type)jcasType).casFeat_language == null)
      jcasType.jcas.throwFeatMissing("language", "eu.project.ttc.types.TranslationAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TranslationAnnotation_Type)jcasType).casFeatCode_language);}
    
  /** setter for language - sets  
   * @generated */
  public void setLanguage(String v) {
    if (TranslationAnnotation_Type.featOkTst && ((TranslationAnnotation_Type)jcasType).casFeat_language == null)
      jcasType.jcas.throwFeatMissing("language", "eu.project.ttc.types.TranslationAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TranslationAnnotation_Type)jcasType).casFeatCode_language, v);}    
  }

    