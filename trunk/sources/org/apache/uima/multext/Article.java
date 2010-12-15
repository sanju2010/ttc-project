

/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Wed Dec 15 10:23:54 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/models/TTC Document Processor.xml
 * @generated */
public class Article extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Article.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Article() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Article(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Article(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Article(JCas jcas, int begin, int end) {
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
  //* Feature: articleType

  /** getter for articleType - gets 
   * @generated */
  public String getArticleType() {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_articleType == null)
      jcasType.jcas.throwFeatMissing("articleType", "org.apache.uima.multext.Article");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Article_Type)jcasType).casFeatCode_articleType);}
    
  /** setter for articleType - sets  
   * @generated */
  public void setArticleType(String v) {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_articleType == null)
      jcasType.jcas.throwFeatMissing("articleType", "org.apache.uima.multext.Article");
    jcasType.ll_cas.ll_setStringValue(addr, ((Article_Type)jcasType).casFeatCode_articleType, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Article");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Article_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "org.apache.uima.multext.Article");
    jcasType.ll_cas.ll_setStringValue(addr, ((Article_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Article");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Article_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "org.apache.uima.multext.Article");
    jcasType.ll_cas.ll_setStringValue(addr, ((Article_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Article");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Article_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (Article_Type.featOkTst && ((Article_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "org.apache.uima.multext.Article");
    jcasType.ll_cas.ll_setStringValue(addr, ((Article_Type)jcasType).casFeatCode_case, v);}    
  }

    