
/* First created by JCasGen Thu Dec 15 23:50:29 CET 2011 */
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Feb 08 15:55:05 CET 2012
 * @generated */
public class TermVariationAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TermVariationAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TermVariationAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TermVariationAnnotation(addr, TermVariationAnnotation_Type.this);
  			   TermVariationAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TermVariationAnnotation(addr, TermVariationAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TermVariationAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TermVariationAnnotation");
 
  /** @generated */
  final Feature casFeat_base;
  /** @generated */
  final int     casFeatCode_base;
  /** @generated */ 
  public int getBase(int addr) {
        if (featOkTst && casFeat_base == null)
      jcas.throwFeatMissing("base", "eu.project.ttc.types.TermVariationAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_base);
  }
  /** @generated */    
  public void setBase(int addr, int v) {
        if (featOkTst && casFeat_base == null)
      jcas.throwFeatMissing("base", "eu.project.ttc.types.TermVariationAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_base, v);}
    
  
 
  /** @generated */
  final Feature casFeat_variant;
  /** @generated */
  final int     casFeatCode_variant;
  /** @generated */ 
  public int getVariant(int addr) {
        if (featOkTst && casFeat_variant == null)
      jcas.throwFeatMissing("variant", "eu.project.ttc.types.TermVariationAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_variant);
  }
  /** @generated */    
  public void setVariant(int addr, int v) {
        if (featOkTst && casFeat_variant == null)
      jcas.throwFeatMissing("variant", "eu.project.ttc.types.TermVariationAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_variant, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermVariationAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_base = jcas.getRequiredFeatureDE(casType, "base", "eu.project.ttc.types.TermAnnotation", featOkTst);
    casFeatCode_base  = (null == casFeat_base) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_base).getCode();

 
    casFeat_variant = jcas.getRequiredFeatureDE(casType, "variant", "eu.project.ttc.types.TermAnnotation", featOkTst);
    casFeatCode_variant  = (null == casFeat_variant) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_variant).getCode();

  }
}



    