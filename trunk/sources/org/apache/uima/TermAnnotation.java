

/* First created by JCasGen Thu Dec 02 15:49:55 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class TermAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: term

  /** getter for term - gets 
   * @generated */
  public Term getTerm() {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "org.apache.uima.TermAnnotation");
    return (Term)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_term)));}
    
  /** setter for term - sets  
   * @generated */
  public void setTerm(Term v) {
    if (TermAnnotation_Type.featOkTst && ((TermAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "org.apache.uima.TermAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermAnnotation_Type)jcasType).casFeatCode_term, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    