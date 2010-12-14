

/* First created by JCasGen Thu Dec 02 15:43:30 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Dec 14 21:42:40 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/TTC Preliminary Linguistic Analysis.xml
 * @generated */
public class MultextAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(MultextAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected MultextAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MultextAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MultextAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MultextAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: tag

  /** getter for tag - gets 
   * @generated */
  public String getTag() {
    if (MultextAnnotation_Type.featOkTst && ((MultextAnnotation_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "org.apache.uima.MultextAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((MultextAnnotation_Type)jcasType).casFeatCode_tag);}
    
  /** setter for tag - sets  
   * @generated */
  public void setTag(String v) {
    if (MultextAnnotation_Type.featOkTst && ((MultextAnnotation_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "org.apache.uima.MultextAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((MultextAnnotation_Type)jcasType).casFeatCode_tag, v);}    
   
    
  //*--------------*
  //* Feature: orig

  /** getter for orig - gets 
   * @generated */
  public Annotation getOrig() {
    if (MultextAnnotation_Type.featOkTst && ((MultextAnnotation_Type)jcasType).casFeat_orig == null)
      jcasType.jcas.throwFeatMissing("orig", "org.apache.uima.MultextAnnotation");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MultextAnnotation_Type)jcasType).casFeatCode_orig)));}
    
  /** setter for orig - sets  
   * @generated */
  public void setOrig(Annotation v) {
    if (MultextAnnotation_Type.featOkTst && ((MultextAnnotation_Type)jcasType).casFeat_orig == null)
      jcasType.jcas.throwFeatMissing("orig", "org.apache.uima.MultextAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((MultextAnnotation_Type)jcasType).casFeatCode_orig, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    