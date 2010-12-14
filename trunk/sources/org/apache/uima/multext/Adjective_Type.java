
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
public class Adjective_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Adjective_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Adjective_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Adjective(addr, Adjective_Type.this);
  			   Adjective_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Adjective(addr, Adjective_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Adjective.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Adjective");
 
  /** @generated */
  final Feature casFeat_adjectiveType;
  /** @generated */
  final int     casFeatCode_adjectiveType;
  /** @generated */ 
  public String getAdjectiveType(int addr) {
        if (featOkTst && casFeat_adjectiveType == null)
      jcas.throwFeatMissing("adjectiveType", "org.apache.uima.multext.Adjective");
    return ll_cas.ll_getStringValue(addr, casFeatCode_adjectiveType);
  }
  /** @generated */    
  public void setAdjectiveType(int addr, String v) {
        if (featOkTst && casFeat_adjectiveType == null)
      jcas.throwFeatMissing("adjectiveType", "org.apache.uima.multext.Adjective");
    ll_cas.ll_setStringValue(addr, casFeatCode_adjectiveType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_degree;
  /** @generated */
  final int     casFeatCode_degree;
  /** @generated */ 
  public String getDegree(int addr) {
        if (featOkTst && casFeat_degree == null)
      jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adjective");
    return ll_cas.ll_getStringValue(addr, casFeatCode_degree);
  }
  /** @generated */    
  public void setDegree(int addr, String v) {
        if (featOkTst && casFeat_degree == null)
      jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adjective");
    ll_cas.ll_setStringValue(addr, casFeatCode_degree, v);}
    
  
 
  /** @generated */
  final Feature casFeat_number;
  /** @generated */
  final int     casFeatCode_number;
  /** @generated */ 
  public String getNumber(int addr) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Adjective");
    return ll_cas.ll_getStringValue(addr, casFeatCode_number);
  }
  /** @generated */    
  public void setNumber(int addr, String v) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Adjective");
    ll_cas.ll_setStringValue(addr, casFeatCode_number, v);}
    
  
 
  /** @generated */
  final Feature casFeat_gender;
  /** @generated */
  final int     casFeatCode_gender;
  /** @generated */ 
  public String getGender(int addr) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Adjective");
    return ll_cas.ll_getStringValue(addr, casFeatCode_gender);
  }
  /** @generated */    
  public void setGender(int addr, String v) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Adjective");
    ll_cas.ll_setStringValue(addr, casFeatCode_gender, v);}
    
  
 
  /** @generated */
  final Feature casFeat_case;
  /** @generated */
  final int     casFeatCode_case;
  /** @generated */ 
  public String getCase(int addr) {
        if (featOkTst && casFeat_case == null)
      jcas.throwFeatMissing("case", "org.apache.uima.multext.Adjective");
    return ll_cas.ll_getStringValue(addr, casFeatCode_case);
  }
  /** @generated */    
  public void setCase(int addr, String v) {
        if (featOkTst && casFeat_case == null)
      jcas.throwFeatMissing("case", "org.apache.uima.multext.Adjective");
    ll_cas.ll_setStringValue(addr, casFeatCode_case, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Adjective_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_adjectiveType = jcas.getRequiredFeatureDE(casType, "adjectiveType", "uima.cas.String", featOkTst);
    casFeatCode_adjectiveType  = (null == casFeat_adjectiveType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_adjectiveType).getCode();

 
    casFeat_degree = jcas.getRequiredFeatureDE(casType, "degree", "uima.cas.String", featOkTst);
    casFeatCode_degree  = (null == casFeat_degree) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_degree).getCode();

 
    casFeat_number = jcas.getRequiredFeatureDE(casType, "number", "uima.cas.String", featOkTst);
    casFeatCode_number  = (null == casFeat_number) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_number).getCode();

 
    casFeat_gender = jcas.getRequiredFeatureDE(casType, "gender", "uima.cas.String", featOkTst);
    casFeatCode_gender  = (null == casFeat_gender) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_gender).getCode();

 
    casFeat_case = jcas.getRequiredFeatureDE(casType, "case", "uima.cas.String", featOkTst);
    casFeatCode_case  = (null == casFeat_case) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_case).getCode();

  }
}



    