

/* First created by JCasGen Wed Sep 07 11:03:54 CEST 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.DocumentAnnotation;


/** 
 * Updated by JCasGen Wed Sep 28 14:27:30 CEST 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/uima-ziggurat/trunk/resources/fr/univnantes/lina/uima/types/TermContextAnnotation.xml
 * @generated */
public class TermContextIndexAnnotation extends DocumentAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermContextIndexAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermContextIndexAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermContextIndexAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermContextIndexAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermContextIndexAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: name

  /** getter for name - gets 
   * @generated */
  public String getName() {
    if (TermContextIndexAnnotation_Type.featOkTst && ((TermContextIndexAnnotation_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.TermContextIndexAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TermContextIndexAnnotation_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (TermContextIndexAnnotation_Type.featOkTst && ((TermContextIndexAnnotation_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.TermContextIndexAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TermContextIndexAnnotation_Type)jcasType).casFeatCode_name, v);}    
  }

    