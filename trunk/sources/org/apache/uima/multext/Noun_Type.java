
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
public class Noun_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Noun_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Noun_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Noun(addr, Noun_Type.this);
  			   Noun_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Noun(addr, Noun_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Noun.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Noun");
 
  /** @generated */
  final Feature casFeat_nounType;
  /** @generated */
  final int     casFeatCode_nounType;
  /** @generated */ 
  public String getNounType(int addr) {
        if (featOkTst && casFeat_nounType == null)
      jcas.throwFeatMissing("nounType", "org.apache.uima.multext.Noun");
    return ll_cas.ll_getStringValue(addr, casFeatCode_nounType);
  }
  /** @generated */    
  public void setNounType(int addr, String v) {
        if (featOkTst && casFeat_nounType == null)
      jcas.throwFeatMissing("nounType", "org.apache.uima.multext.Noun");
    ll_cas.ll_setStringValue(addr, casFeatCode_nounType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_gender;
  /** @generated */
  final int     casFeatCode_gender;
  /** @generated */ 
  public String getGender(int addr) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Noun");
    return ll_cas.ll_getStringValue(addr, casFeatCode_gender);
  }
  /** @generated */    
  public void setGender(int addr, String v) {
        if (featOkTst && casFeat_gender == null)
      jcas.throwFeatMissing("gender", "org.apache.uima.multext.Noun");
    ll_cas.ll_setStringValue(addr, casFeatCode_gender, v);}
    
  
 
  /** @generated */
  final Feature casFeat_number;
  /** @generated */
  final int     casFeatCode_number;
  /** @generated */ 
  public String getNumber(int addr) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Noun");
    return ll_cas.ll_getStringValue(addr, casFeatCode_number);
  }
  /** @generated */    
  public void setNumber(int addr, String v) {
        if (featOkTst && casFeat_number == null)
      jcas.throwFeatMissing("number", "org.apache.uima.multext.Noun");
    ll_cas.ll_setStringValue(addr, casFeatCode_number, v);}
    
  
 
  /** @generated */
  final Feature casFeat_case;
  /** @generated */
  final int     casFeatCode_case;
  /** @generated */ 
  public String getCase(int addr) {
        if (featOkTst && casFeat_case == null)
      jcas.throwFeatMissing("case", "org.apache.uima.multext.Noun");
    return ll_cas.ll_getStringValue(addr, casFeatCode_case);
  }
  /** @generated */    
  public void setCase(int addr, String v) {
        if (featOkTst && casFeat_case == null)
      jcas.throwFeatMissing("case", "org.apache.uima.multext.Noun");
    ll_cas.ll_setStringValue(addr, casFeatCode_case, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Noun_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_nounType = jcas.getRequiredFeatureDE(casType, "nounType", "uima.cas.String", featOkTst);
    casFeatCode_nounType  = (null == casFeat_nounType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_nounType).getCode();

 
    casFeat_gender = jcas.getRequiredFeatureDE(casType, "gender", "uima.cas.String", featOkTst);
    casFeatCode_gender  = (null == casFeat_gender) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_gender).getCode();

 
    casFeat_number = jcas.getRequiredFeatureDE(casType, "number", "uima.cas.String", featOkTst);
    casFeatCode_number  = (null == casFeat_number) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_number).getCode();

 
    casFeat_case = jcas.getRequiredFeatureDE(casType, "case", "uima.cas.String", featOkTst);
    casFeatCode_case  = (null == casFeat_case) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_case).getCode();

  }
}



    