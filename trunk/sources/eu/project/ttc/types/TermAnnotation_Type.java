
/* First created by JCasGen Thu Oct 20 17:03:42 CEST 2011 */
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
public class TermAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TermAnnotation(addr, TermAnnotation_Type.this);
  			   TermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TermAnnotation(addr, TermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TermAnnotation");
 
  /** @generated */
  final Feature casFeat_complexity;
  /** @generated */
  final int     casFeatCode_complexity;
  /** @generated */ 
  public String getComplexity(int addr) {
        if (featOkTst && casFeat_complexity == null)
      jcas.throwFeatMissing("complexity", "eu.project.ttc.types.TermAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_complexity);
  }
  /** @generated */    
  public void setComplexity(int addr, String v) {
        if (featOkTst && casFeat_complexity == null)
      jcas.throwFeatMissing("complexity", "eu.project.ttc.types.TermAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_complexity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_frequency;
  /** @generated */
  final int     casFeatCode_frequency;
  /** @generated */ 
  public double getFrequency(int addr) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_frequency);
  }
  /** @generated */    
  public void setFrequency(int addr, double v) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_frequency, v);}
    
  
 
  /** @generated */
  final Feature casFeat_specificity;
  /** @generated */
  final int     casFeatCode_specificity;
  /** @generated */ 
  public double getSpecificity(int addr) {
        if (featOkTst && casFeat_specificity == null)
      jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_specificity);
  }
  /** @generated */    
  public void setSpecificity(int addr, double v) {
        if (featOkTst && casFeat_specificity == null)
      jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_specificity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_category;
  /** @generated */
  final int     casFeatCode_category;
  /** @generated */ 
  public String getCategory(int addr) {
        if (featOkTst && casFeat_category == null)
      jcas.throwFeatMissing("category", "eu.project.ttc.types.TermAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_category);
  }
  /** @generated */    
  public void setCategory(int addr, String v) {
        if (featOkTst && casFeat_category == null)
      jcas.throwFeatMissing("category", "eu.project.ttc.types.TermAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_category, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lemma;
  /** @generated */
  final int     casFeatCode_lemma;
  /** @generated */ 
  public String getLemma(int addr) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "eu.project.ttc.types.TermAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lemma);
  }
  /** @generated */    
  public void setLemma(int addr, String v) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "eu.project.ttc.types.TermAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_lemma, v);}
    
  
 
  /** @generated */
  final Feature casFeat_variants;
  /** @generated */
  final int     casFeatCode_variants;
  /** @generated */ 
  public int getVariants(int addr) {
        if (featOkTst && casFeat_variants == null)
      jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_variants);
  }
  /** @generated */    
  public void setVariants(int addr, int v) {
        if (featOkTst && casFeat_variants == null)
      jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_variants, v);}
    
   /** @generated */
  public int getVariants(int addr, int i) {
        if (featOkTst && casFeat_variants == null)
      jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_variants), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_variants), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_variants), i);
  }
   
  /** @generated */ 
  public void setVariants(int addr, int i, int v) {
        if (featOkTst && casFeat_variants == null)
      jcas.throwFeatMissing("variants", "eu.project.ttc.types.TermAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_variants), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_variants), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_variants), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_complexity = jcas.getRequiredFeatureDE(casType, "complexity", "uima.cas.String", featOkTst);
    casFeatCode_complexity  = (null == casFeat_complexity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_complexity).getCode();

 
    casFeat_frequency = jcas.getRequiredFeatureDE(casType, "frequency", "uima.cas.Double", featOkTst);
    casFeatCode_frequency  = (null == casFeat_frequency) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_frequency).getCode();

 
    casFeat_specificity = jcas.getRequiredFeatureDE(casType, "specificity", "uima.cas.Double", featOkTst);
    casFeatCode_specificity  = (null == casFeat_specificity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_specificity).getCode();

 
    casFeat_category = jcas.getRequiredFeatureDE(casType, "category", "uima.cas.String", featOkTst);
    casFeatCode_category  = (null == casFeat_category) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_category).getCode();

 
    casFeat_lemma = jcas.getRequiredFeatureDE(casType, "lemma", "uima.cas.String", featOkTst);
    casFeatCode_lemma  = (null == casFeat_lemma) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lemma).getCode();

 
    casFeat_variants = jcas.getRequiredFeatureDE(casType, "variants", "uima.cas.FSArray", featOkTst);
    casFeatCode_variants  = (null == casFeat_variants) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_variants).getCode();

  }
}



    