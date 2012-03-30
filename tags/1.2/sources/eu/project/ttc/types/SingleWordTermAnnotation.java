

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Thu Mar 01 23:11:13 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class SingleWordTermAnnotation extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SingleWordTermAnnotation.class);
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
  protected SingleWordTermAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SingleWordTermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SingleWordTermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SingleWordTermAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: compound

  /** getter for compound - gets 
   * @generated */
  public boolean getCompound() {
    if (SingleWordTermAnnotation_Type.featOkTst && ((SingleWordTermAnnotation_Type)jcasType).casFeat_compound == null)
      jcasType.jcas.throwFeatMissing("compound", "eu.project.ttc.types.SingleWordTermAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((SingleWordTermAnnotation_Type)jcasType).casFeatCode_compound);}
    
  /** setter for compound - sets  
   * @generated */
  public void setCompound(boolean v) {
    if (SingleWordTermAnnotation_Type.featOkTst && ((SingleWordTermAnnotation_Type)jcasType).casFeat_compound == null)
      jcasType.jcas.throwFeatMissing("compound", "eu.project.ttc.types.SingleWordTermAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((SingleWordTermAnnotation_Type)jcasType).casFeatCode_compound, v);}    
   
    
  //*--------------*
  //* Feature: neoclassical

  /** getter for neoclassical - gets 
   * @generated */
  public boolean getNeoclassical() {
    if (SingleWordTermAnnotation_Type.featOkTst && ((SingleWordTermAnnotation_Type)jcasType).casFeat_neoclassical == null)
      jcasType.jcas.throwFeatMissing("neoclassical", "eu.project.ttc.types.SingleWordTermAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((SingleWordTermAnnotation_Type)jcasType).casFeatCode_neoclassical);}
    
  /** setter for neoclassical - sets  
   * @generated */
  public void setNeoclassical(boolean v) {
    if (SingleWordTermAnnotation_Type.featOkTst && ((SingleWordTermAnnotation_Type)jcasType).casFeat_neoclassical == null)
      jcasType.jcas.throwFeatMissing("neoclassical", "eu.project.ttc.types.SingleWordTermAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((SingleWordTermAnnotation_Type)jcasType).casFeatCode_neoclassical, v);}    
  }

    