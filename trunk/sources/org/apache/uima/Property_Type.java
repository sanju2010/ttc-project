
/* First created by JCasGen Thu Dec 02 15:41:06 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.AnnotationBase_Type;

/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * @generated */
public class Property_Type extends AnnotationBase_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Property_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Property_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Property(addr, Property_Type.this);
  			   Property_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Property(addr, Property_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Property.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.Property");



  /** @generated */
  final Feature casFeat_name;
  /** @generated */
  final int     casFeatCode_name;
  /** @generated */ 
  public String getName(int addr) {
        if (featOkTst && casFeat_name == null)
      jcas.throwFeatMissing("name", "org.apache.uima.Property");
    return ll_cas.ll_getStringValue(addr, casFeatCode_name);
  }
  /** @generated */    
  public void setName(int addr, String v) {
        if (featOkTst && casFeat_name == null)
      jcas.throwFeatMissing("name", "org.apache.uima.Property");
    ll_cas.ll_setStringValue(addr, casFeatCode_name, v);}
    
  
 
  /** @generated */
  final Feature casFeat_value;
  /** @generated */
  final int     casFeatCode_value;
  /** @generated */ 
  public String getValue(int addr) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "org.apache.uima.Property");
    return ll_cas.ll_getStringValue(addr, casFeatCode_value);
  }
  /** @generated */    
  public void setValue(int addr, String v) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "org.apache.uima.Property");
    ll_cas.ll_setStringValue(addr, casFeatCode_value, v);}
    
  
 
  /** @generated */
  final Feature casFeat_scheme;
  /** @generated */
  final int     casFeatCode_scheme;
  /** @generated */ 
  public String getScheme(int addr) {
        if (featOkTst && casFeat_scheme == null)
      jcas.throwFeatMissing("scheme", "org.apache.uima.Property");
    return ll_cas.ll_getStringValue(addr, casFeatCode_scheme);
  }
  /** @generated */    
  public void setScheme(int addr, String v) {
        if (featOkTst && casFeat_scheme == null)
      jcas.throwFeatMissing("scheme", "org.apache.uima.Property");
    ll_cas.ll_setStringValue(addr, casFeatCode_scheme, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lang;
  /** @generated */
  final int     casFeatCode_lang;
  /** @generated */ 
  public String getLang(int addr) {
        if (featOkTst && casFeat_lang == null)
      jcas.throwFeatMissing("lang", "org.apache.uima.Property");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lang);
  }
  /** @generated */    
  public void setLang(int addr, String v) {
        if (featOkTst && casFeat_lang == null)
      jcas.throwFeatMissing("lang", "org.apache.uima.Property");
    ll_cas.ll_setStringValue(addr, casFeatCode_lang, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Property_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_name = jcas.getRequiredFeatureDE(casType, "name", "uima.cas.String", featOkTst);
    casFeatCode_name  = (null == casFeat_name) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_name).getCode();

 
    casFeat_value = jcas.getRequiredFeatureDE(casType, "value", "uima.cas.String", featOkTst);
    casFeatCode_value  = (null == casFeat_value) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_value).getCode();

 
    casFeat_scheme = jcas.getRequiredFeatureDE(casType, "scheme", "uima.cas.String", featOkTst);
    casFeatCode_scheme  = (null == casFeat_scheme) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_scheme).getCode();

 
    casFeat_lang = jcas.getRequiredFeatureDE(casType, "lang", "uima.cas.String", featOkTst);
    casFeatCode_lang  = (null == casFeat_lang) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lang).getCode();

  }
}



    