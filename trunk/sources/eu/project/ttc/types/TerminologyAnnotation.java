

/* First created by JCasGen Fri Oct 21 11:26:24 CEST 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Nov 02 16:52:00 CET 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class TerminologyAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TerminologyAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TerminologyAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TerminologyAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TerminologyAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TerminologyAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: entries

  /** getter for entries - gets 
   * @generated */
  public FSArray getEntries() {
    if (TerminologyAnnotation_Type.featOkTst && ((TerminologyAnnotation_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_entries)));}
    
  /** setter for entries - sets  
   * @generated */
  public void setEntries(FSArray v) {
    if (TerminologyAnnotation_Type.featOkTst && ((TerminologyAnnotation_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_entries, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for entries - gets an indexed value - 
   * @generated */
  public TermEntryAnnotation getEntries(int i) {
    if (TerminologyAnnotation_Type.featOkTst && ((TerminologyAnnotation_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_entries), i);
    return (TermEntryAnnotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_entries), i)));}

  /** indexed setter for entries - sets an indexed value - 
   * @generated */
  public void setEntries(int i, TermEntryAnnotation v) { 
    if (TerminologyAnnotation_Type.featOkTst && ((TerminologyAnnotation_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_entries), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_entries), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: language

  /** getter for language - gets 
   * @generated */
  public String getLanguage() {
    if (TerminologyAnnotation_Type.featOkTst && ((TerminologyAnnotation_Type)jcasType).casFeat_language == null)
      jcasType.jcas.throwFeatMissing("language", "eu.project.ttc.types.TerminologyAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_language);}
    
  /** setter for language - sets  
   * @generated */
  public void setLanguage(String v) {
    if (TerminologyAnnotation_Type.featOkTst && ((TerminologyAnnotation_Type)jcasType).casFeat_language == null)
      jcasType.jcas.throwFeatMissing("language", "eu.project.ttc.types.TerminologyAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TerminologyAnnotation_Type)jcasType).casFeatCode_language, v);}    
  }

    