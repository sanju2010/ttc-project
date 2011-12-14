
/* First created by JCasGen Thu Oct 20 17:03:42 CEST 2011 */
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
 * Updated by JCasGen Tue Dec 13 12:41:54 CET 2011
 * @generated */
public class MultiWordTermAnnotation_Type extends TermAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (MultiWordTermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = MultiWordTermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new MultiWordTermAnnotation(addr, MultiWordTermAnnotation_Type.this);
  			   MultiWordTermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new MultiWordTermAnnotation(addr, MultiWordTermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = MultiWordTermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.MultiWordTermAnnotation");
 
  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public MultiWordTermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

  }
}



    