
/* First created by JCasGen Thu Dec 02 15:49:55 CET 2010 */
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

/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * @generated */
public class TermAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TermAnnotation(addr, TermAnnotation_Type.this);
  			   TermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TermAnnotation(addr, TermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.TermAnnotation");
 
  /** @generated */
  final Feature casFeat_term;
  /** @generated */
  final int     casFeatCode_term;
  /** @generated */ 
  public int getTerm(int addr) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "org.apache.uima.TermAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_term);
  }
  /** @generated */    
  public void setTerm(int addr, int v) {
        if (featOkTst && casFeat_term == null)
      jcas.throwFeatMissing("term", "org.apache.uima.TermAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_term, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_term = jcas.getRequiredFeatureDE(casType, "term", "org.apache.uima.Term", featOkTst);
    casFeatCode_term  = (null == casFeat_term) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_term).getCode();

  }
}



    