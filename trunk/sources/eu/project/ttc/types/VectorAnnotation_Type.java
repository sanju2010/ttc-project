
/* First created by JCasGen Wed Nov 23 15:19:27 CET 2011 */
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
 * Updated by JCasGen Tue Jan 10 16:01:52 CET 2012
 * @generated */
public class VectorAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (VectorAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = VectorAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new VectorAnnotation(addr, VectorAnnotation_Type.this);
  			   VectorAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new VectorAnnotation(addr, VectorAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = VectorAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.VectorAnnotation");
 
  /** @generated */
  final Feature casFeat_item;
  /** @generated */
  final int     casFeatCode_item;
  /** @generated */ 
  public String getItem(int addr) {
        if (featOkTst && casFeat_item == null)
      jcas.throwFeatMissing("item", "eu.project.ttc.types.VectorAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_item);
  }
  /** @generated */    
  public void setItem(int addr, String v) {
        if (featOkTst && casFeat_item == null)
      jcas.throwFeatMissing("item", "eu.project.ttc.types.VectorAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_item, v);}
    
  
 
  /** @generated */
  final Feature casFeat_frequency;
  /** @generated */
  final int     casFeatCode_frequency;
  /** @generated */ 
  public int getFrequency(int addr) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "eu.project.ttc.types.VectorAnnotation");
    return ll_cas.ll_getIntValue(addr, casFeatCode_frequency);
  }
  /** @generated */    
  public void setFrequency(int addr, int v) {
        if (featOkTst && casFeat_frequency == null)
      jcas.throwFeatMissing("frequency", "eu.project.ttc.types.VectorAnnotation");
    ll_cas.ll_setIntValue(addr, casFeatCode_frequency, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public VectorAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_item = jcas.getRequiredFeatureDE(casType, "item", "uima.cas.String", featOkTst);
    casFeatCode_item  = (null == casFeat_item) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_item).getCode();

 
    casFeat_frequency = jcas.getRequiredFeatureDE(casType, "frequency", "uima.cas.Integer", featOkTst);
    casFeatCode_frequency  = (null == casFeat_frequency) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_frequency).getCode();

  }
}



    