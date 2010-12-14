

/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Tue Dec 14 21:42:40 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/TTC Preliminary Linguistic Analysis.xml
 * @generated */
public class Adverb extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Adverb.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Adverb() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Adverb(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Adverb(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Adverb(JCas jcas, int begin, int end) {
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
  //* Feature: adverbType

  /** getter for adverbType - gets 
   * @generated */
  public String getAdverbType() {
    if (Adverb_Type.featOkTst && ((Adverb_Type)jcasType).casFeat_adverbType == null)
      jcasType.jcas.throwFeatMissing("adverbType", "org.apache.uima.multext.Adverb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adverb_Type)jcasType).casFeatCode_adverbType);}
    
  /** setter for adverbType - sets  
   * @generated */
  public void setAdverbType(String v) {
    if (Adverb_Type.featOkTst && ((Adverb_Type)jcasType).casFeat_adverbType == null)
      jcasType.jcas.throwFeatMissing("adverbType", "org.apache.uima.multext.Adverb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adverb_Type)jcasType).casFeatCode_adverbType, v);}    
   
    
  //*--------------*
  //* Feature: degree

  /** getter for degree - gets 
   * @generated */
  public String getDegree() {
    if (Adverb_Type.featOkTst && ((Adverb_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adverb");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adverb_Type)jcasType).casFeatCode_degree);}
    
  /** setter for degree - sets  
   * @generated */
  public void setDegree(String v) {
    if (Adverb_Type.featOkTst && ((Adverb_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adverb");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adverb_Type)jcasType).casFeatCode_degree, v);}    
  }

    