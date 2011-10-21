
/* First created by JCasGen Tue Sep 27 11:47:23 CEST 2011 */
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

import org.apache.uima.jcas.cas.AnnotationBase_Type;

/** 
 * Updated by JCasGen Thu Oct 20 14:55:47 CEST 2011
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
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.TermEntryAnnotation");
 
  /** @generated */
  final Feature casFeat_occurrences;
  /** @generated */
  final int     casFeatCode_occurrences;
  /** @generated */ 
  public int getOccurrences(int addr) {
        if (featOkTst && casFeat_occurrences == null)
      jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_occurrences);
  }
  /** @generated */    
  public void setOccurrences(int addr, int v) {
        if (featOkTst && casFeat_occurrences == null)
      jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_occurrences, v);}
    
   /** @generated */
  public int getOccurrences(int addr, int i) {
        if (featOkTst && casFeat_occurrences == null)
      jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_occurrences), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_occurrences), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_occurrences), i);
  }
   
  /** @generated */ 
  public void setOccurrences(int addr, int i, int v) {
        if (featOkTst && casFeat_occurrences == null)
      jcas.throwFeatMissing("occurrences", "org.apache.uima.TermEntryAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_occurrences), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_occurrences), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_occurrences), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_text;
  /** @generated */
  final int     casFeatCode_text;
  /** @generated */ 
  public String getText(int addr) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "org.apache.uima.TermEntryAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_text);
  }
  /** @generated */    
  public void setText(int addr, String v) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "org.apache.uima.TermEntryAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_text, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermEntryAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_occurrences = jcas.getRequiredFeatureDE(casType, "occurrences", "uima.cas.FSArray", featOkTst);
    casFeatCode_occurrences  = (null == casFeat_occurrences) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_occurrences).getCode();

 
    casFeat_text = jcas.getRequiredFeatureDE(casType, "text", "uima.cas.String", featOkTst);
    casFeatCode_text  = (null == casFeat_text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_text).getCode();

  }
}



    