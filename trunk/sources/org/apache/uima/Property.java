

/* First created by JCasGen Thu Dec 02 15:41:06 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class Property extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Property.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Property() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Property(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Property(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
  //*--------------*
  //* Feature: name

  /** getter for name - gets 
   * @generated */
  public String getName() {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.Property");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Property_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.Property");
    jcasType.ll_cas.ll_setStringValue(addr, ((Property_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: value

  /** getter for value - gets 
   * @generated */
  public String getValue() {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.apache.uima.Property");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Property_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated */
  public void setValue(String v) {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.apache.uima.Property");
    jcasType.ll_cas.ll_setStringValue(addr, ((Property_Type)jcasType).casFeatCode_value, v);}    
   
    
  //*--------------*
  //* Feature: scheme

  /** getter for scheme - gets 
   * @generated */
  public String getScheme() {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_scheme == null)
      jcasType.jcas.throwFeatMissing("scheme", "org.apache.uima.Property");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Property_Type)jcasType).casFeatCode_scheme);}
    
  /** setter for scheme - sets  
   * @generated */
  public void setScheme(String v) {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_scheme == null)
      jcasType.jcas.throwFeatMissing("scheme", "org.apache.uima.Property");
    jcasType.ll_cas.ll_setStringValue(addr, ((Property_Type)jcasType).casFeatCode_scheme, v);}    
   
    
  //*--------------*
  //* Feature: lang

  /** getter for lang - gets 
   * @generated */
  public String getLang() {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_lang == null)
      jcasType.jcas.throwFeatMissing("lang", "org.apache.uima.Property");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Property_Type)jcasType).casFeatCode_lang);}
    
  /** setter for lang - sets  
   * @generated */
  public void setLang(String v) {
    if (Property_Type.featOkTst && ((Property_Type)jcasType).casFeat_lang == null)
      jcasType.jcas.throwFeatMissing("lang", "org.apache.uima.Property");
    jcasType.ll_cas.ll_setStringValue(addr, ((Property_Type)jcasType).casFeatCode_lang, v);}    
  }

    