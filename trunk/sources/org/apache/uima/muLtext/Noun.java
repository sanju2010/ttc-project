

/* First created by JCasGen Thu Dec 02 15:44:57 CET 2010 */
package org.apache.uima.muLtext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class Noun extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Noun.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Noun() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Noun(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Noun(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Noun(JCas jcas, int begin, int end) {
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
  //* Feature: nounType

  /** getter for nounType - gets 
   * @generated */
  public String getNounType() {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_nounType == null)
      jcasType.jcas.throwFeatMissing("nounType", "org.apache.uima.mutext.Noun");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Noun_Type)jcasType).casFeatCode_nounType);}
    
  /** setter for nounType - sets  
   * @generated */
  public void setNounType(String v) {
    if (Noun_Type.featOkTst && ((Noun_Type)jcasType).casFeat_nounType == null)
      jcasType.jcas.throwFeatMissing("nounType", "org.apache.uima.mutext.Noun");
    jcasType.ll_cas.ll_setStringValue(addr, ((Noun_Type)jcasType).casFeatCode_nounType, v);}    
  }

    