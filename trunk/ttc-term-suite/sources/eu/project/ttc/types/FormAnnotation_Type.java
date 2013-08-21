
/* First created by JCasGen Fri Jul 20 14:01:15 CEST 2012 */
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

/** Represents a form of a given term.
 * Updated by JCasGen Mon Jul 23 14:49:38 CEST 2012
 * @generated */
public class FormAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (FormAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = FormAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new FormAnnotation(addr, FormAnnotation_Type.this);
  			   FormAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new FormAnnotation(addr, FormAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = FormAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.FormAnnotation");
 
  /** @generated */
  final Feature casFeat_form;
  /** @generated */
  final int     casFeatCode_form;
  /** @generated */ 
  public String getForm(int addr) {
        if (featOkTst && casFeat_form == null)
      jcas.throwFeatMissing("form", "eu.project.ttc.types.FormAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_form);
  }
  /** @generated */    
  public void setForm(int addr, String v) {
        if (featOkTst && casFeat_form == null)
      jcas.throwFeatMissing("form", "eu.project.ttc.types.FormAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_form, v);}
    
  
 
  /** @generated */
  final Feature casFeat_occurrences;
  /** @generated */
  final int     casFeatCode_occurrences;
  /** @generated */ 
  public int getOccurrences(int addr) {
        if (featOkTst && casFeat_occurrences == null)
      jcas.throwFeatMissing("occurrences", "eu.project.ttc.types.FormAnnotation");
    return ll_cas.ll_getIntValue(addr, casFeatCode_occurrences);
  }
  /** @generated */    
  public void setOccurrences(int addr, int v) {
        if (featOkTst && casFeat_occurrences == null)
      jcas.throwFeatMissing("occurrences", "eu.project.ttc.types.FormAnnotation");
    ll_cas.ll_setIntValue(addr, casFeatCode_occurrences, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public FormAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_form = jcas.getRequiredFeatureDE(casType, "form", "uima.cas.String", featOkTst);
    casFeatCode_form  = (null == casFeat_form) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_form).getCode();

 
    casFeat_occurrences = jcas.getRequiredFeatureDE(casType, "occurrences", "uima.cas.Integer", featOkTst);
    casFeatCode_occurrences  = (null == casFeat_occurrences) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_occurrences).getCode();

  }
}



    