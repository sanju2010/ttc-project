
/* First created by JCasGen Thu Dec 02 15:43:30 CET 2010 */
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Tue Dec 14 21:12:06 CET 2010
 * @generated */
public class MultextAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (MultextAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = MultextAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new MultextAnnotation(addr, MultextAnnotation_Type.this);
  			   MultextAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new MultextAnnotation(addr, MultextAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = MultextAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.MultextAnnotation");
 
  /** @generated */
  final Feature casFeat_tag;
  /** @generated */
  final int     casFeatCode_tag;
  /** @generated */ 
  public String getTag(int addr) {
        if (featOkTst && casFeat_tag == null)
      jcas.throwFeatMissing("tag", "org.apache.uima.MultextAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tag);
  }
  /** @generated */    
  public void setTag(int addr, String v) {
        if (featOkTst && casFeat_tag == null)
      jcas.throwFeatMissing("tag", "org.apache.uima.MultextAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_tag, v);}
    
  
 
  /** @generated */
  final Feature casFeat_orig;
  /** @generated */
  final int     casFeatCode_orig;
  /** @generated */ 
  public int getOrig(int addr) {
        if (featOkTst && casFeat_orig == null)
      jcas.throwFeatMissing("orig", "org.apache.uima.MultextAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_orig);
  }
  /** @generated */    
  public void setOrig(int addr, int v) {
        if (featOkTst && casFeat_orig == null)
      jcas.throwFeatMissing("orig", "org.apache.uima.MultextAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_orig, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public MultextAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tag = jcas.getRequiredFeatureDE(casType, "tag", "uima.cas.String", featOkTst);
    casFeatCode_tag  = (null == casFeat_tag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tag).getCode();

 
    casFeat_orig = jcas.getRequiredFeatureDE(casType, "orig", "uima.tcas.Annotation", featOkTst);
    casFeatCode_orig  = (null == casFeat_orig) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_orig).getCode();

  }
}



    