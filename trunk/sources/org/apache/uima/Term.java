

/* First created by JCasGen Thu Dec 02 15:48:29 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class Term extends TOP {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Term.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Term() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Term(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Term(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
  //*--------------*
  //* Feature: coveredText

  /** getter for coveredText - gets 
   * @generated */
  public String getCoveredText() {
    if (Term_Type.featOkTst && ((Term_Type)jcasType).casFeat_coveredText == null)
      jcasType.jcas.throwFeatMissing("coveredText", "org.apache.uima.Term");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Term_Type)jcasType).casFeatCode_coveredText);}
    
  /** setter for coveredText - sets  
   * @generated */
  public void setCoveredText(String v) {
    if (Term_Type.featOkTst && ((Term_Type)jcasType).casFeat_coveredText == null)
      jcasType.jcas.throwFeatMissing("coveredText", "org.apache.uima.Term");
    jcasType.ll_cas.ll_setStringValue(addr, ((Term_Type)jcasType).casFeatCode_coveredText, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (Term_Type.featOkTst && ((Term_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "org.apache.uima.Term");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Term_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (Term_Type.featOkTst && ((Term_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "org.apache.uima.Term");
    jcasType.ll_cas.ll_setStringValue(addr, ((Term_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: stem

  /** getter for stem - gets 
   * @generated */
  public String getStem() {
    if (Term_Type.featOkTst && ((Term_Type)jcasType).casFeat_stem == null)
      jcasType.jcas.throwFeatMissing("stem", "org.apache.uima.Term");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Term_Type)jcasType).casFeatCode_stem);}
    
  /** setter for stem - sets  
   * @generated */
  public void setStem(String v) {
    if (Term_Type.featOkTst && ((Term_Type)jcasType).casFeat_stem == null)
      jcasType.jcas.throwFeatMissing("stem", "org.apache.uima.Term");
    jcasType.ll_cas.ll_setStringValue(addr, ((Term_Type)jcasType).casFeatCode_stem, v);}    
  }

    