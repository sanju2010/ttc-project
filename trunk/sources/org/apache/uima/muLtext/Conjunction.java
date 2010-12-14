

/* First created by JCasGen Thu Dec 02 15:48:29 CET 2010 */
package org.apache.uima.muLtext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class Conjunction extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Conjunction.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Conjunction() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Conjunction(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Conjunction(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Conjunction(JCas jcas, int begin, int end) {
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

    