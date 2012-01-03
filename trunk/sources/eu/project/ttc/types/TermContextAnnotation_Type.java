
/* First created by JCasGen Wed Nov 23 11:46:56 CET 2011 */
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
 * Updated by JCasGen Tue Jan 03 14:27:17 CET 2012
 * @generated */
public class TermContextAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TermContextAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TermContextAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TermContextAnnotation(addr, TermContextAnnotation_Type.this);
  			   TermContextAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TermContextAnnotation(addr, TermContextAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TermContextAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TermContextAnnotation");
 
  /** @generated */
  final Feature casFeat_context;
  /** @generated */
  final int     casFeatCode_context;
  /** @generated */ 
  public int getContext(int addr) {
        if (featOkTst && casFeat_context == null)
      jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_context);
  }
  /** @generated */    
  public void setContext(int addr, int v) {
        if (featOkTst && casFeat_context == null)
      jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_context, v);}
    
   /** @generated */
  public int getContext(int addr, int i) {
        if (featOkTst && casFeat_context == null)
      jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_context), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_context), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_context), i);
  }
   
  /** @generated */ 
  public void setContext(int addr, int i, int v) {
        if (featOkTst && casFeat_context == null)
      jcas.throwFeatMissing("context", "eu.project.ttc.types.TermContextAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_context), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_context), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_context), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_term;
  /** @generated */
  final int     casFeatCode_term;
  /** @generated */ 
  public int getTerm(int addr) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_term);
  }
  /** @generated */    
  public void setTerm(int addr, int v) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_term, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermContextAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_context = jcas.getRequiredFeatureDE(casType, "context", "uima.cas.FSArray", featOkTst);
    casFeatCode_context  = (null == casFeat_context) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_context).getCode();

 
    casFeat_term = jcas.getRequiredFeatureDE(casType, "term", "eu.project.ttc.types.TermAnnotation", featOkTst);
    casFeatCode_term  = (null == casFeat_term) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_term).getCode();

  }
}



    