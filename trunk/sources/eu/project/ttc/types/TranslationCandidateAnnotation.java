

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Feb 24 14:28:15 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TranslationCandidateAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TranslationCandidateAnnotation.class);
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
  protected TranslationCandidateAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TranslationCandidateAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TranslationCandidateAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TranslationCandidateAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: translation

  /** getter for translation - gets 
   * @generated */
  public String getTranslation() {
    if (TranslationCandidateAnnotation_Type.featOkTst && ((TranslationCandidateAnnotation_Type)jcasType).casFeat_translation == null)
      jcasType.jcas.throwFeatMissing("translation", "eu.project.ttc.types.TranslationCandidateAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TranslationCandidateAnnotation_Type)jcasType).casFeatCode_translation);}
    
  /** setter for translation - sets  
   * @generated */
  public void setTranslation(String v) {
    if (TranslationCandidateAnnotation_Type.featOkTst && ((TranslationCandidateAnnotation_Type)jcasType).casFeat_translation == null)
      jcasType.jcas.throwFeatMissing("translation", "eu.project.ttc.types.TranslationCandidateAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TranslationCandidateAnnotation_Type)jcasType).casFeatCode_translation, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (TranslationCandidateAnnotation_Type.featOkTst && ((TranslationCandidateAnnotation_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.project.ttc.types.TranslationCandidateAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TranslationCandidateAnnotation_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (TranslationCandidateAnnotation_Type.featOkTst && ((TranslationCandidateAnnotation_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.project.ttc.types.TranslationCandidateAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TranslationCandidateAnnotation_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: rank

  /** getter for rank - gets 
   * @generated */
  public int getRank() {
    if (TranslationCandidateAnnotation_Type.featOkTst && ((TranslationCandidateAnnotation_Type)jcasType).casFeat_rank == null)
      jcasType.jcas.throwFeatMissing("rank", "eu.project.ttc.types.TranslationCandidateAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((TranslationCandidateAnnotation_Type)jcasType).casFeatCode_rank);}
    
  /** setter for rank - sets  
   * @generated */
  public void setRank(int v) {
    if (TranslationCandidateAnnotation_Type.featOkTst && ((TranslationCandidateAnnotation_Type)jcasType).casFeat_rank == null)
      jcasType.jcas.throwFeatMissing("rank", "eu.project.ttc.types.TranslationCandidateAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((TranslationCandidateAnnotation_Type)jcasType).casFeatCode_rank, v);}    
  }

    