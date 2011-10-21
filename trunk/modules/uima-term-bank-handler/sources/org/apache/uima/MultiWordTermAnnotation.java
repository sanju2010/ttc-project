

/* First created by JCasGen Fri Sep 16 20:21:04 CEST 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;


/** 
 * Updated by JCasGen Thu Oct 20 14:55:47 CEST 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/modules/uima-term-bank-handler/resources/fr/univnantes/lina/uima/types/TermAnnotation.xml
 * @generated */
public class MultiWordTermAnnotation extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(MultiWordTermAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected MultiWordTermAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MultiWordTermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MultiWordTermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MultiWordTermAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: components

  /** getter for components - gets 
   * @generated */
  public FSArray getComponents() {
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "org.apache.uima.MultiWordTermAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components)));}
    
  /** setter for components - sets  
   * @generated */
  public void setComponents(FSArray v) {
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "org.apache.uima.MultiWordTermAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for components - gets an indexed value - 
   * @generated */
  public TermComponentAnnotation getComponents(int i) {
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "org.apache.uima.MultiWordTermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i);
    return (TermComponentAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i)));}

  /** indexed setter for components - sets an indexed value - 
   * @generated */
  public void setComponents(int i, TermComponentAnnotation v) { 
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "org.apache.uima.MultiWordTermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    