
/* First created by JCasGen Thu Dec 02 15:41:06 CET 2010 */
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
import org.apache.uima.jcas.cas.AnnotationBase_Type;

/** 
 * Updated by JCasGen Tue Dec 14 21:12:06 CET 2010
 * @generated */
public class DublinCore_Type extends AnnotationBase_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DublinCore_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DublinCore_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DublinCore(addr, DublinCore_Type.this);
  			   DublinCore_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DublinCore(addr, DublinCore_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = DublinCore.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.DublinCore");
 
  /** @generated */
  final Feature casFeat_metadata;
  /** @generated */
  final int     casFeatCode_metadata;
  /** @generated */ 
  public int getMetadata(int addr) {
        if (featOkTst && casFeat_metadata == null)
      jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    return ll_cas.ll_getRefValue(addr, casFeatCode_metadata);
  }
  /** @generated */    
  public void setMetadata(int addr, int v) {
        if (featOkTst && casFeat_metadata == null)
      jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    ll_cas.ll_setRefValue(addr, casFeatCode_metadata, v);}
    
   /** @generated */
  public int getMetadata(int addr, int i) {
        if (featOkTst && casFeat_metadata == null)
      jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_metadata), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_metadata), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_metadata), i);
  }
   
  /** @generated */ 
  public void setMetadata(int addr, int i, int v) {
        if (featOkTst && casFeat_metadata == null)
      jcas.throwFeatMissing("metadata", "org.apache.uima.DublinCore");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_metadata), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_metadata), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_metadata), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DublinCore_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_metadata = jcas.getRequiredFeatureDE(casType, "metadata", "uima.cas.FSArray", featOkTst);
    casFeatCode_metadata  = (null == casFeat_metadata) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_metadata).getCode();

  }
}



    