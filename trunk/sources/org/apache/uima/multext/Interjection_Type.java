
/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.MultextAnnotation_Type;

/** 
 * Updated by JCasGen Wed Dec 15 10:23:54 CET 2010
 * @generated */
public class Interjection_Type extends MultextAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Interjection_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Interjection_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Interjection(addr, Interjection_Type.this);
  			   Interjection_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Interjection(addr, Interjection_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Interjection.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.multext.Interjection");



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Interjection_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

  }
}



    