
/* First created by JCasGen Tue Oct 25 10:43:56 CEST 2011 */
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
 * Updated by JCasGen Sat Nov 05 19:54:33 CET 2011
 * @generated */
public class TermContextVectorAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TermContextVectorAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TermContextVectorAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TermContextVectorAnnotation(addr, TermContextVectorAnnotation_Type.this);
  			   TermContextVectorAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TermContextVectorAnnotation(addr, TermContextVectorAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TermContextVectorAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TermContextVectorAnnotation");
 
  /** @generated */
  final Feature casFeat_contextVector;
  /** @generated */
  final int     casFeatCode_contextVector;
  /** @generated */ 
  public int getContextVector(int addr) {
        if (featOkTst && casFeat_contextVector == null)
      jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_contextVector);
  }
  /** @generated */    
  public void setContextVector(int addr, int v) {
        if (featOkTst && casFeat_contextVector == null)
      jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_contextVector, v);}
    
   /** @generated */
  public int getContextVector(int addr, int i) {
        if (featOkTst && casFeat_contextVector == null)
      jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_contextVector), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_contextVector), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_contextVector), i);
  }
   
  /** @generated */ 
  public void setContextVector(int addr, int i, int v) {
        if (featOkTst && casFeat_contextVector == null)
      jcas.throwFeatMissing("contextVector", "eu.project.ttc.types.TermContextVectorAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_contextVector), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_contextVector), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_contextVector), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_term;
  /** @generated */
  final int     casFeatCode_term;
  /** @generated */ 
  public int getTerm(int addr) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextVectorAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_term);
  }
  /** @generated */    
  public void setTerm(int addr, int v) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "eu.project.ttc.types.TermContextVectorAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_term, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermContextVectorAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_contextVector = jcas.getRequiredFeatureDE(casType, "contextVector", "uima.cas.FSArray", featOkTst);
    casFeatCode_contextVector  = (null == casFeat_contextVector) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_contextVector).getCode();

 
    casFeat_term = jcas.getRequiredFeatureDE(casType, "term", "eu.project.ttc.types.TermContextEntryAnnotation", featOkTst);
    casFeatCode_term  = (null == casFeat_term) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_term).getCode();

  }
}



    