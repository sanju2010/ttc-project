

/* First created by JCasGen Wed Nov 23 11:46:56 CET 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jan 10 16:01:52 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TermContextAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TermContextAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TermContextAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TermContextAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TermContextAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TermContextAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: context

  /** getter for context - gets 
   * @generated */
  public FSArray getContext() {
    if (TermContextAnnotation_Type.featOkTst && ((TermContextAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_context)));}
    
  /** setter for context - sets  
   * @generated */
  public void setContext(FSArray v) {
    if (TermContextAnnotation_Type.featOkTst && ((TermContextAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_context, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for context - gets an indexed value - 
   * @generated */
  public TermAnnotation getContext(int i) {
    if (TermContextAnnotation_Type.featOkTst && ((TermContextAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_context), i);
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_context), i)));}

  /** indexed setter for context - sets an indexed value - 
   * @generated */
  public void setContext(int i, TermAnnotation v) { 
    if (TermContextAnnotation_Type.featOkTst && ((TermContextAnnotation_Type)jcasType).casFeat_context == null)
      jcasType.jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_context), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_context), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: term

  /** getter for term - gets 
   * @generated */
  public TermAnnotation getTerm() {
    if (TermContextAnnotation_Type.featOkTst && ((TermContextAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextAnnotation");
    return (TermAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_term)));}
    
  /** setter for term - sets  
   * @generated */
  public void setTerm(TermAnnotation v) {
    if (TermContextAnnotation_Type.featOkTst && ((TermContextAnnotation_Type)jcasType).casFeat_term == null)
      jcasType.jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TermContextAnnotation_Type)jcasType).casFeatCode_term, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    