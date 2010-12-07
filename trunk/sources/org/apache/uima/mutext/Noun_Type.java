
/* First created by JCasGen Thu Dec 02 15:44:57 CET 2010 */
package org.apache.uima.mutext;

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
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * @generated */
public class Noun_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Noun_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Noun_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Noun(addr, Noun_Type.this);
  			   Noun_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Noun(addr, Noun_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Noun.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.mutext.Noun");



  /** @generated */
  final Feature casFeat_nounType;
  /** @generated */
  final int     casFeatCode_nounType;
  /** @generated */ 
  public String getNounType(int addr) {
        if (featOkTst && casFeat_nounType == null)
      jcas.throwFeatMissing("nounType", "org.apache.uima.mutext.Noun");
    return ll_cas.ll_getStringValue(addr, casFeatCode_nounType);
  }
  /** @generated */    
  public void setNounType(int addr, String v) {
        if (featOkTst && casFeat_nounType == null)
      jcas.throwFeatMissing("nounType", "org.apache.uima.mutext.Noun");
    ll_cas.ll_setStringValue(addr, casFeatCode_nounType, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Noun_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_nounType = jcas.getRequiredFeatureDE(casType, "nounType", "uima.cas.String", featOkTst);
    casFeatCode_nounType  = (null == casFeat_nounType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_nounType).getCode();

  }
}



    