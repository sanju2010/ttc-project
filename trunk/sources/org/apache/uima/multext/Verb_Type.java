
/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.MultextAnnotation_Type;

/** 
 * Updated by JCasGen Tue Dec 14 21:42:40 CET 2010
 * @generated */
public class Verb_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Verb_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Verb_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Verb(addr, Verb_Type.this);
  			   Verb_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Verb(addr, Verb_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Verb.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Verb");
 
  /** @generated */
  final Feature casFeat_verbType;
  /** @generated */
  final int     casFeatCode_verbType;
  /** @generated */ 
  public String getVerbType(int addr) {
        if (featOkTst && casFeat_verbType == null)
      jcas.throwFeatMissing("verbType", "org.apache.uima.multext.Verb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_verbType);
  }
  /** @generated */    
  public void setVerbType(int addr, String v) {
        if (featOkTst && casFeat_verbType == null)
      jcas.throwFeatMissing("verbType", "org.apache.uima.multext.Verb");
    ll_cas.ll_setStringValue(addr, casFeatCode_verbType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_mood;
  /** @generated */
  final int     casFeatCode_mood;
  /** @generated */ 
  public String getMood(int addr) {
        if (featOkTst && casFeat_mood == null)
      jcas.throwFeatMissing("mood", "org.apache.uima.multext.Verb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_mood);
  }
  /** @generated */    
  public void setMood(int addr, String v) {
        if (featOkTst && casFeat_mood == null)
      jcas.throwFeatMissing("mood", "org.apache.uima.multext.Verb");
    ll_cas.ll_setStringValue(addr, casFeatCode_mood, v);}
    
  
 
  /** @generated */
  final Feature casFeat_tense;
  /** @generated */
  final int     casFeatCode_tense;
  /** @generated */ 
  public String getTense(int addr) {
        if (featOkTst && casFeat_tense == null)
      jcas.throwFeatMissing("tense", "org.apache.uima.multext.Verb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tense);
  }
  /** @generated */    
  public void setTense(int addr, String v) {
        if (featOkTst && casFeat_tense == null)
      jcas.throwFeatMissing("tense", "org.apache.uima.multext.Verb");
    ll_cas.ll_setStringValue(addr, casFeatCode_tense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public String getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.multext.Verb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, String v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.multext.Verb");
    ll_cas.ll_setStringValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_number;
  /** @generated */
  final int     casFeatCode_number;
  /** @generated */ 
  public String getNumber(int addr) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Verb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_number);
  }
  /** @generated */    
  public void setNumber(int addr, String v) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Verb");
    ll_cas.ll_setStringValue(addr, casFeatCode_number, v);}
    
  
 
  /** @generated */
  final Feature casFeat_gender;
  /** @generated */
  final int     casFeatCode_gender;
  /** @generated */ 
  public String getGender(int addr) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Verb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_gender);
  }
  /** @generated */    
  public void setGender(int addr, String v) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Verb");
    ll_cas.ll_setStringValue(addr, casFeatCode_gender, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Verb_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_verbType = jcas.getRequiredFeatureDE(casType, "verbType", "uima.cas.String", featOkTst);
    casFeatCode_verbType  = (null == casFeat_verbType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_verbType).getCode();

 
    casFeat_mood = jcas.getRequiredFeatureDE(casType, "mood", "uima.cas.String", featOkTst);
    casFeatCode_mood  = (null == casFeat_mood) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_mood).getCode();

 
    casFeat_tense = jcas.getRequiredFeatureDE(casType, "tense", "uima.cas.String", featOkTst);
    casFeatCode_tense  = (null == casFeat_tense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tense).getCode();

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "uima.cas.String", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_number = jcas.getRequiredFeatureDE(casType, "number", "uima.cas.String", featOkTst);
    casFeatCode_number  = (null == casFeat_number) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_number).getCode();

 
    casFeat_gender = jcas.getRequiredFeatureDE(casType, "gender", "uima.cas.String", featOkTst);
    casFeatCode_gender  = (null == casFeat_gender) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_gender).getCode();

  }
}



    