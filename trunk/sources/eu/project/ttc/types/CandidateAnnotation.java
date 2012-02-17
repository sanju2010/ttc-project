

/* First created by JCasGen Thu Nov 24 11:32:45 CET 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Feb 17 13:13:10 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class CandidateAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CandidateAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CandidateAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CandidateAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CandidateAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public CandidateAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: translation

  /** getter for translation - gets 
   * @generated */
  public String getTranslation() {
    if (CandidateAnnotation_Type.featOkTst && ((CandidateAnnotation_Type)jcasType).casFeat_translation == null)
      jcasType.jcas.throwFeatMissing("translation", "eu.project.ttc.types.CandidateAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CandidateAnnotation_Type)jcasType).casFeatCode_translation);}
    
  /** setter for translation - sets  
   * @generated */
  public void setTranslation(String v) {
    if (CandidateAnnotation_Type.featOkTst && ((CandidateAnnotation_Type)jcasType).casFeat_translation == null)
      jcasType.jcas.throwFeatMissing("translation", "eu.project.ttc.types.CandidateAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((CandidateAnnotation_Type)jcasType).casFeatCode_translation, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (CandidateAnnotation_Type.featOkTst && ((CandidateAnnotation_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.project.ttc.types.CandidateAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((CandidateAnnotation_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (CandidateAnnotation_Type.featOkTst && ((CandidateAnnotation_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.project.ttc.types.CandidateAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((CandidateAnnotation_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: rank

  /** getter for rank - gets 
   * @generated */
  public int getRank() {
    if (CandidateAnnotation_Type.featOkTst && ((CandidateAnnotation_Type)jcasType).casFeat_rank == null)
      jcasType.jcas.throwFeatMissing("rank", "eu.project.ttc.types.CandidateAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CandidateAnnotation_Type)jcasType).casFeatCode_rank);}
    
  /** setter for rank - sets  
   * @generated */
  public void setRank(int v) {
    if (CandidateAnnotation_Type.featOkTst && ((CandidateAnnotation_Type)jcasType).casFeat_rank == null)
      jcasType.jcas.throwFeatMissing("rank", "eu.project.ttc.types.CandidateAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((CandidateAnnotation_Type)jcasType).casFeatCode_rank, v);}    
  }

    