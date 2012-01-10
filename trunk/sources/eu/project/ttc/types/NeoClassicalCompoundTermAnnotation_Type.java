
/* First created by JCasGen Sat Nov 05 19:54:33 CET 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;

/** 
 * Updated by JCasGen Tue Jan 10 16:01:52 CET 2012
 * @generated */
public class NeoClassicalCompoundTermAnnotation_Type extends TermAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (NeoClassicalCompoundTermAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = NeoClassicalCompoundTermAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new NeoClassicalCompoundTermAnnotation(addr, NeoClassicalCompoundTermAnnotation_Type.this);
  			   NeoClassicalCompoundTermAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new NeoClassicalCompoundTermAnnotation(addr, NeoClassicalCompoundTermAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = NeoClassicalCompoundTermAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.project.ttc.types.NeoClassicalCompoundTermAnnotation");



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public NeoClassicalCompoundTermAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

  }
}



    