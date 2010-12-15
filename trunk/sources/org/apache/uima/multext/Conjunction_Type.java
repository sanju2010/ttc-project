
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
public class Conjunction_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Conjunction_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Conjunction_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Conjunction(addr, Conjunction_Type.this);
  			   Conjunction_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Conjunction(addr, Conjunction_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Conjunction.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Conjunction");
 
  /** @generated */
  final Feature casFeat_conjunctionType;
  /** @generated */
  final int     casFeatCode_conjunctionType;
  /** @generated */ 
  public String getConjunctionType(int addr) {
        if (featOkTst && casFeat_conjunctionType == null)
      jcas.throwFeatMissing("conjunctionType", "org.apache.uima.multext.Conjunction");
    return ll_cas.ll_getStringValue(addr, casFeatCode_conjunctionType);
  }
  /** @generated */    
  public void setConjunctionType(int addr, String v) {
        if (featOkTst && casFeat_conjunctionType == null)
      jcas.throwFeatMissing("conjunctionType", "org.apache.uima.multext.Conjunction");
    ll_cas.ll_setStringValue(addr, casFeatCode_conjunctionType, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Conjunction_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_conjunctionType = jcas.getRequiredFeatureDE(casType, "conjunctionType", "uima.cas.String", featOkTst);
    casFeatCode_conjunctionType  = (null == casFeat_conjunctionType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_conjunctionType).getCode();

  }
}



    