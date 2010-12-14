

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
public class Adposition extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Adposition.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Adposition() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Adposition(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Adposition(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Adposition(JCas jcas, int begin, int end) {
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
  //* Feature: adpositionType

  /** getter for adpositionType - gets 
   * @generated */
  public String getAdpositionType() {
    if (Adposition_Type.featOkTst && ((Adposition_Type)jcasType).casFeat_adpositionType == null)
      jcasType.jcas.throwFeatMissing("adpositionType", "org.apache.uima.multext.Adposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adposition_Type)jcasType).casFeatCode_adpositionType);}
    
  /** setter for adpositionType - sets  
   * @generated */
  public void setAdpositionType(String v) {
    if (Adposition_Type.featOkTst && ((Adposition_Type)jcasType).casFeat_adpositionType == null)
      jcasType.jcas.throwFeatMissing("adpositionType", "org.apache.uima.multext.Adposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adposition_Type)jcasType).casFeatCode_adpositionType, v);}    
   
    
  //*--------------*
  //* Feature: formation

  /** getter for formation - gets 
   * @generated */
  public String getFormation() {
    if (Adposition_Type.featOkTst && ((Adposition_Type)jcasType).casFeat_formation == null)
      jcasType.jcas.throwFeatMissing("formation", "org.apache.uima.multext.Adposition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Adposition_Type)jcasType).casFeatCode_formation);}
    
  /** setter for formation - sets  
   * @generated */
  public void setFormation(String v) {
    if (Adposition_Type.featOkTst && ((Adposition_Type)jcasType).casFeat_formation == null)
      jcasType.jcas.throwFeatMissing("formation", "org.apache.uima.multext.Adposition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Adposition_Type)jcasType).casFeatCode_formation, v);}    
  }

    