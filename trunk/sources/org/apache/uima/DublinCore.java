

/* First created by JCasGen Thu Dec 02 15:41:06 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Tue Dec 14 21:12:06 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/TTC Type System.xml
 * @generated */
public class DublinCore extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(DublinCore.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected DublinCore() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DublinCore(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DublinCore(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: metadata

  /** getter for metadata - gets 
   * @generated */
  public FSArray getMetadata() {
    if (DublinCore_Type.featOkTst && ((DublinCore_Type)jcasType).casFeat_metadata == null)
      jcasType.jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DublinCore_Type)jcasType).casFeatCode_metadata)));}
    
  /** setter for metadata - sets  
   * @generated */
  public void setMetadata(FSArray v) {
    if (DublinCore_Type.featOkTst && ((DublinCore_Type)jcasType).casFeat_metadata == null)
      jcasType.jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    jcasType.ll_cas.ll_setRefValue(addr, ((DublinCore_Type)jcasType).casFeatCode_metadata, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for metadata - gets an indexed value - 
   * @generated */
  public Property getMetadata(int i) {
    if (DublinCore_Type.featOkTst && ((DublinCore_Type)jcasType).casFeat_metadata == null)
      jcasType.jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DublinCore_Type)jcasType).casFeatCode_metadata), i);
    return (Property)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DublinCore_Type)jcasType).casFeatCode_metadata), i)));}

  /** indexed setter for metadata - sets an indexed value - 
   * @generated */
  public void setMetadata(int i, Property v) { 
    if (DublinCore_Type.featOkTst && ((DublinCore_Type)jcasType).casFeat_metadata == null)
      jcasType.jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DublinCore_Type)jcasType).casFeatCode_metadata), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DublinCore_Type)jcasType).casFeatCode_metadata), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    