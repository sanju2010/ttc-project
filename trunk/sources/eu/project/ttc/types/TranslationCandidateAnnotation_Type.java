
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Feb 24 09:30:03 CET 2012
 * @generated */
public class TranslationCandidateAnnotation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TranslationCandidateAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TranslationCandidateAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TranslationCandidateAnnotation(addr, TranslationCandidateAnnotation_Type.this);
  			   TranslationCandidateAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TranslationCandidateAnnotation(addr, TranslationCandidateAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = TranslationCandidateAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.TranslationCandidateAnnotation");
 
  /** @generated */
  final Feature casFeat_translation;
  /** @generated */
  final int     casFeatCode_translation;
  /** @generated */ 
  public String getTranslation(int addr) {
        if (featOkTst && casFeat_translation == null)
      jcas.throwFeatMissing("translation", "eu.project.ttc.types.TranslationCandidateAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_translation);
  }
  /** @generated */    
  public void setTranslation(int addr, String v) {
        if (featOkTst && casFeat_translation == null)
      jcas.throwFeatMissing("translation", "eu.project.ttc.types.TranslationCandidateAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_translation, v);}
    
  
 
  /** @generated */
  final Feature casFeat_score;
  /** @generated */
  final int     casFeatCode_score;
  /** @generated */ 
  public double getScore(int addr) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.project.ttc.types.TranslationCandidateAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_score);
  }
  /** @generated */    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.project.ttc.types.TranslationCandidateAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_score, v);}
    
  
 
  /** @generated */
  final Feature casFeat_rank;
  /** @generated */
  final int     casFeatCode_rank;
  /** @generated */ 
  public int getRank(int addr) {
        if (featOkTst && casFeat_rank == null)
      jcas.throwFeatMissing("rank", "eu.project.ttc.types.TranslationCandidateAnnotation");
    return ll_cas.ll_getIntValue(addr, casFeatCode_rank);
  }
  /** @generated */    
  public void setRank(int addr, int v) {
        if (featOkTst && casFeat_rank == null)
      jcas.throwFeatMissing("rank", "eu.project.ttc.types.TranslationCandidateAnnotation");
    ll_cas.ll_setIntValue(addr, casFeatCode_rank, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TranslationCandidateAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_translation = jcas.getRequiredFeatureDE(casType, "translation", "uima.cas.String", featOkTst);
    casFeatCode_translation  = (null == casFeat_translation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_translation).getCode();

 
    casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.Double", featOkTst);
    casFeatCode_score  = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_score).getCode();

 
    casFeat_rank = jcas.getRequiredFeatureDE(casType, "rank", "uima.cas.Integer", featOkTst);
    casFeatCode_rank  = (null == casFeat_rank) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rank).getCode();

  }
}



    