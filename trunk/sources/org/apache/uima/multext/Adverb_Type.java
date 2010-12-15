
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
 * Updated by JCasGen Wed Dec 15 10:23:54 CET 2010
 * @generated */
public class Adverb_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Adverb_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Adverb_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Adverb(addr, Adverb_Type.this);
  			   Adverb_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Adverb(addr, Adverb_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Adverb.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Adverb");
 
  /** @generated */
  final Feature casFeat_adverbType;
  /** @generated */
  final int     casFeatCode_adverbType;
  /** @generated */ 
  public String getAdverbType(int addr) {
        if (featOkTst && casFeat_adverbType == null)
      jcas.throwFeatMissing("adverbType", "org.apache.uima.multext.Adverb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_adverbType);
  }
  /** @generated */    
  public void setAdverbType(int addr, String v) {
        if (featOkTst && casFeat_adverbType == null)
      jcas.throwFeatMissing("adverbType", "org.apache.uima.multext.Adverb");
    ll_cas.ll_setStringValue(addr, casFeatCode_adverbType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_degree;
  /** @generated */
  final int     casFeatCode_degree;
  /** @generated */ 
  public String getDegree(int addr) {
        if (featOkTst && casFeat_degree == null)
      jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adverb");
    return ll_cas.ll_getStringValue(addr, casFeatCode_degree);
  }
  /** @generated */    
  public void setDegree(int addr, String v) {
        if (featOkTst && casFeat_degree == null)
      jcas.throwFeatMissing("degree", "org.apache.uima.multext.Adverb");
    ll_cas.ll_setStringValue(addr, casFeatCode_degree, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Adverb_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_adverbType = jcas.getRequiredFeatureDE(casType, "adverbType", "uima.cas.String", featOkTst);
    casFeatCode_adverbType  = (null == casFeat_adverbType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_adverbType).getCode();

 
    casFeat_degree = jcas.getRequiredFeatureDE(casType, "degree", "uima.cas.String", featOkTst);
    casFeatCode_degree  = (null == casFeat_degree) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_degree).getCode();

  }
}



    