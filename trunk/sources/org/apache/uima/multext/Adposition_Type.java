
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
public class Adposition_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Adposition_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Adposition_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Adposition(addr, Adposition_Type.this);
  			   Adposition_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Adposition(addr, Adposition_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Adposition.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Adposition");
 
  /** @generated */
  final Feature casFeat_adpositionType;
  /** @generated */
  final int     casFeatCode_adpositionType;
  /** @generated */ 
  public String getAdpositionType(int addr) {
        if (featOkTst && casFeat_adpositionType == null)
      jcas.throwFeatMissing("adpositionType", "org.apache.uima.multext.Adposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_adpositionType);
  }
  /** @generated */    
  public void setAdpositionType(int addr, String v) {
        if (featOkTst && casFeat_adpositionType == null)
      jcas.throwFeatMissing("adpositionType", "org.apache.uima.multext.Adposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_adpositionType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_formation;
  /** @generated */
  final int     casFeatCode_formation;
  /** @generated */ 
  public String getFormation(int addr) {
        if (featOkTst && casFeat_formation == null)
      jcas.throwFeatMissing("formation", "org.apache.uima.multext.Adposition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_formation);
  }
  /** @generated */    
  public void setFormation(int addr, String v) {
        if (featOkTst && casFeat_formation == null)
      jcas.throwFeatMissing("formation", "org.apache.uima.multext.Adposition");
    ll_cas.ll_setStringValue(addr, casFeatCode_formation, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Adposition_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_adpositionType = jcas.getRequiredFeatureDE(casType, "adpositionType", "uima.cas.String", featOkTst);
    casFeatCode_adpositionType  = (null == casFeat_adpositionType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_adpositionType).getCode();

 
    casFeat_formation = jcas.getRequiredFeatureDE(casType, "formation", "uima.cas.String", featOkTst);
    casFeatCode_formation  = (null == casFeat_formation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_formation).getCode();

  }
}



    