/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.pcpcustom.core.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.pcpcustom.core.entity.SampleEntityClassName.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Sun Jan 03 21:34:05 CST 2021")
public class SampleEntityClassName_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<SampleEntityClassName,String> alias;
    public static volatile SingularAttribute<SampleEntityClassName,Date> date;
    public static volatile SingularAttribute<SampleEntityClassName,String> id;
    public static volatile SingularAttribute<SampleEntityClassName,String> name;
    public static volatile SingularAttribute<SampleEntityClassName,Integer> orderNumber;
}