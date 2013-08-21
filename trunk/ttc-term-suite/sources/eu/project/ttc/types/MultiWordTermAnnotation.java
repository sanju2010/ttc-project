

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



import org.apache.uima.jcas.cas.FSArray;


/** 
 * Updated by JCasGen Thu Mar 01 23:11:13 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class MultiWordTermAnnotation extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MultiWordTermAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected MultiWordTermAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MultiWordTermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MultiWordTermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MultiWordTermAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
  //*--------------*
  //* Feature: components

  /** getter for components - gets 
   * @generated */
  public FSArray getComponents() {
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components)));}
    
  /** setter for components - sets  
   * @generated */
  public void setComponents(FSArray v) {
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for components - gets an indexed value - 
   * @generated */
  public TermComponentAnnotation getComponents(int i) {
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i);
    return (TermComponentAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i)));}

  /** indexed setter for components - sets an indexed value - 
   * @generated */
  public void setComponents(int i, TermComponentAnnotation v) { 
    if (MultiWordTermAnnotation_Type.featOkTst && ((MultiWordTermAnnotation_Type)jcasType).casFeat_components == null)
      jcasType.jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((MultiWordTermAnnotation_Type)jcasType).casFeatCode_components), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    