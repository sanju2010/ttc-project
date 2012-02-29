
/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
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

/** 
 * Updated by JCasGen Wed Feb 29 11:17:07 CET 2012
 * @generated */
public class SingleWordTermAnnotation_Type extends TermAnnotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SingleWordTermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SingleWordTermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SingleWordTermAnnotation(addr, SingleWordTermAnnotation_Type.this);
  			   SingleWordTermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SingleWordTermAnnotation(addr, SingleWordTermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SingleWordTermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.SingleWordTermAnnotation");
 
  /** @generated */
  final Feature casFeat_compound;
  /** @generated */
  final int     casFeatCode_compound;
  /** @generated */ 
  public boolean getCompound(int addr) {
        if (featOkTst && casFeat_compound == null)
      jcas.throwFeatMissing("compound", "eu.project.ttc.types.SingleWordTermAnnotation");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_compound);
  }
  /** @generated */    
  public void setCompound(int addr, boolean v) {
        if (featOkTst && casFeat_compound == null)
      jcas.throwFeatMissing("compound", "eu.project.ttc.types.SingleWordTermAnnotation");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_compound, v);}
    
  
 
  /** @generated */
  final Feature casFeat_neoclassical;
  /** @generated */
  final int     casFeatCode_neoclassical;
  /** @generated */ 
  public boolean getNeoclassical(int addr) {
        if (featOkTst && casFeat_neoclassical == null)
      jcas.throwFeatMissing("neoclassical", "eu.project.ttc.types.SingleWordTermAnnotation");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_neoclassical);
  }
  /** @generated */    
  public void setNeoclassical(int addr, boolean v) {
        if (featOkTst && casFeat_neoclassical == null)
      jcas.throwFeatMissing("neoclassical", "eu.project.ttc.types.SingleWordTermAnnotation");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_neoclassical, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SingleWordTermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_compound = jcas.getRequiredFeatureDE(casType, "compound", "uima.cas.Boolean", featOkTst);
    casFeatCode_compound  = (null == casFeat_compound) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_compound).getCode();

 
    casFeat_neoclassical = jcas.getRequiredFeatureDE(casType, "neoclassical", "uima.cas.Boolean", featOkTst);
    casFeatCode_neoclassical  = (null == casFeat_neoclassical) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_neoclassical).getCode();

  }
}



    