

/* First created by JCasGen Tue Dec 14 21:11:21 CET 2010 */
package org.apache.uima.multext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Wed Dec 15 10:23:54 CET 2010
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-project/trunk/resources/eu/project/ttc/models/TTC Document Processor.xml
 * @generated */
public class Unique extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Unique.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Unique() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Unique(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Unique(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Unique(JCas jcas, int begin, int end) {
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

    