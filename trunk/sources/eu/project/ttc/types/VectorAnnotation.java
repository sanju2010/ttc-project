

/* First created by JCasGen Wed Nov 23 15:19:27 CET 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Dec 13 12:41:54 CET 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class VectorAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(VectorAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected VectorAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public VectorAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public VectorAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public VectorAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: item

  /** getter for item - gets 
   * @generated */
  public String getItem() {
    if (VectorAnnotation_Type.featOkTst && ((VectorAnnotation_Type)jcasType).casFeat_item == null)
      jcasType.jcas.throwFeatMissing("item", "eu.project.ttc.types.VectorAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((VectorAnnotation_Type)jcasType).casFeatCode_item);}
    
  /** setter for item - sets  
   * @generated */
  public void setItem(String v) {
    if (VectorAnnotation_Type.featOkTst && ((VectorAnnotation_Type)jcasType).casFeat_item == null)
      jcasType.jcas.throwFeatMissing("item", "eu.project.ttc.types.VectorAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((VectorAnnotation_Type)jcasType).casFeatCode_item, v);}    
   
    
  //*--------------*
  //* Feature: frequency

  /** getter for frequency - gets 
   * @generated */
  public int getFrequency() {
    if (VectorAnnotation_Type.featOkTst && ((VectorAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "eu.project.ttc.types.VectorAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((VectorAnnotation_Type)jcasType).casFeatCode_frequency);}
    
  /** setter for frequency - sets  
   * @generated */
  public void setFrequency(int v) {
    if (VectorAnnotation_Type.featOkTst && ((VectorAnnotation_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "eu.project.ttc.types.VectorAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((VectorAnnotation_Type)jcasType).casFeatCode_frequency, v);}    
  }

    