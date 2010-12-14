
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
public class Determiner_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Determiner_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Determiner_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Determiner(addr, Determiner_Type.this);
  			   Determiner_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Determiner(addr, Determiner_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Determiner.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Determiner");
 
  /** @generated */
  final Feature casFeat_determinerType;
  /** @generated */
  final int     casFeatCode_determinerType;
  /** @generated */ 
  public String getDeterminerType(int addr) {
        if (featOkTst && casFeat_determinerType == null)
      jcas.throwFeatMissing("determinerType", "org.apache.uima.multext.Determiner");
    return ll_cas.ll_getStringValue(addr, casFeatCode_determinerType);
  }
  /** @generated */    
  public void setDeterminerType(int addr, String v) {
        if (featOkTst && casFeat_determinerType == null)
      jcas.throwFeatMissing("determinerType", "org.apache.uima.multext.Determiner");
    ll_cas.ll_setStringValue(addr, casFeatCode_determinerType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public String getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.multext.Determiner");
    return ll_cas.ll_getStringValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, String v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.multext.Determiner");
    ll_cas.ll_setStringValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_number;
  /** @generated */
  final int     casFeatCode_number;
  /** @generated */ 
  public String getNumber(int addr) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Determiner");
    return ll_cas.ll_getStringValue(addr, casFeatCode_number);
  }
  /** @generated */    
  public void setNumber(int addr, String v) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Determiner");
    ll_cas.ll_setStringValue(addr, casFeatCode_number, v);}
    
  
 
  /** @generated */
  final Feature casFeat_gender;
  /** @generated */
  final int     casFeatCode_gender;
  /** @generated */ 
  public String getGender(int addr) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Determiner");
    return ll_cas.ll_getStringValue(addr, casFeatCode_gender);
  }
  /** @generated */    
  public void setGender(int addr, String v) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Determiner");
    ll_cas.ll_setStringValue(addr, casFeatCode_gender, v);}
    
  
 
  /** @generated */
  final Feature casFeat_case;
  /** @generated */
  final int     casFeatCode_case;
  /** @generated */ 
  public String getCase(int addr) {
        if (featOkTst && casFeat_case == null)
      jcas.throwFeatMissing("case", "org.apache.uima.multext.Determiner");
    return ll_cas.ll_getStringValue(addr, casFeatCode_case);
  }
  /** @generated */    
  public void setCase(int addr, String v) {
        if (featOkTst && casFeat_case == null)
      jcas.throwFeatMissing("case", "org.apache.uima.multext.Determiner");
    ll_cas.ll_setStringValue(addr, casFeatCode_case, v);}
    
  
 
  /** @generated */
  final Feature casFeat_possessor;
  /** @generated */
  final int     casFeatCode_possessor;
  /** @generated */ 
  public String getPossessor(int addr) {
        if (featOkTst && casFeat_possessor == null)
      jcas.throwFeatMissing("possessor", "org.apache.uima.multext.Determiner");
    return ll_cas.ll_getStringValue(addr, casFeatCode_possessor);
  }
  /** @generated */    
  public void setPossessor(int addr, String v) {
        if (featOkTst && casFeat_possessor == null)
      jcas.throwFeatMissing("possessor", "org.apache.uima.multext.Determiner");
    ll_cas.ll_setStringValue(addr, casFeatCode_possessor, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Determiner_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_determinerType = jcas.getRequiredFeatureDE(casType, "determinerType", "uima.cas.String", featOkTst);
    casFeatCode_determinerType  = (null == casFeat_determinerType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_determinerType).getCode();

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "uima.cas.String", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_number = jcas.getRequiredFeatureDE(casType, "number", "uima.cas.String", featOkTst);
    casFeatCode_number  = (null == casFeat_number) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_number).getCode();

 
    casFeat_gender = jcas.getRequiredFeatureDE(casType, "gender", "uima.cas.String", featOkTst);
    casFeatCode_gender  = (null == casFeat_gender) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_gender).getCode();

 
    casFeat_case = jcas.getRequiredFeatureDE(casType, "case", "uima.cas.String", featOkTst);
    casFeatCode_case  = (null == casFeat_case) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_case).getCode();

 
    casFeat_possessor = jcas.getRequiredFeatureDE(casType, "possessor", "uima.cas.String", featOkTst);
    casFeatCode_possessor  = (null == casFeat_possessor) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_possessor).getCode();

  }
}



    