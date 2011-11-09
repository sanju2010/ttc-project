

/* First created by JCasGen Tue Oct 25 10:43:56 CEST 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Nov 05 19:54:33 CET 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TermContextVectorAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermContextVectorAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermContextVectorAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermContextVectorAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermContextVectorAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermContextVectorAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: contextVector

  /** getter for contextVector - gets 
   * @generated */
  public FSArray getContextVector() {
    if (TermContextVectorAnnotation_Type.featOkTst && ((TermContextVectorAnnotation_Type)jcasType).casFeat_contextVector == null)
      jcasType.jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_contextVector)));}
    
  /** setter for contextVector - sets  
   * @generated */
  public void setContextVector(FSArray v) {
    if (TermContextVectorAnnotation_Type.featOkTst && ((TermContextVectorAnnotation_Type)jcasType).casFeat_contextVector == null)
      jcasType.jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_contextVector, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for contextVector - gets an indexed value - 
   * @generated */
  public TermContextEntryAnnotation getContextVector(int i) {
    if (TermContextVectorAnnotation_Type.featOkTst && ((TermContextVectorAnnotation_Type)jcasType).casFeat_contextVector == null)
      jcasType.jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_contextVector), i);
    return (TermContextEntryAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_contextVector), i)));}

  /** indexed setter for contextVector - sets an indexed value - 
   * @generated */
  public void setContextVector(int i, TermContextEntryAnnotation v) { 
    if (TermContextVectorAnnotation_Type.featOkTst && ((TermContextVectorAnnotation_Type)jcasType).casFeat_contextVector == null)
      jcasType.jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_contextVector), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_contextVector), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: term

  /** getter for term - gets 
   * @generated */
  public TermContextEntryAnnotation getTerm() {
    if (TermContextVectorAnnotation_Type.featOkTst && ((TermContextVectorAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextVectorAnnotation");
    return (TermContextEntryAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_term)));}
    
  /** setter for term - sets  
   * @generated */
  public void setTerm(TermContextEntryAnnotation v) {
    if (TermContextVectorAnnotation_Type.featOkTst && ((TermContextVectorAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextVectorAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermContextVectorAnnotation_Type)jcasType).casFeatCode_term, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    