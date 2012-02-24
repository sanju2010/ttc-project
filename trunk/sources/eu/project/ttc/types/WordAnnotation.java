

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Feb 24 14:28:15 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class WordAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(WordAnnotation.class);
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
  protected WordAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WordAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WordAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WordAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: tag

  /** getter for tag - gets 
   * @generated */
  public String getTag() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_tag);}
    
  /** setter for tag - sets  
   * @generated */
  public void setTag(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_tag == null)
      jcasType.jcas.throwFeatMissing("tag", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_tag, v);}    
   
    
  //*--------------*
  //* Feature: category

  /** getter for category - gets 
   * @generated */
  public String getCategory() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_category);}
    
  /** setter for category - sets  
   * @generated */
  public void setCategory(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_category, v);}    
   
    
  //*--------------*
  //* Feature: subCategory

  /** getter for subCategory - gets 
   * @generated */
  public String getSubCategory() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_subCategory == null)
      jcasType.jcas.throwFeatMissing("subCategory", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_subCategory);}
    
  /** setter for subCategory - sets  
   * @generated */
  public void setSubCategory(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_subCategory == null)
      jcasType.jcas.throwFeatMissing("subCategory", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_subCategory, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: stem

  /** getter for stem - gets 
   * @generated */
  public String getStem() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_stem == null)
      jcasType.jcas.throwFeatMissing("stem", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_stem);}
    
  /** setter for stem - sets  
   * @generated */
  public void setStem(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_stem == null)
      jcasType.jcas.throwFeatMissing("stem", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_stem, v);}    
   
    
  //*--------------*
  //* Feature: number

  /** getter for number - gets 
   * @generated */
  public String getNumber() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_number);}
    
  /** setter for number - sets  
   * @generated */
  public void setNumber(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_number == null)
      jcasType.jcas.throwFeatMissing("number", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_number, v);}    
   
    
  //*--------------*
  //* Feature: gender

  /** getter for gender - gets 
   * @generated */
  public String getGender() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_gender);}
    
  /** setter for gender - sets  
   * @generated */
  public void setGender(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_gender == null)
      jcasType.jcas.throwFeatMissing("gender", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_gender, v);}    
   
    
  //*--------------*
  //* Feature: case

  /** getter for case - gets 
   * @generated */
  public String getCase() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_case);}
    
  /** setter for case - sets  
   * @generated */
  public void setCase(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_case == null)
      jcasType.jcas.throwFeatMissing("case", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_case, v);}    
   
    
  //*--------------*
  //* Feature: mood

  /** getter for mood - gets 
   * @generated */
  public String getMood() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_mood == null)
      jcasType.jcas.throwFeatMissing("mood", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_mood);}
    
  /** setter for mood - sets  
   * @generated */
  public void setMood(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_mood == null)
      jcasType.jcas.throwFeatMissing("mood", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_mood, v);}    
   
    
  //*--------------*
  //* Feature: tense

  /** getter for tense - gets 
   * @generated */
  public String getTense() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_tense == null)
      jcasType.jcas.throwFeatMissing("tense", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_tense);}
    
  /** setter for tense - sets  
   * @generated */
  public void setTense(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_tense == null)
      jcasType.jcas.throwFeatMissing("tense", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_tense, v);}    
   
    
  //*--------------*
  //* Feature: person

  /** getter for person - gets 
   * @generated */
  public String getPerson() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_person);}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_person, v);}    
   
    
  //*--------------*
  //* Feature: possessor

  /** getter for possessor - gets 
   * @generated */
  public String getPossessor() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_possessor == null)
      jcasType.jcas.throwFeatMissing("possessor", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_possessor);}
    
  /** setter for possessor - sets  
   * @generated */
  public void setPossessor(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_possessor == null)
      jcasType.jcas.throwFeatMissing("possessor", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_possessor, v);}    
   
    
  //*--------------*
  //* Feature: degree

  /** getter for degree - gets 
   * @generated */
  public String getDegree() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_degree);}
    
  /** setter for degree - sets  
   * @generated */
  public void setDegree(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_degree, v);}    
   
    
  //*--------------*
  //* Feature: formation

  /** getter for formation - gets 
   * @generated */
  public String getFormation() {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_formation == null)
      jcasType.jcas.throwFeatMissing("formation", "eu.project.ttc.types.WordAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_formation);}
    
  /** setter for formation - sets  
   * @generated */
  public void setFormation(String v) {
    if (WordAnnotation_Type.featOkTst && ((WordAnnotation_Type)jcasType).casFeat_formation == null)
      jcasType.jcas.throwFeatMissing("formation", "eu.project.ttc.types.WordAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WordAnnotation_Type)jcasType).casFeatCode_formation, v);}    
  }

    