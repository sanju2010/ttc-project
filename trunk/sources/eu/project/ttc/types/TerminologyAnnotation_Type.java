
/* First created by JCasGen Fri Oct 21 11:26:24 CEST 2011 */
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
 * Updated by JCasGen Wed Nov 02 16:52:00 CET 2011
 * @generated */
public class TerminologyAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TerminologyAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TerminologyAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TerminologyAnnotation(addr, TerminologyAnnotation_Type.this);
  			   TerminologyAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TerminologyAnnotation(addr, TerminologyAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TerminologyAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TerminologyAnnotation");
 
  /** @generated */
  final Feature casFeat_entries;
  /** @generated */
  final int     casFeatCode_entries;
  /** @generated */ 
  public int getEntries(int addr) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_entries);
  }
  /** @generated */    
  public void setEntries(int addr, int v) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_entries, v);}
    
   /** @generated */
  public int getEntries(int addr, int i) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i);
  }
   
  /** @generated */ 
  public void setEntries(int addr, int i, int v) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "eu.project.ttc.types.TerminologyAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_language;
  /** @generated */
  final int     casFeatCode_language;
  /** @generated */ 
  public String getLanguage(int addr) {
        if (featOkTst && casFeat_language == null)
      jcas.throwFeatMissing("language", "eu.project.ttc.types.TerminologyAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_language);
  }
  /** @generated */    
  public void setLanguage(int addr, String v) {
        if (featOkTst && casFeat_language == null)
      jcas.throwFeatMissing("language", "eu.project.ttc.types.TerminologyAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_language, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TerminologyAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_entries = jcas.getRequiredFeatureDE(casType, "entries", "uima.cas.FSArray", featOkTst);
    casFeatCode_entries  = (null == casFeat_entries) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_entries).getCode();

 
    casFeat_language = jcas.getRequiredFeatureDE(casType, "language", "uima.cas.String", featOkTst);
    casFeatCode_language  = (null == casFeat_language) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_language).getCode();

  }
}



    