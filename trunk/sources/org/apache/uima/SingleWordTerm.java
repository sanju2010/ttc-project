

/* First created by JCasGen Thu Dec 02 15:51:13 CET 2010 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class SingleWordTerm extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(SingleWordTerm.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SingleWordTerm() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SingleWordTerm(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SingleWordTerm(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SingleWordTerm(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
}

    