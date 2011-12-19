

/* First created by JCasGen Thu Oct 20 17:03:42 CEST 2011 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Thu Dec 15 23:50:29 CET 2011
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class SingleWordTermAnnotation extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(SingleWordTermAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SingleWordTermAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SingleWordTermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SingleWordTermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SingleWordTermAnnotation(JCas jcas, int begin, int end) {
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

    