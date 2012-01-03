

/* First created by JCasGen Thu Dec 15 23:50:29 CET 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jan 03 14:27:17 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TermVariationAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermVariationAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermVariationAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermVariationAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermVariationAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermVariationAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: base

  /** getter for base - gets 
   * @generated */
  public TermAnnotation getBase() {
    if (TermVariationAnnotation_Type.featOkTst && ((TermVariationAnnotation_Type)jcasType).casFeat_base == null)
      jcasType.jcas.throwFeatMissing("base", "eu.project.ttc.types.TermVariationAnnotation");
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermVariationAnnotation_Type)jcasType).casFeatCode_base)));}
    
  /** setter for base - sets  
   * @generated */
  public void setBase(TermAnnotation v) {
    if (TermVariationAnnotation_Type.featOkTst && ((TermVariationAnnotation_Type)jcasType).casFeat_base == null)
      jcasType.jcas.throwFeatMissing("base", "eu.project.ttc.types.TermVariationAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermVariationAnnotation_Type)jcasType).casFeatCode_base, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: variant

  /** getter for variant - gets 
   * @generated */
  public TermAnnotation getVariant() {
    if (TermVariationAnnotation_Type.featOkTst && ((TermVariationAnnotation_Type)jcasType).casFeat_variant == null)
      jcasType.jcas.throwFeatMissing("variant", "eu.project.ttc.types.TermVariationAnnotation");
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermVariationAnnotation_Type)jcasType).casFeatCode_variant)));}
    
  /** setter for variant - sets  
   * @generated */
  public void setVariant(TermAnnotation v) {
    if (TermVariationAnnotation_Type.featOkTst && ((TermVariationAnnotation_Type)jcasType).casFeat_variant == null)
      jcasType.jcas.throwFeatMissing("variant", "eu.project.ttc.types.TermVariationAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermVariationAnnotation_Type)jcasType).casFeatCode_variant, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    