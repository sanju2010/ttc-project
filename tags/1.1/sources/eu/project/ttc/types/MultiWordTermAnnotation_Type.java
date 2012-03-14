
/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

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
 * Updated by JCasGen Thu Mar 01 23:11:13 CET 2012
 * @generated */
public class MultiWordTermAnnotation_Type extends TermAnnotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (MultiWordTermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = MultiWordTermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new MultiWordTermAnnotation(addr, MultiWordTermAnnotation_Type.this);
  			   MultiWordTermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new MultiWordTermAnnotation(addr, MultiWordTermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = MultiWordTermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.MultiWordTermAnnotation");



  /** @generated */
  final Feature casFeat_components;
  /** @generated */
  final int     casFeatCode_components;
  /** @generated */ 
  public int getComponents(int addr) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_components);
  }
  /** @generated */    
  public void setComponents(int addr, int v) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_components, v);}
    
   /** @generated */
  public int getComponents(int addr, int i) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_components), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i);
  }
   
  /** @generated */ 
  public void setComponents(int addr, int i, int v) {
        if (featOkTst && casFeat_components == null)
      jcas.throwFeatMissing("components", "eu.project.ttc.types.MultiWordTermAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_components), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_components), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public MultiWordTermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_components = jcas.getRequiredFeatureDE(casType, "components", "uima.cas.FSArray", featOkTst);
    casFeatCode_components  = (null == casFeat_components) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_components).getCode();

  }
}



    