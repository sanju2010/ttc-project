

/* First created by JCasGen Fri Jul 20 14:01:15 CEST 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Represents a form of a given term.
 * Updated by JCasGen Mon Jul 23 14:49:38 CEST 2012
 * XML source: /home/pena-s/workspace/ttc-project/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class FormAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(FormAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected FormAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public FormAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public FormAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public FormAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: form

  /** getter for form - gets The from as a string.
   * @generated */
  public String getForm() {
    if (FormAnnotation_Type.featOkTst && ((FormAnnotation_Type)jcasType).casFeat_form == null)
      jcasType.jcas.throwFeatMissing("form", "eu.project.ttc.types.FormAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FormAnnotation_Type)jcasType).casFeatCode_form);}
    
  /** setter for form - sets The from as a string. 
   * @generated */
  public void setForm(String v) {
    if (FormAnnotation_Type.featOkTst && ((FormAnnotation_Type)jcasType).casFeat_form == null)
      jcasType.jcas.throwFeatMissing("form", "eu.project.ttc.types.FormAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((FormAnnotation_Type)jcasType).casFeatCode_form, v);}    
   
    
  //*--------------*
  //* Feature: occurrences

  /** getter for occurrences - gets The occurrences of the form.
   * @generated */
  public int getOccurrences() {
    if (FormAnnotation_Type.featOkTst && ((FormAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "eu.project.ttc.types.FormAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((FormAnnotation_Type)jcasType).casFeatCode_occurrences);}
    
  /** setter for occurrences - sets The occurrences of the form. 
   * @generated */
  public void setOccurrences(int v) {
    if (FormAnnotation_Type.featOkTst && ((FormAnnotation_Type)jcasType).casFeat_occurrences == null)
      jcasType.jcas.throwFeatMissing("occurrences", "eu.project.ttc.types.FormAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((FormAnnotation_Type)jcasType).casFeatCode_occurrences, v);}    
  }

    