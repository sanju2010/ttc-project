
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
 * Updated by JCasGen Sat Nov 05 19:54:33 CET 2011
 * @generated */
public class TermEntryAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TermEntryAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TermEntryAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TermEntryAnnotation(addr, TermEntryAnnotation_Type.this);
  			   TermEntryAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TermEntryAnnotation(addr, TermEntryAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TermEntryAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TermEntryAnnotation");
 
  /** @generated */
  final Feature casFeat_frequency;
  /** @generated */
  final int     casFeatCode_frequency;
  /** @generated */ 
  public double getFrequency(int addr) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermEntryAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_frequency);
  }
  /** @generated */    
  public void setFrequency(int addr, double v) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "eu.project.ttc.types.TermEntryAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_frequency, v);}
    
  
 
  /** @generated */
  final Feature casFeat_specificity;
  /** @generated */
  final int     casFeatCode_specificity;
  /** @generated */ 
  public double getSpecificity(int addr) {
        if (featOkTst && casFeat_specificity == null)
      jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermEntryAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_specificity);
  }
  /** @generated */    
  public void setSpecificity(int addr, double v) {
        if (featOkTst && casFeat_specificity == null)
      jcas.throwFeatMissing("specificity", "eu.project.ttc.types.TermEntryAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_specificity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_complexity;
  /** @generated */
  final int     casFeatCode_complexity;
  /** @generated */ 
  public String getComplexity(int addr) {
        if (featOkTst && casFeat_complexity == null)
      jcas.throwFeatMissing("complexity", "eu.project.ttc.types.TermEntryAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_complexity);
  }
  /** @generated */    
  public void setComplexity(int addr, String v) {
        if (featOkTst && casFeat_complexity == null)
      jcas.throwFeatMissing("complexity", "eu.project.ttc.types.TermEntryAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_complexity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_category;
  /** @generated */
  final int     casFeatCode_category;
  /** @generated */ 
  public String getCategory(int addr) {
        if (featOkTst && casFeat_category == null)
      jcas.throwFeatMissing("category", "eu.project.ttc.types.TermEntryAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_category);
  }
  /** @generated */    
  public void setCategory(int addr, String v) {
        if (featOkTst && casFeat_category == null)
      jcas.throwFeatMissing("category", "eu.project.ttc.types.TermEntryAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_category, v);}
    
  
 
  /** @generated */
  final Feature casFeat_term;
  /** @generated */
  final int     casFeatCode_term;
  /** @generated */ 
  public String getTerm(int addr) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "eu.project.ttc.types.TermEntryAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_term);
  }
  /** @generated */    
  public void setTerm(int addr, String v) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "eu.project.ttc.types.TermEntryAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_term, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermEntryAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_frequency = jcas.getRequiredFeatureDE(casType, "frequency", "uima.cas.Double", featOkTst);
    casFeatCode_frequency  = (null == casFeat_frequency) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_frequency).getCode();

 
    casFeat_specificity = jcas.getRequiredFeatureDE(casType, "specificity", "uima.cas.Double", featOkTst);
    casFeatCode_specificity  = (null == casFeat_specificity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_specificity).getCode();

 
    casFeat_complexity = jcas.getRequiredFeatureDE(casType, "complexity", "uima.cas.String", featOkTst);
    casFeatCode_complexity  = (null == casFeat_complexity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_complexity).getCode();

 
    casFeat_category = jcas.getRequiredFeatureDE(casType, "category", "uima.cas.String", featOkTst);
    casFeatCode_category  = (null == casFeat_category) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_category).getCode();

 
    casFeat_term = jcas.getRequiredFeatureDE(casType, "term", "uima.cas.String", featOkTst);
    casFeatCode_term  = (null == casFeat_term) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_term).getCode();

  }
}



    