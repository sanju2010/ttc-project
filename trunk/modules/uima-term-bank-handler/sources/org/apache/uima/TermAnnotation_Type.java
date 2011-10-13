
/* First created by JCasGen Tue Feb 01 14:10:06 CET 2011 */
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
 * Updated by JCasGen Tue Oct 04 09:29:21 CEST 2011
 * @generated */
public class TermAnnotation_Type extends TermComponentAnnotation_Type {
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
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.TermAnnotation");
 
  /** @generated */
  final Feature casFeat_document;
  /** @generated */
  final int     casFeatCode_document;
  /** @generated */ 
  public String getDocument(int addr) {
        if (featOkTst && casFeat_document == null)
      jcas.throwFeatMissing("document", "org.apache.uima.TermAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_document);
  }
  /** @generated */    
  public void setDocument(int addr, String v) {
        if (featOkTst && casFeat_document == null)
      jcas.throwFeatMissing("document", "org.apache.uima.TermAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_document, v);}
    
  
 
  /** @generated */
  final Feature casFeat_complexity;
  /** @generated */
  final int     casFeatCode_complexity;
  /** @generated */ 
  public String getComplexity(int addr) {
        if (featOkTst && casFeat_complexity == null)
      jcas.throwFeatMissing("complexity", "org.apache.uima.TermAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_complexity);
  }
  /** @generated */    
  public void setComplexity(int addr, String v) {
        if (featOkTst && casFeat_complexity == null)
      jcas.throwFeatMissing("complexity", "org.apache.uima.TermAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_complexity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_frequency;
  /** @generated */
  final int     casFeatCode_frequency;
  /** @generated */ 
  public double getFrequency(int addr) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "org.apache.uima.TermAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_frequency);
  }
  /** @generated */    
  public void setFrequency(int addr, double v) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "org.apache.uima.TermAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_frequency, v);}
    
  
 
  /** @generated */
  final Feature casFeat_specificity;
  /** @generated */
  final int     casFeatCode_specificity;
  /** @generated */ 
  public double getSpecificity(int addr) {
        if (featOkTst && casFeat_specificity == null)
      jcas.throwFeatMissing("specificity", "org.apache.uima.TermAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_specificity);
  }
  /** @generated */    
  public void setSpecificity(int addr, double v) {
        if (featOkTst && casFeat_specificity == null)
      jcas.throwFeatMissing("specificity", "org.apache.uima.TermAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_specificity, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_document = jcas.getRequiredFeatureDE(casType, "document", "uima.cas.String", featOkTst);
    casFeatCode_document  = (null == casFeat_document) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_document).getCode();

 
    casFeat_complexity = jcas.getRequiredFeatureDE(casType, "complexity", "uima.cas.String", featOkTst);
    casFeatCode_complexity  = (null == casFeat_complexity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_complexity).getCode();

 
    casFeat_frequency = jcas.getRequiredFeatureDE(casType, "frequency", "uima.cas.Double", featOkTst);
    casFeatCode_frequency  = (null == casFeat_frequency) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_frequency).getCode();

 
    casFeat_specificity = jcas.getRequiredFeatureDE(casType, "specificity", "uima.cas.Double", featOkTst);
    casFeatCode_specificity  = (null == casFeat_specificity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_specificity).getCode();

  }
}



    