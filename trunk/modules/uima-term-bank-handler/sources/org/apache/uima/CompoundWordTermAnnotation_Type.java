
/* First created by JCasGen Fri Sep 16 20:21:04 CEST 2011 */
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

/** 
 * Updated by JCasGen Thu Oct 20 14:55:47 CEST 2011
 * @generated */
public class CompoundWordTermAnnotation_Type extends TermAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompoundWordTermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompoundWordTermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompoundWordTermAnnotation(addr, CompoundWordTermAnnotation_Type.this);
  			   CompoundWordTermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompoundWordTermAnnotation(addr, CompoundWordTermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompoundWordTermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.CompoundWordTermAnnotation");
 
  /** @generated */
  final Feature casFeat_components;
  /** @generated */
  final int     casFeatCode_components;
  /** @generated */ 
  public int getComponents(int addr) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "org.apache.uima.CompoundWordTermAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_components);
  }
  /** @generated */    
  public void setComponents(int addr, int v) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "org.apache.uima.CompoundWordTermAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_components, v);}
    
   /** @generated */
  public int getComponents(int addr, int i) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "org.apache.uima.CompoundWordTermAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_components), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i);
  }
   
  /** @generated */ 
  public void setComponents(int addr, int i, int v) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "org.apache.uima.CompoundWordTermAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_components), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompoundWordTermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_components = jcas.getRequiredFeatureDE(casType, "components", "uima.cas.FSArray", featOkTst);
    casFeatCode_components  = (null == casFeat_components) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_components).getCode();

  }
}



    