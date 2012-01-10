

/* First created by JCasGen Tue Jan 03 14:27:17 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jan 10 16:01:52 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class SimilarityAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SimilarityAnnotation.class);
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
  protected SimilarityAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SimilarityAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SimilarityAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SimilarityAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: source

  /** getter for source - gets 
   * @generated */
  public String getSource() {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "eu.project.ttc.types.SimilarityAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_source);}
    
  /** setter for source - sets  
   * @generated */
  public void setSource(String v) {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "eu.project.ttc.types.SimilarityAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_source, v);}    
   
    
  //*--------------*
  //* Feature: target

  /** getter for target - gets 
   * @generated */
  public String getTarget() {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_target == null)
      jcasType.jcas.throwFeatMissing("target", "eu.project.ttc.types.SimilarityAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_target);}
    
  /** setter for target - sets  
   * @generated */
  public void setTarget(String v) {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_target == null)
      jcasType.jcas.throwFeatMissing("target", "eu.project.ttc.types.SimilarityAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_target, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.project.ttc.types.SimilarityAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.project.ttc.types.SimilarityAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: distance

  /** getter for distance - gets 
   * @generated */
  public String getDistance() {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_distance == null)
      jcasType.jcas.throwFeatMissing("distance", "eu.project.ttc.types.SimilarityAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_distance);}
    
  /** setter for distance - sets  
   * @generated */
  public void setDistance(String v) {
    if (SimilarityAnnotation_Type.featOkTst && ((SimilarityAnnotation_Type)jcasType).casFeat_distance == null)
      jcasType.jcas.throwFeatMissing("distance", "eu.project.ttc.types.SimilarityAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((SimilarityAnnotation_Type)jcasType).casFeatCode_distance, v);}    
  }

    