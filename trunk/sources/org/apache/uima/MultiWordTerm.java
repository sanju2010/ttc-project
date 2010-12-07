

/* First created by JCasGen Thu Dec 02 15:51:13 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class MultiWordTerm extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(MultiWordTerm.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected MultiWordTerm() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MultiWordTerm(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MultiWordTerm(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MultiWordTerm(JCas jcas, int begin, int end) {
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
  //* Feature: annotations

  /** getter for annotations - gets 
   * @generated */
  public FSArray getAnnotations() {
    if (MultiWordTerm_Type.featOkTst && ((MultiWordTerm_Type)jcasType).casFeat_annotations == null)
      jcasType.jcas.throwFeatMissing("annotations", "org.apache.uima.MultiWordTerm");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTerm_Type)jcasType).casFeatCode_annotations)));}
    
  /** setter for annotations - sets  
   * @generated */
  public void setAnnotations(FSArray v) {
    if (MultiWordTerm_Type.featOkTst && ((MultiWordTerm_Type)jcasType).casFeat_annotations == null)
      jcasType.jcas.throwFeatMissing("annotations", "org.apache.uima.MultiWordTerm");
    jcasType.ll_cas.ll_setRefValue(addr, ((MultiWordTerm_Type)jcasType).casFeatCode_annotations, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for annotations - gets an indexed value - 
   * @generated */
  public Annotation getAnnotations(int i) {
    if (MultiWordTerm_Type.featOkTst && ((MultiWordTerm_Type)jcasType).casFeat_annotations == null)
      jcasType.jcas.throwFeatMissing("annotations", "org.apache.uima.MultiWordTerm");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTerm_Type)jcasType).casFeatCode_annotations), i);
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTerm_Type)jcasType).casFeatCode_annotations), i)));}

  /** indexed setter for annotations - sets an indexed value - 
   * @generated */
  public void setAnnotations(int i, Annotation v) { 
    if (MultiWordTerm_Type.featOkTst && ((MultiWordTerm_Type)jcasType).casFeat_annotations == null)
      jcasType.jcas.throwFeatMissing("annotations", "org.apache.uima.MultiWordTerm");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTerm_Type)jcasType).casFeatCode_annotations), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTerm_Type)jcasType).casFeatCode_annotations), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    