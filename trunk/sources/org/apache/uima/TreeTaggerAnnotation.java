

/* First created by JCasGen Thu Dec 02 15:42:54 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Dec 14 21:12:06 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/TTC Type System.xml
 * @generated */
public class TreeTaggerAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TreeTaggerAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TreeTaggerAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TreeTaggerAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TreeTaggerAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TreeTaggerAnnotation(JCas jcas, int begin, int end) {
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
    if (TreeTaggerAnnotation_Type.featOkTst && ((TreeTaggerAnnotation_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "org.apache.uima.TreeTaggerAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TreeTaggerAnnotation_Type)jcasType).casFeatCode_tag);}
    
  /** setter for tag - sets  
   * @generated */
  public void setTag(String v) {
    if (TreeTaggerAnnotation_Type.featOkTst && ((TreeTaggerAnnotation_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "org.apache.uima.TreeTaggerAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TreeTaggerAnnotation_Type)jcasType).casFeatCode_tag, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (TreeTaggerAnnotation_Type.featOkTst && ((TreeTaggerAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "org.apache.uima.TreeTaggerAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TreeTaggerAnnotation_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (TreeTaggerAnnotation_Type.featOkTst && ((TreeTaggerAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "org.apache.uima.TreeTaggerAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TreeTaggerAnnotation_Type)jcasType).casFeatCode_lemma, v);}    
  }

    